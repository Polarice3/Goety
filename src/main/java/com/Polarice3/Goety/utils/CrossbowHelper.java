package com.Polarice3.Goety.utils;

import com.google.common.collect.Lists;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class CrossbowHelper {

    public static List<ItemStack> getChargedProjectiles(ItemStack p_40942_) {
        List<ItemStack> list = Lists.newArrayList();
        CompoundNBT compoundtag = p_40942_.getTag();
        if (compoundtag != null && compoundtag.contains("ChargedProjectiles", 9)) {
            ListNBT listtag = compoundtag.getList("ChargedProjectiles", 10);
            if (listtag != null) {
                for(int i = 0; i < listtag.size(); ++i) {
                    CompoundNBT compoundtag1 = listtag.getCompound(i);
                    list.add(ItemStack.of(compoundtag1));
                }
            }
        }

        return list;
    }

    public static float[] getShotPitches(Random p_220024_) {
        boolean flag = p_220024_.nextBoolean();
        return new float[]{1.0F, getRandomShotPitch(flag, p_220024_), getRandomShotPitch(!flag, p_220024_)};
    }

    public static float getRandomShotPitch(boolean p_220026_, Random p_220027_) {
        float f = p_220026_ ? 0.63F : 0.43F;
        return 1.0F / (p_220027_.nextFloat() * 0.5F + 1.8F) + f;
    }

    public static void performCustomShooting(World level, LivingEntity shooter, Hand hand, ItemStack crossbow, ProjectileEntity projectile, float velocity, float p_40893_) {
        performCustomShooting(level, shooter, hand, crossbow, projectile, SoundEvents.CROSSBOW_SHOOT, velocity, p_40893_);
    }

    public static void performCustomShooting(World level, LivingEntity shooter, Hand hand, ItemStack crossbow, ProjectileEntity projectile, SoundEvent soundEvent, float velocity, float p_40893_) {
        if (shooter instanceof PlayerEntity && net.minecraftforge.event.ForgeEventFactory.onArrowLoose(crossbow, shooter.level, (PlayerEntity) shooter, 1, true) < 0) return;
        List<ItemStack> list = getChargedProjectiles(crossbow);
        float[] afloat = getShotPitches(shooter.getRandom());

        for(int i = 0; i < list.size(); ++i) {
            ItemStack itemstack = list.get(i);
            if (!itemstack.isEmpty()) {
                if (i == 0) {
                    shootCustomProjectile(level, shooter, hand, crossbow, projectile, soundEvent, afloat[i], velocity, p_40893_, 0.0F);
                } else if (i == 1) {
                    shootCustomProjectile(level, shooter, hand, crossbow, projectile, soundEvent, afloat[i], velocity, p_40893_, -10.0F);
                } else if (i == 2) {
                    shootCustomProjectile(level, shooter, hand, crossbow, projectile, soundEvent, afloat[i], velocity, p_40893_, 10.0F);
                }
            }
        }

        onCrossbowShot(level, shooter, crossbow);
    }

    public static void onCrossbowShot(World p_40906_, LivingEntity p_40907_, ItemStack p_40908_) {
        if (p_40907_ instanceof ServerPlayerEntity) {
            if (!p_40906_.isClientSide) {
                CriteriaTriggers.SHOT_CROSSBOW.trigger((ServerPlayerEntity) p_40907_, p_40908_);
            }

            ((ServerPlayerEntity) p_40907_).awardStat(Stats.ITEM_USED.get(p_40908_.getItem()));
        }

        clearChargedProjectiles(p_40908_);
    }

    public static void clearChargedProjectiles(ItemStack p_40944_) {
        CompoundNBT compoundtag = p_40944_.getTag();
        if (compoundtag != null) {
            ListNBT listtag = compoundtag.getList("ChargedProjectiles", 9);
            listtag.clear();
            compoundtag.put("ChargedProjectiles", listtag);
        }

    }

    public static void shootCustomProjectile(World level, LivingEntity shooter, Hand hand, ItemStack crossbow, ProjectileEntity projectile, SoundEvent soundEvent, float pitch, float velocity, float p_40903_, float p_40904_) {
        if (!level.isClientSide) {
            if (shooter instanceof ICrossbowUser) {
                ICrossbowUser crossbowattackmob = (ICrossbowUser) shooter;
                if (crossbowattackmob.getTarget() != null) {
                    crossbowattackmob.shootCrossbowProjectile(crossbowattackmob.getTarget(), crossbow, projectile, p_40904_);
                }
            } else {
                Vector3d vec31 = shooter.getUpVector(1.0F);
                Quaternion quaternion = new Quaternion(new Vector3f(vec31), p_40904_, true);
                Vector3d vec3 = shooter.getViewVector(1.0F);
                Vector3f vector3f = new Vector3f(vec3);
                vector3f.transform(quaternion);
                projectile.shoot((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), velocity, p_40903_);
            }

            crossbow.hurtAndBreak(1, shooter, (p_40858_) -> {
                p_40858_.broadcastBreakEvent(hand);
            });
            level.addFreshEntity(projectile);
            level.playSound((PlayerEntity) null, shooter.getX(), shooter.getY(), shooter.getZ(), soundEvent, SoundCategory.PLAYERS, 1.0F, pitch);
        }
    }
}
