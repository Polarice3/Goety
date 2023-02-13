package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.blocks.DeadPileBlock;
import com.Polarice3.Goety.common.blocks.DeadTNTBlock;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModSounds;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TNTBlock;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class DeadSandExplosion {
    private final World level;
    private final DeadSandExplosion.Mode blockInteraction;
    private final double x;
    private final double y;
    private final double z;
    @Nullable
    private final Entity source;
    private final float radius;
    private final List<BlockPos> toBlow = Lists.newArrayList();
    private final Vector3d position;

    public DeadSandExplosion(World pLevel, @Nullable Entity pSource, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, List<BlockPos> pPositions) {
        this(pLevel, pSource, pToBlowX, pToBlowY, pToBlowZ, pRadius, DeadSandExplosion.Mode.SPREAD, pPositions);
    }

    public DeadSandExplosion(World pLevel, @Nullable Entity pSource, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, DeadSandExplosion.Mode pBlockInteraction, List<BlockPos> pPositions) {
        this(pLevel, pSource, pToBlowX, pToBlowY, pToBlowZ, pRadius, pBlockInteraction);
        this.toBlow.addAll(pPositions);
    }

    public DeadSandExplosion(World pLevel, @Nullable Entity pSource, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, DeadSandExplosion.Mode pBlockInteraction) {
        this.level = pLevel;
        this.source = pSource;
        this.radius = pRadius;
        this.x = pToBlowX;
        this.y = pToBlowY;
        this.z = pToBlowZ;
        this.blockInteraction = pBlockInteraction;
        this.position = new Vector3d(this.x, this.y, this.z);
    }

    public static float getSeenPercent(Vector3d pExplosionVector, Entity pEntity) {
        AxisAlignedBB axisalignedbb = pEntity.getBoundingBox();
        double d0 = 1.0D / ((axisalignedbb.maxX - axisalignedbb.minX) * 2.0D + 1.0D);
        double d1 = 1.0D / ((axisalignedbb.maxY - axisalignedbb.minY) * 2.0D + 1.0D);
        double d2 = 1.0D / ((axisalignedbb.maxZ - axisalignedbb.minZ) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
        if (!(d0 < 0.0D) && !(d1 < 0.0D) && !(d2 < 0.0D)) {
            int i = 0;
            int j = 0;

            for(float f = 0.0F; f <= 1.0F; f = (float)((double)f + d0)) {
                for(float f1 = 0.0F; f1 <= 1.0F; f1 = (float)((double)f1 + d1)) {
                    for(float f2 = 0.0F; f2 <= 1.0F; f2 = (float)((double)f2 + d2)) {
                        double d5 = MathHelper.lerp((double)f, axisalignedbb.minX, axisalignedbb.maxX);
                        double d6 = MathHelper.lerp((double)f1, axisalignedbb.minY, axisalignedbb.maxY);
                        double d7 = MathHelper.lerp((double)f2, axisalignedbb.minZ, axisalignedbb.maxZ);
                        Vector3d vector3d = new Vector3d(d5 + d3, d6, d7 + d4);
                        if (pEntity.level.clip(new RayTraceContext(vector3d, pExplosionVector, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, pEntity)).getType() == RayTraceResult.Type.MISS) {
                            ++i;
                        }

                        ++j;
                    }
                }
            }

            return (float)i / (float)j;
        } else {
            return 0.0F;
        }
    }

    public void explode() {
        Set<BlockPos> set = Sets.newHashSet();
        int i = 16;

        for(int j = 0; j < 16; ++j) {
            for(int k = 0; k < 16; ++k) {
                for(int l = 0; l < 16; ++l) {
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

                            if (fluidstate.isEmpty()) {
                                set.add(blockpos);
                            }

                            if (blockstate.getBlock().is(ModBlocks.DEAD_TNT.get())){
                                set.add(blockpos);
                            }

                            if (blockstate.getBlock() instanceof TNTBlock){
                                set.add(blockpos);
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
        Vector3d vector3d = new Vector3d(this.x, this.y, this.z);

        for(int k2 = 0; k2 < list.size(); ++k2) {
            Entity entity = list.get(k2);
            double d12 = (double)(MathHelper.sqrt(entity.distanceToSqr(vector3d)) / f2);
            if (d12 <= 1.0D) {
                double d5 = entity.getX() - this.x;
                double d7 = entity.getEyeY() - this.y;
                double d9 = entity.getZ() - this.z;
                double d13 = (double)MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                if (d13 != 0.0D) {
                    d5 = d5 / d13;
                    d7 = d7 / d13;
                    d9 = d9 / d13;
                    double d14 = (double)getSeenPercent(vector3d, entity);
                    double d10 = (1.0D - d12) * d14;
                    entity.hurt(ModDamageSource.DESICCATE, (float)((int)((d10 * d10 + d10) / 2.0D * 5.0D * (double)f2 + 1.0D)));
                    double d11 = d10;
                    if (entity instanceof LivingEntity) {
                        d11 = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity)entity, d10);
                    }

                    entity.setDeltaMovement(entity.getDeltaMovement().add(d5 * d11, d7 * d11, d9 * d11));
                    if (entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) entity;
                        if (livingEntity.hasEffect(ModEffects.DESICCATE.get()) && this.level.random.nextFloat() <= 0.25F){
                            EffectsUtil.amplifyEffect(livingEntity, ModEffects.DESICCATE.get(), 1200);
                        } else {
                            if (livingEntity.hasEffect(ModEffects.DESICCATE.get())){
                                int d2 = Objects.requireNonNull(livingEntity.getEffect(ModEffects.DESICCATE.get())).getDuration();
                                EffectsUtil.resetDuration(livingEntity, ModEffects.DESICCATE.get(), Math.max(d2, 1200));
                            } else {
                                livingEntity.addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 1200));
                            }
                        }
                    }
                }
            }
        }

    }

    public void finalizeExplosion(boolean pSpawnParticles) {

        boolean flag = this.blockInteraction != DeadSandExplosion.Mode.NONE;

        if (this.level.isClientSide) {
            this.soundAndParticles(pSpawnParticles);
        }

        if (!this.level.isClientSide){
            ServerWorld serverWorld = (ServerWorld) this.level;
            serverWorld.playSound(null, position.x, position.y, position.z, ModSounds.CORRUPT_EXPLOSION.get(), SoundCategory.BLOCKS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F);
            if (pSpawnParticles) {
                if (!(this.radius < 2.0F)) {
                    serverWorld.sendParticles(ModParticleTypes.DEAD_SAND_EXPLOSION_EMITTER.get(), this.x, this.y, this.z, 0, 1.0D, 0.0D, 0.0D, 0.0F);
                } else {
                    serverWorld.sendParticles(ModParticleTypes.DEAD_SAND_EXPLOSION.get(), this.x, this.y, this.z, 0, 1.0D, 0.0D, 0.0D, 0.0F);
                }
            }
        }

        if (flag) {
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();
            Collections.shuffle(this.toBlow, this.level.random);
            BlockPos pos = new BlockPos(this.position);
            FluidState fluidstate = this.level.getFluidState(pos);

            for (BlockPos blockpos : this.toBlow) {
                BlockFinder.DeadSandReplaceLagFree(blockpos, this.level);

                BlockState blockstate = this.level.getBlockState(blockpos);
                if (this.level.random.nextInt(3) == 0) {
                    if (this.level.getBlockState(blockpos.above()).isAir()
                            && blockstate.isSolidRender(this.level, blockpos)) {
                        this.level.setBlockAndUpdate(blockpos.above(), ModBlocks.DEAD_PILE.get().defaultBlockState());
                    }
                    if (blockstate.getBlock() == ModBlocks.DEAD_PILE.get()
                        && blockstate.getValue(DeadPileBlock.LAYERS) < 8){
                        int i = this.level.getBlockState(blockpos).getValue(DeadPileBlock.LAYERS);
                        this.level.setBlockAndUpdate(blockpos, blockstate.setValue(DeadPileBlock.LAYERS, i + 1));
                    }
                }
                if (blockstate.getBlock().is(ModBlocks.DEAD_TNT.get())){
                    DeadTNTBlock.wasDeadExploded(this.level, blockpos, this);
                }
                if (blockstate.getBlock() instanceof TNTBlock){
                    DeadTNTBlock.wasDeadExploded(this.level, blockpos, this);
                }
            }

            for (Pair<ItemStack, BlockPos> pair : objectarraylist) {
                Block.popResource(this.level, pair.getSecond(), pair.getFirst());
            }

            if (!fluidstate.isEmpty()) {
                AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.level, pos.getX(), pos.getY(), pos.getZ());
                areaeffectcloudentity.setRadius(2.5F);
                areaeffectcloudentity.setRadiusOnUse(-0.5F);
                areaeffectcloudentity.setWaitTime(10);
                areaeffectcloudentity.setDuration(areaeffectcloudentity.getDuration() / 2);
                areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float) areaeffectcloudentity.getDuration());
                areaeffectcloudentity.addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 1200));
                this.level.addFreshEntity(areaeffectcloudentity);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void soundAndParticles(boolean pSpawnParticles){
        this.level.playLocalSound(position.x, position.y, position.z, ModSounds.CORRUPT_EXPLOSION.get(), SoundCategory.BLOCKS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F, false);
        if (pSpawnParticles) {
            if (!(this.radius < 2.0F)) {
                this.level.addParticle(ModParticleTypes.DEAD_SAND_EXPLOSION_EMITTER.get(), this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
            } else {
                this.level.addParticle(ModParticleTypes.DEAD_SAND_EXPLOSION.get(), this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
            }
        }
    }

    @Nullable
    public LivingEntity getSourceMob() {
        if (this.source == null) {
            return null;
        } else if (this.source instanceof LivingEntity) {
            return (LivingEntity)this.source;
        } else {
            if (this.source instanceof ProjectileEntity) {
                Entity entity = ((ProjectileEntity)this.source).getOwner();
                if (entity instanceof LivingEntity) {
                    return (LivingEntity)entity;
                }
            }

            return null;
        }
    }

    public void clearToBlow() {
        this.toBlow.clear();
    }

    public List<BlockPos> getToBlow() {
        return this.toBlow;
    }

    public Vector3d getPosition() {
        return this.position;
    }

    @Nullable
    public Entity getExploder() {
        return this.source;
    }

    public static enum Mode {
        NONE,
        SPREAD;
    }

}
