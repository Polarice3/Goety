package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.soulenergy.ISoulEnergy;
import com.Polarice3.Goety.common.spells.BreathSpell;
import com.Polarice3.Goety.common.spells.SpewingSpell;
import com.Polarice3.Goety.utils.GoldTotemFinder;
import com.Polarice3.Goety.utils.SEHelper;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.village.GossipType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SoulStaff extends SoulWand{
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public SoulStaff() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 7.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)-2.4F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlotType.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    public void MagicResults(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        ItemStack foundStack;
        PlayerEntity playerEntity = (PlayerEntity) entityLiving;
        foundStack = GoldTotemFinder.FindTotem(playerEntity);
        ISoulEnergy soulEnergy = SEHelper.getCapability(playerEntity);
        if (!worldIn.isClientSide) {
            ServerWorld serverWorld = (ServerWorld) worldIn;
            if (this.getSpell(stack) != null) {
                if (SEHelper.getSEActive(playerEntity)) {
                    if (soulEnergy.getSoulEnergy() >= SoulUse(entityLiving, stack)) {
                        int random = worldIn.random.nextInt(4);
                        if (this.getSpell(stack) instanceof SpewingSpell || this.getSpell(stack) instanceof BreathSpell) {
                            if (random == 0) {
                                soulEnergy.decreaseSE(SoulUse(entityLiving, stack));
                                SEHelper.sendSEUpdatePacket(playerEntity);
                                if (MainConfig.VillagerHateSpells.get() > 0) {
                                    for (VillagerEntity villager : entityLiving.level.getEntitiesOfClass(VillagerEntity.class, entityLiving.getBoundingBox().inflate(16.0D))) {
                                        villager.getGossips().add(entityLiving.getUUID(), GossipType.MINOR_NEGATIVE, MainConfig.VillagerHateSpells.get());
                                    }
                                }
                            }
                        } else {
                            soulEnergy.decreaseSE(SoulUse(entityLiving, stack));
                            SEHelper.sendSEUpdatePacket(playerEntity);
                            if (MainConfig.VillagerHateSpells.get() > 0) {
                                for (VillagerEntity villager : entityLiving.level.getEntitiesOfClass(VillagerEntity.class, entityLiving.getBoundingBox().inflate(16.0D))) {
                                    villager.getGossips().add(entityLiving.getUUID(), GossipType.MINOR_NEGATIVE, MainConfig.VillagerHateSpells.get());
                                }
                            }
                        }
                        assert stack.getTag() != null;
                        this.getSpell(stack).StaffResult(serverWorld, entityLiving);
                    } else {
                        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    }
                } else if (!foundStack.isEmpty() && GoldTotemItem.currentSouls(foundStack) >= SoulUse(entityLiving, stack)) {
                    int random = worldIn.random.nextInt(4);
                    if (this.getSpell(stack) instanceof SpewingSpell || this.getSpell(stack) instanceof BreathSpell) {
                        if (random == 0) {
                            GoldTotemItem.decreaseSouls(foundStack, SoulUse(entityLiving, stack));
                            if (MainConfig.VillagerHateSpells.get() > 0) {
                                for (VillagerEntity villager : entityLiving.level.getEntitiesOfClass(VillagerEntity.class, entityLiving.getBoundingBox().inflate(16.0D))) {
                                    villager.getGossips().add(entityLiving.getUUID(), GossipType.MINOR_NEGATIVE, MainConfig.VillagerHateSpells.get());
                                }
                            }
                        }
                    } else {
                        GoldTotemItem.decreaseSouls(foundStack, SoulUse(entityLiving, stack));
                        if (MainConfig.VillagerHateSpells.get() > 0) {
                            for (VillagerEntity villager : entityLiving.level.getEntitiesOfClass(VillagerEntity.class, entityLiving.getBoundingBox().inflate(16.0D))) {
                                villager.getGossips().add(entityLiving.getUUID(), GossipType.MINOR_NEGATIVE, MainConfig.VillagerHateSpells.get());
                            }
                        }
                    }
                    assert stack.getTag() != null;
                    this.getSpell(stack).StaffResult(serverWorld, entityLiving);
                } else {
                    worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                }
            } else {
                worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
        }
        if (worldIn.isClientSide){
            if (this.getSpell(stack) != null) {
                if (SEHelper.getSEActive(playerEntity)) {
                    if (soulEnergy.getSoulEnergy() < SoulUse(entityLiving, stack)) {
                        this.failParticles(worldIn, entityLiving);
                    } else if (this.getSpell(stack) instanceof SpewingSpell){
                        SpewingSpell spewingSpell = (SpewingSpell) this.getSpell(stack);
                        spewingSpell.showStaffBreath(entityLiving);
                    }
                } else if (foundStack.isEmpty() || GoldTotemItem.currentSouls(foundStack) < SoulUse(entityLiving, stack)) {
                    this.failParticles(worldIn, entityLiving);
                } else if (this.getSpell(stack) instanceof SpewingSpell){
                    SpewingSpell spewingSpell = (SpewingSpell) this.getSpell(stack);
                    spewingSpell.showStaffBreath(entityLiving);
                }
            } else {
                this.failParticles(worldIn, entityLiving);
            }
        }
    }
}
