package com.Polarice3.Goety.common.entities.hostile.cultists;

import com.Polarice3.Goety.common.entities.utilities.FireBlastTrapEntity;
import com.Polarice3.Goety.init.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ReturnedEntity extends SpellcastingCultistEntity implements ICultist{

    public ReturnedEntity(EntityType<? extends ReturnedEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
        this.setPathfindingMalus(PathNodeType.LAVA, 0.0F);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(PathNodeType.DAMAGE_FIRE, 0.0F);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new CastingSpellGoal());
        this.goalSelector.addGoal(4, new ProjectileSpellGoal());
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    public boolean canStandOnFluid(Fluid p_230285_1_) {
        return p_230285_1_.is(FluidTags.LAVA);
    }

    @OnlyIn(Dist.CLIENT)
    public AbstractCultistEntity.ArmPose getArmPose() {
        if (this.isSpellcasting()) {
            return ArmPose.SPELLCASTING;
        } else {
            return ArmPose.CROSSED;
        }
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITCH_AMBIENT;
    }

    public void playAmbientSound() {
        super.playAmbientSound();
        this.playSound(SoundEvents.WITHER_SKELETON_AMBIENT, this.getSoundVolume(), 1.0F);
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.WITCH_HURT;
    }

    protected void playHurtSound(DamageSource pSource) {
        super.playHurtSound(pSource);
        this.playSound(SoundEvents.WITHER_SKELETON_HURT, this.getSoundVolume(), 1.0F);
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.WITCH_DEATH;
    }

    protected SoundEvent getStepSound() {
        return null;
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
    }

    protected float getVoicePitch() {
        return 0.25F;
    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    public boolean isSensitiveToWater() {
        return true;
    }

    public boolean isOnFire() {
        return this.isSpellcasting();
    }

    public void die(DamageSource pCause) {
        this.setInvisible(true);
        this.playSound(SoundEvents.WITHER_SKELETON_DEATH, this.getSoundVolume(), 1.0F);
        if (!this.level.isClientSide){
            ServerWorld serverWorld = (ServerWorld) this.level;
            float f = 1.5F;
            float f5 = (float) Math.PI * f * f;
            for (int j1 = 0; j1 < 16; ++j1) {
                for (int k1 = 0; (float) k1 < f5; ++k1) {
                    float f6 = this.random.nextFloat() * ((float) Math.PI * 2F);
                    float f7 = MathHelper.sqrt(this.random.nextFloat()) * f;
                    float f8 = MathHelper.cos(f6) * f7;
                    float f9 = MathHelper.sin(f6) * f7;
                    serverWorld.sendParticles(ParticleTypes.FLAME, this.getX() + (double) f8, this.getY(), this.getZ() + (double) f9, 0, 0, 0.5D, 0, 0.5F);
                }
            }
        }
        super.die(pCause);
    }

    protected float getDamageAfterMagicAbsorb(DamageSource source, float damage) {
        damage = super.getDamageAfterMagicAbsorb(source, damage);
        if (source.getEntity() == this) {
            damage = 0.0F;
        }

        if (this.level.getDifficulty() == Difficulty.HARD){
            if (source.isMagic()){
                damage = (float)((double)damage * 0.15D);
            }
        }

        return damage;
    }

    public void aiStep() {
        super.aiStep();
        Vector3d vector3d = this.getDeltaMovement();
        if (!this.onGround && vector3d.y < 0.0D) {
            this.setDeltaMovement(vector3d.multiply(1.0D, 0.6D, 1.0D));
        }
        if (!this.level.isClientSide) {
            ServerWorld serverWorld = (ServerWorld) this.level;
            if (this.isAlive()) {
                serverWorld.sendParticles(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.5D), this.getY() + 0.5D, this.getRandomZ(0.5D), 1, 0, 0, 0, 0);
            }
        }
    }

    protected void teleport() {
        if (!this.level.isClientSide() && this.isAlive()) {
            for(int i = 0; i < 128; ++i) {
                double d3 = this.getX() + (this.getRandom().nextDouble() - 0.5D) * 32.0D;
                double d5 = this.getZ() + (this.getRandom().nextDouble() - 0.5D) * 32.0D;
                BlockPos blockPos = new BlockPos(d3, this.getY(), d5);
                double d4 = this.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockPos).getY();
                if (this.randomTeleport(d3, d4, d5, false)) {
                    this.teleportHits();
                    break;
                }
            }
        }
    }

    public void teleportHits(){
        this.level.broadcastEntityEvent(this, (byte) 100);
        if (!this.isSilent()) {
            this.level.playSound((PlayerEntity)null, this.xo, this.yo, this.zo, ModSounds.APOSTLE_TELEPORT.get(), this.getSoundSource(), 1.0F, 1.0F);
            this.playSound(ModSounds.APOSTLE_TELEPORT.get(), 1.0F, 1.0F);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte pId) {
        if (pId == 100){
            int i = 128;

            for(int j = 0; j < i; ++j) {
                double d0 = (double)j / (i - 1);
                float f = (this.random.nextFloat() - 0.5F) * 0.2F;
                float f1 = (this.random.nextFloat() - 0.5F) * 0.2F;
                float f2 = (this.random.nextFloat() - 0.5F) * 0.2F;
                double d1 = MathHelper.lerp(d0, this.xo, this.getX()) + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth() * 2.0D;
                double d2 = MathHelper.lerp(d0, this.yo, this.getY()) + this.random.nextDouble() * (double)this.getBbHeight();
                double d3 = MathHelper.lerp(d0, this.zo, this.getZ()) + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth() * 2.0D;
                this.level.addParticle(ParticleTypes.FLAME, d1, d2, d3, (double)f, (double)f1, (double)f2);
            }
        } else {
            super.handleEntityEvent(pId);
        }
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.BLAZE_SHOOT;
    }

    class CastingSpellGoal extends SpellcastingCultistEntity.CastingASpellGoal {
        private CastingSpellGoal() {
        }

        public void tick() {
            if (ReturnedEntity.this.getTarget() != null) {
                double d1 = ReturnedEntity.this.getTarget().getX() - ReturnedEntity.this.getTarget().getX();
                double d2 = ReturnedEntity.this.getTarget().getZ() - ReturnedEntity.this.getTarget().getZ();
                ReturnedEntity.this.getLookControl().setLookAt(ReturnedEntity.this.getTarget(), 30.0F, 30.0F);
                ReturnedEntity.this.yRot = -((float)MathHelper.atan2(d1, d2)) * (180F / (float)Math.PI);
                ReturnedEntity.this.yBodyRot = ReturnedEntity.this.yRot;
            }

        }
    }

    class ProjectileSpellGoal extends SpellcastingCultistEntity.UseSpellGoal {
        public ReturnedEntity returnedEntity;

        private ProjectileSpellGoal() {
            this.returnedEntity = ReturnedEntity.this;
        }

        public boolean canUse() {
            return this.returnedEntity.getTarget() != null
                    && this.returnedEntity.getSensing().canSee(this.returnedEntity.getTarget())
                    && super.canUse();
        }

        protected int getCastingTime() {
            return 20;
        }

        protected int getCastingInterval() {
            return 60;
        }

        protected SoundEvent getSpellPrepareSound() {
            return ModSounds.EVIL_LAUGH.get();
        }

        @Override
        protected void castSpell() {
            if (this.returnedEntity.getTarget() != null) {
                double d = this.returnedEntity.distanceToSqr(this.returnedEntity.getTarget());
                float f = MathHelper.sqrt(MathHelper.sqrt(d)) * 0.5F;
                double d0 = Math.min(this.returnedEntity.getTarget().getY(), this.returnedEntity.getY());
                double d1 = Math.max(this.returnedEntity.getTarget().getY(), this.returnedEntity.getY()) + 1.0D;
                spawnBlast(this.returnedEntity, this.returnedEntity.getTarget().getX(), this.returnedEntity.getTarget().getZ(), d0, d1);
                for(int i = 0; i < 5; ++i) {
                    float f1 = f + (float)i * (float)Math.PI * 0.4F;
                    spawnBlast(this.returnedEntity, this.returnedEntity.getTarget().getX() + (double)MathHelper.cos(f1) * 1.5D, this.returnedEntity.getTarget().getZ() + (double)MathHelper.sin(f1) * 1.5D, d0, d1);
                }
                this.returnedEntity.teleport();
            }
        }

        @Override
        protected SpellType getSpellType() {
            return SpellType.FIRE;
        }

        public void spawnBlast(LivingEntity livingEntity, double pPosX, double pPosZ, double PPPosY, double pOPosY) {
            BlockPos blockpos = new BlockPos(pPosX, pOPosY, pPosZ);
            boolean flag = false;
            double d0 = 0.0D;

            do {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate = livingEntity.level.getBlockState(blockpos1);
                if (blockstate.isFaceSturdy(livingEntity.level, blockpos1, Direction.UP)) {
                    if (!livingEntity.level.isEmptyBlock(blockpos)) {
                        BlockState blockstate1 = livingEntity.level.getBlockState(blockpos);
                        VoxelShape voxelshape = blockstate1.getCollisionShape(livingEntity.level, blockpos);
                        if (!voxelshape.isEmpty()) {
                            d0 = voxelshape.max(Direction.Axis.Y);
                        }
                    }

                    flag = true;
                    break;
                }

                blockpos = blockpos.below();
            } while(blockpos.getY() >= MathHelper.floor(PPPosY) - 1);

            if (flag) {
                FireBlastTrapEntity fireBlastTrap = new FireBlastTrapEntity(livingEntity.level, pPosX, (double)blockpos.getY() + d0, pPosZ);
                fireBlastTrap.setOwner(livingEntity);
                livingEntity.level.addFreshEntity(fireBlastTrap);
            }

        }
    }

}
