package com.Polarice3.Goety.common.entities.hostile.illagers;

import com.Polarice3.Goety.common.entities.projectiles.SoulSkullEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Map;

public class EnviokerEntity extends HuntingIllagerEntity {

    public EnviokerEntity(EntityType<? extends EnviokerEntity> p_i50207_1_, World p_i50207_2_) {
        super(p_i50207_1_, p_i50207_2_);
        this.xpReward = 20;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new CastingSpellGoal());
        this.goalSelector.addGoal(2, new AttackGoal(this));
        this.goalSelector.addGoal(4, new SummonSpellGoal());
        this.goalSelector.addGoal(5, new AttackSpellGoal());
        this.goalSelector.addGoal(6, new SoulSkullSpellGoal());
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
    }

    public boolean isMagic(){
        return this.getOffhandItem().getItem() == Items.TOTEM_OF_UNDYING;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld pLevel, DifficultyInstance pDifficulty, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
        ILivingEntityData ilivingentitydata = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        ((GroundPathNavigator)this.getNavigation()).setCanOpenDoors(true);
        this.populateDefaultEquipmentSlots(pDifficulty);
        this.populateDefaultEquipmentEnchantments(pDifficulty);
        return ilivingentitydata;
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance pDifficulty) {
        if (this.getCurrentRaid() == null) {
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
            this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.TOTEM_OF_UNDYING));
        }

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ArmPose getArmPose() {
        if (this.isCastingSpell() && this.isMagic()) {
            return ArmPose.SPELLCASTING;
        } else if (this.isAggressive() && !this.isMagic()) {
            return ArmPose.ATTACKING;
        } else {
            return this.isCelebrating() ? ArmPose.CELEBRATING : ArmPose.CROSSED;
        }
    }

    public boolean isAlliedTo(Entity pEntity) {
        if (pEntity == this) {
            return true;
        } else if (super.isAlliedTo(pEntity)) {
            return true;
        } else if (pEntity instanceof VexEntity) {
            return this.isAlliedTo(((VexEntity)pEntity).getOwner());
        } else if (pEntity instanceof LivingEntity && ((LivingEntity)pEntity).getMobType() == CreatureAttribute.ILLAGER) {
            return this.getTeam() == null && pEntity.getTeam() == null;
        } else {
            return false;
        }
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.EVOKER_HURT;
    }

    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    public void applyRaidBuffs(int pWave, boolean p_213660_2_) {
        ItemStack itemstack = new ItemStack(Items.IRON_SWORD);
        Raid raid = this.getCurrentRaid();
        int i = 1;
        if (pWave > raid.getNumGroups(Difficulty.NORMAL)) {
            i = 3;
        }

        boolean flag = this.random.nextFloat() <= raid.getEnchantOdds();
        if (flag) {
            Map<Enchantment, Integer> map = Maps.newHashMap();
            map.put(Enchantments.SHARPNESS, i);
            map.put(Enchantments.KNOCKBACK, i);
            EnchantmentHelper.setEnchantments(map, itemstack);
        }

        this.setItemSlot(EquipmentSlotType.MAINHAND, itemstack);
    }

    class AttackSpellGoal extends UseSpellGoal {
        private AttackSpellGoal() {
        }

        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else {
                return EnviokerEntity.this.isMagic();
            }
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 100;
        }

        protected void performSpellCasting() {
            LivingEntity livingentity = EnviokerEntity.this.getTarget();
            double d0 = Math.min(livingentity.getY(), EnviokerEntity.this.getY());
            double d1 = Math.max(livingentity.getY(), EnviokerEntity.this.getY()) + 1.0D;
            float f = (float) MathHelper.atan2(livingentity.getZ() - EnviokerEntity.this.getZ(), livingentity.getX() - EnviokerEntity.this.getX());
            if (EnviokerEntity.this.distanceToSqr(livingentity) < 9.0D) {
                for(int i = 0; i < 5; ++i) {
                    float f1 = f + (float)i * (float)Math.PI * 0.4F;
                    this.createSpellEntity(EnviokerEntity.this.getX() + (double)MathHelper.cos(f1) * 1.5D, EnviokerEntity.this.getZ() + (double)MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
                }

                for(int k = 0; k < 8; ++k) {
                    float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + 1.2566371F;
                    this.createSpellEntity(EnviokerEntity.this.getX() + (double)MathHelper.cos(f2) * 2.5D, EnviokerEntity.this.getZ() + (double)MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
                }

                for(int k = 0; k < 11; ++k) {
                    float f2 = f + (float)k * (float)Math.PI * 4.0F / 16.0F + 2.5133462F;
                    this.createSpellEntity(EnviokerEntity.this.getX() + (double)MathHelper.cos(f2) * 3.5D, EnviokerEntity.this.getZ() + (double)MathHelper.sin(f2) * 3.5D, d0, d1, f2, 6);
                }
            } else {
                for(int l = 0; l < 32; ++l) {
                    double d2 = 1.25D * (double)(l + 1);
                    float fleft = f + 0.4F;
                    float fright = f - 0.4F;
                    this.createSpellEntity(EnviokerEntity.this.getX() + (double)MathHelper.cos(f) * d2, EnviokerEntity.this.getZ() + (double)MathHelper.sin(f) * d2, d0, d1, f, l);
                    this.createSpellEntity(EnviokerEntity.this.getX() + (double)MathHelper.cos(fleft) * d2, EnviokerEntity.this.getZ() + (double)MathHelper.sin(fleft) * d2, d0, d1, f, l);
                    this.createSpellEntity(EnviokerEntity.this.getX() + (double)MathHelper.cos(fright) * d2, EnviokerEntity.this.getZ() + (double)MathHelper.sin(fright) * d2, d0, d1, f, l);
                }
            }

        }

        private void createSpellEntity(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
            BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
            boolean flag = false;
            double d0 = 0.0D;

            do {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate = EnviokerEntity.this.level.getBlockState(blockpos1);
                if (blockstate.isFaceSturdy(EnviokerEntity.this.level, blockpos1, Direction.UP)) {
                    if (!EnviokerEntity.this.level.isEmptyBlock(blockpos)) {
                        BlockState blockstate1 = EnviokerEntity.this.level.getBlockState(blockpos);
                        VoxelShape voxelshape = blockstate1.getCollisionShape(EnviokerEntity.this.level, blockpos);
                        if (!voxelshape.isEmpty()) {
                            d0 = voxelshape.max(Direction.Axis.Y);
                        }
                    }

                    flag = true;
                    break;
                }

                blockpos = blockpos.below();
            } while(blockpos.getY() >= MathHelper.floor(p_190876_5_) - 1);

            if (flag) {
                EnviokerEntity.this.level.addFreshEntity(new EvokerFangsEntity(EnviokerEntity.this.level, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, EnviokerEntity.this));
            }

        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        protected SpellType getSpell() {
            return SpellType.FANGS;
        }
    }

    class CastingSpellGoal extends CastingASpellGoal {
        private CastingSpellGoal() {
        }

        public void tick() {
            if (EnviokerEntity.this.getTarget() != null) {
                EnviokerEntity.this.getLookControl().setLookAt(EnviokerEntity.this.getTarget(), (float)EnviokerEntity.this.getMaxHeadYRot(), (float)EnviokerEntity.this.getMaxHeadXRot());
            }

        }
    }

    class SoulSkullSpellGoal extends UseSpellGoal {
        private SoulSkullSpellGoal() {
        }

        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else {
                return EnviokerEntity.this.isMagic();
            }
        }

        protected int getCastingTime() {
            return 5;
        }

        protected int getCastingInterval() {
            return 5;
        }

        protected void performSpellCasting() {
            LivingEntity livingentity = EnviokerEntity.this.getTarget();
            if (livingentity != null) {
                if (EnviokerEntity.this.getSensing().canSee(livingentity)) {
                    double d1 = livingentity.getX() - EnviokerEntity.this.getX();
                    double d2 = livingentity.getY(0.5D) - EnviokerEntity.this.getY(0.5D);
                    double d3 = livingentity.getZ() - EnviokerEntity.this.getZ();
                    SoulSkullEntity soulSkullEntity = new SoulSkullEntity(EnviokerEntity.this.level, EnviokerEntity.this, d1, d2, d3);
                    soulSkullEntity.setPos(soulSkullEntity.getX(), EnviokerEntity.this.getY(0.5), soulSkullEntity.getZ());
                    EnviokerEntity.this.level.addFreshEntity(soulSkullEntity);
                    if (!EnviokerEntity.this.isSilent()) {
                        EnviokerEntity.this.level.levelEvent(null, 1024, EnviokerEntity.this.blockPosition(), 0);
                    }
                }
            }

        }

        protected SoundEvent getSpellPrepareSound() {
            return null;
        }

        protected SpellType getSpell() {
            return SpellType.WOLOLO;
        }
    }

    class SummonSpellGoal extends UseSpellGoal {
        private final EntityPredicate vexCountTargeting = (new EntityPredicate()).range(16.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();

        private SummonSpellGoal() {
        }

        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else {
                int i = EnviokerEntity.this.level.getNearbyEntities(TormentorEntity.class, this.vexCountTargeting, EnviokerEntity.this, EnviokerEntity.this.getBoundingBox().inflate(16.0D)).size();
                return EnviokerEntity.this.random.nextInt(8) + 1 > i && EnviokerEntity.this.isMagic();
            }
        }

        protected int getCastingTime() {
            return 100;
        }

        protected int getCastingInterval() {
            return 340;
        }

        protected void performSpellCasting() {
            ServerWorld serverworld = (ServerWorld)EnviokerEntity.this.level;

            BlockPos blockpos = EnviokerEntity.this.blockPosition().offset(-2 + EnviokerEntity.this.random.nextInt(5), 1, -2 + EnviokerEntity.this.random.nextInt(5));
            TormentorEntity tormentorEntity = ModEntityType.TORMENTOR.get().create(EnviokerEntity.this.level);
            assert tormentorEntity != null;
            tormentorEntity.moveTo(blockpos, 0.0F, 0.0F);
            tormentorEntity.finalizeSpawn(serverworld, EnviokerEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
            tormentorEntity.setOwner(EnviokerEntity.this);
            tormentorEntity.setBoundOrigin(blockpos);
            tormentorEntity.setLimitedLife(20 * (30 + EnviokerEntity.this.random.nextInt(90)));
            serverworld.addFreshEntityWithPassengers(tormentorEntity);

        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        protected SpellType getSpell() {
            return SpellType.SUMMON_VEX;
        }
    }

    class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(EnviokerEntity p_i50577_2_) {
            super(p_i50577_2_, 1.0D, false);
        }

        @Override
        public boolean canUse() {
            return !EnviokerEntity.this.isMagic() && EnviokerEntity.this.getTarget() != null;
        }

        protected double getAttackReachSqr(LivingEntity pAttackTarget) {
            if (this.mob.getVehicle() instanceof RavagerEntity) {
                float f = this.mob.getVehicle().getBbWidth() - 0.1F;
                return (double)(f * 2.0F * f * 2.0F + pAttackTarget.getBbWidth());
            } else {
                return super.getAttackReachSqr(pAttackTarget);
            }
        }
    }

}
