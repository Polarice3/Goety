package com.Polarice3.Goety.common.entities.hostile;

import com.Polarice3.Goety.common.blocks.IDeadBlock;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.DeadSandExplosion;
import com.Polarice3.Goety.utils.LichdomUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

@OnlyIn(
        value = Dist.CLIENT,
        _interface = IChargeableMob.class
)
public class BoomerEntity extends MonsterEntity implements IChargeableMob, IDeadMob {
    private static final DataParameter<Integer> DATA_SWELL_DIR = EntityDataManager.defineId(BoomerEntity.class, DataSerializers.INT);
    private static final DataParameter<Boolean> DATA_IS_POWERED = EntityDataManager.defineId(BoomerEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_IS_IGNITED = EntityDataManager.defineId(BoomerEntity.class, DataSerializers.BOOLEAN);
    private int oldSwell;
    private int swell;
    private int maxSwell = 30;
    private int explosionRadius = 3;

    public BoomerEntity(EntityType<? extends BoomerEntity> p_i50213_1_, World p_i50213_2_) {
        super(p_i50213_1_, p_i50213_2_);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new BoomerSwellGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, OcelotEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, CatEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.25F)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    public int getMaxFallDistance() {
        return this.getTarget() == null ? 3 : 3 + (int)(this.getHealth() - 1.0F);
    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        boolean flag = super.causeFallDamage(p_225503_1_, p_225503_2_);
        this.swell = (int)((float)this.swell + p_225503_1_ * 1.5F);
        if (this.swell > this.maxSwell - 5) {
            this.swell = this.maxSwell - 5;
        }

        return flag;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SWELL_DIR, -1);
        this.entityData.define(DATA_IS_POWERED, false);
        this.entityData.define(DATA_IS_IGNITED, false);
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.entityData.get(DATA_IS_POWERED)) {
            pCompound.putBoolean("powered", true);
        }

        pCompound.putShort("Fuse", (short)this.maxSwell);
        pCompound.putByte("ExplosionRadius", (byte)this.explosionRadius);
        pCompound.putBoolean("ignited", this.isIgnited());
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.entityData.set(DATA_IS_POWERED, pCompound.getBoolean("powered"));
        if (pCompound.contains("Fuse", 99)) {
            this.maxSwell = pCompound.getShort("Fuse");
        }

        if (pCompound.contains("ExplosionRadius", 99)) {
            this.explosionRadius = pCompound.getByte("ExplosionRadius");
        }

        if (pCompound.getBoolean("ignited")) {
            this.ignite();
        }

    }

    public void tick() {
        if (this.isAlive()) {
            this.oldSwell = this.swell;
            if (this.isIgnited()) {
                this.setSwellDir(1);
            }

            int i = this.getSwellDir();
            if (i > 0 && this.swell == 0) {
                this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.15F);
            }

            this.swell += i;
            if (this.swell < 0) {
                this.swell = 0;
            }

            if (this.swell >= this.maxSwell) {
                this.swell = this.maxSwell;
                this.explodeCreeper();
            }

            List<BlockState> result = new ArrayList<>();
            Iterable<BlockPos> blocksToCheck = BlockPos.betweenClosed(
                    this.blockPosition().offset(-explosionRadius, -explosionRadius, -explosionRadius),
                    this.blockPosition().offset(explosionRadius, explosionRadius, explosionRadius));

            for (BlockPos blockToCheck : blocksToCheck) {
                BlockState blockState = this.level.getBlockState(blockToCheck);
                if (!(blockState.getBlock() instanceof IDeadBlock) && BlockFinder.NotDeadSandImmune(blockState)){
                    result.add(blockState);
                }
            }

            if (result.size() > explosionRadius * 2){
                this.ignite();
            }

            if (this.isInWater()){
                this.ignite();
            }

        }

        super.tick();
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.CREEPER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return null;
    }

    protected void playHurtSound(DamageSource pSource) {
        SoundEvent soundevent = this.getHurtSound(pSource);
        if (soundevent != null) {
            this.playSound(soundevent, 1.0F, 0.15F);
        }
    }

    public void die(DamageSource pCause) {
        SoundEvent soundevent = SoundEvents.CREEPER_DEATH;
        this.playSound(soundevent, 1.0F, 0.15F);
    }

    public boolean doHurtTarget(Entity pEntity) {
        return true;
    }

    public boolean isPowered() {
        return this.entityData.get(DATA_IS_POWERED);
    }

    public float getSwelling(float pPartialTicks) {
        return MathHelper.lerp(pPartialTicks, (float)this.oldSwell, (float)this.swell) / (float)(this.maxSwell - 2);
    }

    public int getSwellDir() {
        return this.entityData.get(DATA_SWELL_DIR);
    }

    public void setSwellDir(int pState) {
        this.entityData.set(DATA_SWELL_DIR, pState);
    }

    public void thunderHit(ServerWorld pLevel, LightningBoltEntity pLightning) {
        super.thunderHit(pLevel, pLightning);
        this.entityData.set(DATA_IS_POWERED, true);
    }

    protected ActionResultType mobInteract(PlayerEntity pPlayer, Hand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
            this.level.playSound(pPlayer, this.getX(), this.getY(), this.getZ(), SoundEvents.FLINTANDSTEEL_USE, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
            if (!this.level.isClientSide) {
                this.ignite();
                itemstack.hurtAndBreak(1, pPlayer, (p_213625_1_) -> {
                    p_213625_1_.broadcastBreakEvent(pHand);
                });
            }

            return ActionResultType.sidedSuccess(this.level.isClientSide);
        } else {
            return super.mobInteract(pPlayer, pHand);
        }
    }

    public DeadSandExplosion explode(World world, @Nullable Entity pExploder, double pX, double pY, double pZ, float pSize, DeadSandExplosion.Mode pBlockInteraction) {
        DeadSandExplosion explosion = new DeadSandExplosion(world, pExploder, pX, pY, pZ, pSize, pBlockInteraction);
        explosion.explode();
        explosion.finalizeExplosion(true);
        return explosion;
    }

    private void explodeCreeper() {
        if (!this.level.isClientSide) {
            float f = this.isPowered() ? 2.0F : 1.0F;
            float explosion = (int) (this.explosionRadius * f);
            this.dead = true;
            if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                this.explode(this.level, this, this.getX(), this.getY(), this.getZ(), explosion, DeadSandExplosion.Mode.SPREAD);
            } else {
                this.explode(this.level, this, this.getX(), this.getY(), this.getZ(), explosion, DeadSandExplosion.Mode.NONE);
            }
            this.remove();
            this.spawnLingeringCloud();
        }

    }

    private void spawnLingeringCloud() {
        Collection<EffectInstance> collection = this.getActiveEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.level, this.getX(), this.getY(), this.getZ());
            areaeffectcloudentity.setRadius(2.5F);
            areaeffectcloudentity.setRadiusOnUse(-0.5F);
            areaeffectcloudentity.setWaitTime(10);
            areaeffectcloudentity.setDuration(areaeffectcloudentity.getDuration() / 2);
            areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float)areaeffectcloudentity.getDuration());

            for(EffectInstance effectinstance : collection) {
                areaeffectcloudentity.addEffect(new EffectInstance(effectinstance));
            }

            this.level.addFreshEntity(areaeffectcloudentity);
        }

    }

    public boolean isIgnited() {
        return this.entityData.get(DATA_IS_IGNITED);
    }

    public void ignite() {
        this.entityData.set(DATA_IS_IGNITED, true);
    }

    static class BoomerSwellGoal extends Goal {
        private final BoomerEntity creeper;
        private LivingEntity target;

        public BoomerSwellGoal(BoomerEntity p_i1655_1_) {
            this.creeper = p_i1655_1_;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.creeper.getTarget();
            return this.creeper.getSwellDir() > 0 || livingentity != null && this.creeper.distanceToSqr(livingentity) < 9.0D;
        }

        public void start() {
            this.creeper.getNavigation().stop();
            this.target = this.creeper.getTarget();
        }

        public void stop() {
            this.target = null;
        }

        public void tick() {
            if (this.target == null) {
                this.creeper.setSwellDir(-1);
            } else if (this.creeper.distanceToSqr(this.target) > 49.0D) {
                this.creeper.setSwellDir(-1);
            } else if (!this.creeper.getSensing().canSee(this.target)) {
                this.creeper.setSwellDir(-1);
            } else {
                this.creeper.setSwellDir(1);
            }
        }
    }
}
