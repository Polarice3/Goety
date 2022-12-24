package com.Polarice3.Goety.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class FrostExplosion extends Explosion {
    public List<BlockPos> toPacked = Lists.newArrayList();

    public FrostExplosion(World pLevel, @Nullable Entity pSource, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, Explosion.Mode pBlockInteraction) {
        super(pLevel, pSource, null, null, pToBlowX, pToBlowY, pToBlowZ, pRadius, false, pBlockInteraction);
    }

    public void explode() {
        Set<BlockPos> set = Sets.newHashSet();
        int i = 16;

        for(int j = 0; j < i; ++j) {
            for(int k = 0; k < i; ++k) {
                for(int l = 0; l < i; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d0 = (double)((float)j / 15.0F * 2.0F - 1.0F);
                        double d1 = (double)((float)k / 15.0F * 2.0F - 1.0F);
                        double d2 = (double)((float)l / 15.0F * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 = d0 / d3;
                        d1 = d1 / d3;
                        d2 = d2 / d3;
                        float f = this.radius * (0.7F + this.level.random.nextFloat() * 0.6F);
                        double d4 = this.x;
                        double d6 = this.y;
                        double d8 = this.z;

                        for(float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                            BlockPos blockpos = new BlockPos(d4, d6, d8);
                            BlockState blockstate = this.level.getBlockState(blockpos);
                            FluidState fluidstate = this.level.getFluidState(blockpos);
                            Optional<Float> optional = this.damageCalculator.getBlockExplosionResistance(this, this.level, blockpos, blockstate, fluidstate);
                            if (optional.isPresent()) {
                                f -= (optional.get() + 0.3F) * 0.3F;
                            }

                            if (!fluidstate.isEmpty() && fluidstate.isSource() && fluidstate.getType() == Fluids.WATER){
                                set.add(blockpos);
                            }

                            if (f > 0.0F && this.damageCalculator.shouldBlockExplode(this, this.level, blockpos, blockstate, f)) {
                                set.add(blockpos);
                            }

                            if (blockstate.getBlock() == Blocks.ICE){
                                set.remove(blockpos);
                                this.toPacked.add(blockpos);
                            }

                            d4 += d0 * (double)0.3F;
                            d6 += d1 * (double)0.3F;
                            d8 += d2 * (double)0.3F;
                        }
                    }
                }
            }
        }

        this.toBlow.addAll(set);
        float f2 = this.radius * 2.0F;
        int k1 = MathHelper.floor(this.x - (double)f2 - 1.0D);
        int l1 = MathHelper.floor(this.x + (double)f2 + 1.0D);
        int i2 = MathHelper.floor(this.y - (double)f2 - 1.0D);
        int i1 = MathHelper.floor(this.y + (double)f2 + 1.0D);
        int j2 = MathHelper.floor(this.z - (double)f2 - 1.0D);
        int j1 = MathHelper.floor(this.z + (double)f2 + 1.0D);
        List<Entity> list = this.level.getEntities(this.source, new AxisAlignedBB((double)k1, (double)i2, (double)j2, (double)l1, (double)i1, (double)j1));
        net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.level, this, list, f2);
        Vector3d vector3d = new Vector3d(this.x, this.y, this.z);

        for(int k2 = 0; k2 < list.size(); ++k2) {
            Entity entity = list.get(k2);
            if (!entity.ignoreExplosion()) {
                double d12 = (double)(MathHelper.sqrt(entity.distanceToSqr(vector3d)) / f2);
                if (d12 <= 1.0D) {
                    double d5 = entity.getX() - this.x;
                    double d7 = (entity instanceof TNTEntity ? entity.getY() : entity.getEyeY()) - this.y;
                    double d9 = entity.getZ() - this.z;
                    double d13 = (double)MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                    if (d13 != 0.0D) {
                        d5 = d5 / d13;
                        d7 = d7 / d13;
                        d9 = d9 / d13;
                        double d14 = (double)getSeenPercent(vector3d, entity);
                        double d10 = (1.0D - d12) * d14;
                        float damage = (float)((int)((d10 * d10 + d10) / 2.0D * 7.0D * (double)f2 + 1.0D));
                        double d11 = d10;
                        if (entity instanceof LivingEntity) {
                            LivingEntity livingEntity = (LivingEntity) entity;
                            d11 = ProtectionEnchantment.getExplosionKnockbackAfterDampener(livingEntity, d10);
                            if (MobUtil.immuneToFrost(livingEntity)){
                                damage = damage/2;
                            } else {
                                if (!livingEntity.hasEffect(Effects.MOVEMENT_SLOWDOWN)) {
                                    livingEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 300));
                                } else {
                                    if (this.random.nextFloat() <= 0.01F) {
                                        EffectsUtil.amplifyEffect(livingEntity, Effects.MOVEMENT_SLOWDOWN, 300);
                                    } else {
                                        EffectsUtil.resetDuration(livingEntity, Effects.MOVEMENT_SLOWDOWN, 300);
                                    }
                                }
                            }
                        }
                        entity.hurt(this.getDamageSource(), damage);
                        entity.setDeltaMovement(entity.getDeltaMovement().add(d5 * d11, d7 * d11, d9 * d11));
                        if (entity instanceof PlayerEntity) {
                            PlayerEntity playerentity = (PlayerEntity)entity;
                            if (!playerentity.isSpectator() && (!playerentity.isCreative() || !playerentity.abilities.flying)) {
                                this.hitPlayers.put(playerentity, new Vector3d(d5 * d10, d7 * d10, d9 * d10));
                            }
                        }
                    }
                }
            }
        }

    }

    public void iceExplosion() {
        for(BlockPos blockpos2 : this.toBlow) {
            if (this.random.nextInt(3) == 0 && this.level.getBlockState(blockpos2).isAir() && this.level.getBlockState(blockpos2.below()).isSolidRender(this.level, blockpos2.below())) {
                this.level.setBlockAndUpdate(blockpos2, Blocks.SNOW.defaultBlockState());
            }
            if (this.level.getBlockState(blockpos2).getFluidState().getType() == Fluids.WATER && !this.level.getFluidState(blockpos2).isEmpty() && this.level.getBlockState(blockpos2).getFluidState().isSource()){
                this.level.setBlockAndUpdate(blockpos2, Blocks.ICE.defaultBlockState());
            }
        }
        for (BlockPos blockPos3 : this.toPacked){
            if (this.level.getBlockState(blockPos3).getBlock() == Blocks.ICE){
                this.level.setBlockAndUpdate(blockPos3, Blocks.PACKED_ICE.defaultBlockState());
            }
        }
    }
}
