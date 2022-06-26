package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.common.entities.hostile.HuskarlEntity;
import com.Polarice3.Goety.common.entities.hostile.dead.IDeadMob;
import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class MobUtil {
    public static void deadSandConvert(Entity entity, boolean natural){
        if (entity instanceof MonsterEntity && !(entity instanceof IDeadMob)){
            MonsterEntity monster = (MonsterEntity) entity;
            MonsterEntity corrupt = null;
            if (monster instanceof CreeperEntity) {
                corrupt = monster.convertTo(ModEntityType.BOOMER.get(), false);
            }
            if (monster instanceof SpiderEntity) {
                corrupt = monster.convertTo(ModEntityType.DUNE_SPIDER.get(), false);
            }
            if (monster instanceof ZombieEntity && !(monster instanceof HuskarlEntity)) {
                corrupt = monster.convertTo(ModEntityType.FALLEN.get(), true);
            }
            if (monster instanceof AbstractSkeletonEntity && !(monster instanceof WitherSkeletonEntity)) {
                if (monster.level.random.nextFloat() < 0.1F){
                    corrupt = monster.convertTo(ModEntityType.MARCIRE.get(), false);
                } else {
                    corrupt = monster.convertTo(ModEntityType.DESICCATED.get(), true);
                }
            }
            if (corrupt != null) {
                corrupt.finalizeSpawn((IServerWorld) corrupt.level, corrupt.level.getCurrentDifficultyAt(corrupt.blockPosition()), SpawnReason.CONVERSION, null, null);
                if (!natural){
                    net.minecraftforge.event.ForgeEventFactory.onLivingConvert((LivingEntity) entity, corrupt);
                    new SoundUtil(corrupt.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundCategory.HOSTILE, 1.0F, 1.0F);
                }
            }
        }
    }

    public static boolean nonDesiccateExplodeEntities(Entity entity){
        return !(entity instanceof CreeperEntity)
                && !(entity instanceof SpiderEntity)
                && !(entity instanceof ZombieEntity)
                && !(entity instanceof AbstractSkeletonEntity);
    }

    public static boolean playerValidity(PlayerEntity player, boolean lich){
        if (!player.isCreative() && !player.isSpectator()) {
            if (lich) {
                return !LichdomHelper.isLich(player);
            } else {
                return true;
            }
        }
        return false;
    }

    public static LootContext.Builder createLootContext(DamageSource pDamageSource, LivingEntity livingEntity) {
        LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld)livingEntity.level)).withRandom(livingEntity.getRandom()).withParameter(LootParameters.THIS_ENTITY, livingEntity).withParameter(LootParameters.ORIGIN, livingEntity.position()).withParameter(LootParameters.DAMAGE_SOURCE, pDamageSource).withOptionalParameter(LootParameters.KILLER_ENTITY, pDamageSource.getEntity()).withOptionalParameter(LootParameters.DIRECT_KILLER_ENTITY, pDamageSource.getDirectEntity());
        if (livingEntity.getLastHurtByMob() != null && livingEntity.getLastHurtByMob() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) livingEntity.getLastHurtByMob();
            lootcontext$builder = lootcontext$builder.withParameter(LootParameters.LAST_DAMAGE_PLAYER, player).withLuck(player.getLuck());
        }

        return lootcontext$builder;
    }

    public static class MoveHelperController extends MovementController {
        public MoveHelperController(MobEntity vex) {
            super(vex);
        }

        public void tick() {
            if (this.operation == Action.MOVE_TO) {
                Vector3d vector3d = new Vector3d(this.wantedX - this.mob.getX(), this.wantedY - this.mob.getY(), this.wantedZ - this.mob.getZ());
                double d0 = vector3d.length();
                if (d0 < this.mob.getBoundingBox().getSize()) {
                    this.operation = Action.WAIT;
                    this.mob.setDeltaMovement(this.mob.getDeltaMovement().scale(0.5D));
                } else {
                    this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.05D / d0)));
                    if (this.mob.getTarget() == null) {
                        Vector3d vector3d1 = this.mob.getDeltaMovement();
                        this.mob.yRot = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float)Math.PI);
                    } else {
                        double d2 = this.mob.getTarget().getX() - this.mob.getX();
                        double d1 = this.mob.getTarget().getZ() - this.mob.getZ();
                        this.mob.yRot = -((float)MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
                    }
                    this.mob.yBodyRot = this.mob.yRot;
                }

            }
        }
    }

    public static int getSummonLifespan(World world){
        return 20 * (30 + world.random.nextInt(90));
    }
}
