package com.Polarice3.Goety.common.capabilities.soulenergy;

import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.network.packets.server.SFocusCooldownPacket;
import com.google.common.collect.Maps;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Map;

/**
 * Made custom cooldown mechanic based of vanilla item cooldowns for any future tinkering. And also to prevent an exploit where relogging can reset item cooldown.".
 */
public class FocusCooldown {
    public final Map<Item, CooldownInstance> cooldowns = Maps.newHashMap();

    public boolean isOnCooldown(Item item) {
        return this.getCooldownPercent(item) > 0.0F;
    }

    public float getCooldownPercent(Item item) {
        CooldownInstance cooldownInstance = this.cooldowns.get(item);
        if (cooldownInstance != null) {
            return MathHelper.clamp((float) cooldownInstance.time / cooldownInstance.totalTime, 0.0F, 1.0F);
        } else {
            return 0.0F;
        }
    }

    public void tick(World level) {
        if (!this.cooldowns.isEmpty()) {
            Iterator<Map.Entry<Item, CooldownInstance>> iterator = this.cooldowns.entrySet().iterator();

            while(iterator.hasNext()) {
                Map.Entry<Item, CooldownInstance> entry = iterator.next();
                entry.getValue().decreaseTime();
                if (entry.getValue().time <= 0) {
                    iterator.remove();
                    if (!level.isClientSide) {
                        this.onCooldownEnded(entry.getKey());
                    }
                }
            }
        }
    }

    public void addCooldown(World level, Item item, int coolDown) {
        this.cooldowns.put(item, new CooldownInstance(coolDown));
        if (!level.isClientSide) {
            this.onCooldownStarted(item, coolDown);
        }
    }

    public void removeCooldown(World level, Item item) {
        this.cooldowns.remove(item);
        if (!level.isClientSide) {
            this.onCooldownEnded(item);
        }
    }

    protected void onCooldownStarted(Item item, int duration) {
        ModNetwork.sendToALL(new SFocusCooldownPacket(item, duration));
    }

    protected void onCooldownEnded(Item item) {
        ModNetwork.sendToALL(new SFocusCooldownPacket(item, 0));
    }

    public CooldownInstance getInstance(Item item){
        return this.cooldowns.get(item);
    }

    public void setCooldown(Item item, CooldownInstance cooldownInstance){
        this.cooldowns.put(item, cooldownInstance);
    }

    public void save(ListNBT listTag) {
        cooldowns.forEach((item, cooldown) -> {
            if (isOnCooldown(item)) {
                CompoundNBT compoundTag = new CompoundNBT();
                compoundTag.putInt("Item", Item.getId(item));
                compoundTag.putInt("Time", cooldown.time);
                compoundTag.putInt("TotalTime", cooldown.totalTime);
                listTag.add(compoundTag);
            }
        });
    }

    public void load(ListNBT listTag){
        if (listTag != null) {
            listTag.forEach(tag -> {
                CompoundNBT compoundTag = (CompoundNBT) tag;
                Item item = Item.byId(compoundTag.getInt("Item"));
                int startTime = compoundTag.getInt("Time");
                int endTime = compoundTag.getInt("TotalTime");
                cooldowns.put(item, new CooldownInstance(startTime, endTime));
            });
        }
    }

    public static class CooldownInstance {
        int time;
        final int totalTime;

        public CooldownInstance(int time){
            this(time, time);
        }

        public CooldownInstance(int time, int totalTime) {
            this.time = time;
            this.totalTime = totalTime;
        }

        public void decreaseTime(){
            if (this.time > 0){
                --this.time;
            }
        }
    }
}
