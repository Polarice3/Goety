package com.Polarice3.Goety.common.entities.hostile.cultists;

import com.Polarice3.Goety.MobConfig;
import com.Polarice3.Goety.common.entities.neutral.ZPiglinBruteMinionEntity;
import com.Polarice3.Goety.common.entities.utilities.MagicBlastTrapEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Predicate;

public class DiscipleEntity extends SpellcastingCultistEntity implements ICultist {
    private int f;
    private int cooldown;
    private int spellcycle;
    private final Predicate<Entity> field_213690_b = Entity::isAlive;
    private boolean roarParticles;

    public DiscipleEntity(EntityType<? extends SpellcastingCultistEntity> type, World worldIn) {
        super(type, worldIn);
        this.f = 0;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, IronGolemEntity.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(3, new CastingSpellGoal());
        this.goalSelector.addGoal(3, new FireballSpellGoal());
        this.goalSelector.addGoal(3, new ZombieSpellGoal());
        this.goalSelector.addGoal(3, new SkeletonSpellGoal());
        this.goalSelector.addGoal(3, new RoarSpellGoal());
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, MobConfig.DiscipleHealth.get())
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D);
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.DISCIPLE_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.DISCIPLE_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.DISCIPLE_DEATH.get();
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return ModSounds.DISCIPLE_CELEBRATE.get();
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("firing", this.f);
        pCompound.putInt("cooldown", this.cooldown);
        pCompound.putInt("spellcycle", this.spellcycle);
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.f = pCompound.getInt("firing");
        this.cooldown = pCompound.getInt("cooldown");
        this.spellcycle = pCompound.getInt("spellcycle");
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        if (worldIn.getRandom().nextInt(100) == 0) {
            CrimsonSpiderEntity spider = new CrimsonSpiderEntity(ModEntityType.CRIMSON_SPIDER.get(), level);
            if (this.isPersistenceRequired()){
                spider.setPersistenceRequired();
            }
            spider.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0F);
            spider.finalizeSpawn(worldIn, difficultyIn, SpawnReason.JOCKEY, null, null);
            this.startRiding(spider);
            worldIn.addFreshEntity(spider);
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.NETHER_BOOK.get()));
        this.setDropChance(EquipmentSlotType.MAINHAND, 0.085F);
        this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.CULTISTHELM.get()));
        this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(ModItems.CULTISTROBE.get()));
        this.setDropChance(EquipmentSlotType.HEAD, 0.0F);
        this.setDropChance(EquipmentSlotType.CHEST, 0.0F);
        boolean flag = true;

        for(EquipmentSlotType equipmentslottype : EquipmentSlotType.values()) {
            if (equipmentslottype.getType() == EquipmentSlotType.Group.ARMOR && equipmentslottype != EquipmentSlotType.HEAD && equipmentslottype != EquipmentSlotType.CHEST) {
                ItemStack itemstack = this.getItemBySlot(equipmentslottype);
                if (!flag && this.random.nextFloat() < 0.25F) {
                    break;
                }

                flag = false;
                if (itemstack.isEmpty()) {
                    int i = this.random.nextInt(8);
                    if (i == 4){
                        --i;
                    }
                    Item item = getEquipmentForSlot(equipmentslottype, i);
                    if (item != null) {
                        this.setItemSlot(equipmentslottype, new ItemStack(item));
                        this.setDropChance(equipmentslottype, 0.025F);
                    }
                }
            }
        }
    }

    protected float getDamageAfterMagicAbsorb(DamageSource pSource, float pDamage) {
        pDamage = super.getDamageAfterMagicAbsorb(pSource, pDamage);
        if (pSource.getEntity() == this) {
            pDamage = 0.0F;
        }

        if (pSource.isMagic()) {
            pDamage = (float)((double)pDamage * 0.15D);
        }

        return pDamage;
    }

    public boolean isFiring(){
        return this.roarParticles;
    }

    public void setFiring(boolean firing){
        this.roarParticles = firing;
    }

    @Override
    protected SoundEvent getCastingSoundEvent () {
        return ModSounds.CULTIST_CAST_SPELL.get();
    }

    @OnlyIn(Dist.CLIENT)
    public AbstractCultistEntity.ArmPose getArmPose() {
        if (this.isSpellcasting()) {
            return ArmPose.BOW_AND_ARROW;
        } else {
            return AbstractCultistEntity.ArmPose.NEUTRAL;
        }
    }

    public void aiStep() {
        super.aiStep();
        if (this.isFiring()) {
            ++this.f;
            if (this.f % 2 == 0 && this.f < 10) {
                for (Entity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D), field_213690_b)) {
                    if (!(entity instanceof AbstractCultistEntity)) {
                        entity.hurt(DamageSource.mobAttack(this), 2.0F);
                        this.launch(entity, this);
                    }
                }
                Vector3d vector3d = this.getBoundingBox().getCenter();
                if (!this.level.isClientSide){
                    ServerWorld serverWorld = (ServerWorld) this.level;
                    for (int i = 0; i < 40; ++i) {
                        double d0 = this.random.nextGaussian() * 0.2D;
                        double d1 = this.random.nextGaussian() * 0.2D;
                        double d2 = this.random.nextGaussian() * 0.2D;
                        serverWorld.sendParticles(ParticleTypes.POOF, vector3d.x, vector3d.y, vector3d.z, 0, d0, d1, d2, 0.5F);
                    }
                }
            }
            if (this.f >= 10){
                this.setFiring(false);
                this.f = 0;
            }
        }
        if (this.cooldown < 50) {
            ++this.cooldown;
        } else {
            this.spellcycle = 0;
        }
        if (this.spellcycle == 1){
            if (this.tickCount % 20 == 0) {
                if (this.level.random.nextBoolean()) {
                    this.spellcycle = 2;
                } else {
                    this.spellcycle = 3;
                }
            }
        }
    }

    private void launch(Entity p_213688_1_, LivingEntity livingEntity) {
        double d0 = p_213688_1_.getX() - livingEntity.getX();
        double d1 = p_213688_1_.getZ() - livingEntity.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        MobUtil.push(p_213688_1_, d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
    }

    class CastingSpellGoal extends SpellcastingCultistEntity.CastingASpellGoal {
        private CastingSpellGoal() {
        }

        public void tick() {
            if (DiscipleEntity.this.getTarget() != null) {
                DiscipleEntity.this.getLookControl().setLookAt(DiscipleEntity.this.getTarget(), (float) DiscipleEntity.this.getMaxHeadYRot(), (float) DiscipleEntity.this.getMaxHeadXRot());
            }
        }
    }

    class FireballSpellGoal extends SpellcastingCultistEntity.UseSpellGoal {
        private FireballSpellGoal() {
        }

        @Override
        public boolean canUse() {
            return super.canUse()
                    && DiscipleEntity.this.spellcycle == 0
                    && DiscipleEntity.this.getTarget() != null
                    && DiscipleEntity.this.canSee(DiscipleEntity.this.getTarget());
        }

        protected int getCastingTime() {
            return 20;
        }

        protected int getCastingInterval() {
            return 60;
        }

        public void castSpell() {
            LivingEntity livingentity = DiscipleEntity.this.getTarget();
            if (livingentity != null) {
                double d = DiscipleEntity.this.distanceToSqr(livingentity);
                float f = MathHelper.sqrt(MathHelper.sqrt(d)) * 0.5F;
                if (!livingentity.fireImmune() && !livingentity.hasEffect(Effects.FIRE_RESISTANCE)) {
                    double d1 = livingentity.getX() - DiscipleEntity.this.getX();
                    double d2 = livingentity.getY(0.5D) - DiscipleEntity.this.getY(0.5D);
                    double d3 = livingentity.getZ() - DiscipleEntity.this.getZ();
                    for (int i = 0; i < 3; ++i) {
                        SmallFireballEntity smallfireballentity = new SmallFireballEntity(DiscipleEntity.this.level, DiscipleEntity.this, d1 + DiscipleEntity.this.getRandom().nextGaussian() * (double) f, d2, d3 + DiscipleEntity.this.getRandom().nextGaussian() * (double) f);
                        smallfireballentity.setPos(smallfireballentity.getX(), DiscipleEntity.this.getY(0.5), smallfireballentity.getZ());
                        DiscipleEntity.this.level.addFreshEntity(smallfireballentity);
                    }
                    if (!DiscipleEntity.this.isSilent()) {
                        DiscipleEntity.this.level.levelEvent(null, 1016, DiscipleEntity.this.blockPosition(), 0);
                    }
                } else {
                    double d0 = Math.min(DiscipleEntity.this.getTarget().getY(), DiscipleEntity.this.getY());
                    double d1 = Math.max(DiscipleEntity.this.getTarget().getY(), DiscipleEntity.this.getY()) + 1.0D;
                    spawnBlast(DiscipleEntity.this, livingentity.getX(), livingentity.getZ(), d0, d1);
                    for(int i = 0; i < 5; ++i) {
                        float f1 = f + (float)i * (float)Math.PI * 0.4F;
                        spawnBlast(DiscipleEntity.this, DiscipleEntity.this.getTarget().getX() + (double)MathHelper.cos(f1) * 1.5D, DiscipleEntity.this.getTarget().getZ() + (double)MathHelper.sin(f1) * 1.5D, d0, d1);
                    }
                }
                DiscipleEntity.this.cooldown = 0;
                ++DiscipleEntity.this.spellcycle;
            }
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
                MagicBlastTrapEntity fireBlastTrap = new MagicBlastTrapEntity(livingEntity.level, pPosX, (double)blockpos.getY() + d0, pPosZ);
                fireBlastTrap.setOwner(livingEntity);
                livingEntity.level.addFreshEntity(fireBlastTrap);
            }

        }

        protected SoundEvent getSpellPrepareSound() {
            return ModSounds.CULTIST_PREPARE_SPELL.get();
        }

        @Override
        protected SpellcastingCultistEntity.SpellType getSpellType() {
            return SpellType.FIRE;
        }
    }

    class ZombieSpellGoal extends SpellcastingCultistEntity.UseSpellGoal {
        private ZombieSpellGoal() {
        }

        @Override
        public boolean canUse() {
            return super.canUse()
                    && DiscipleEntity.this.cooldown >= 45
                    && DiscipleEntity.this.spellcycle == 2
                    && DiscipleEntity.this.getTarget() != null
                    && DiscipleEntity.this.canSee(DiscipleEntity.this.getTarget());
        }

        protected int getCastingTime() {
            return 20;
        }

        protected int getCastingInterval() {
            return 0;
        }

        public void castSpell() {
            LivingEntity livingentity = DiscipleEntity.this.getTarget();
            if (livingentity != null) {
                BlockPos blockpos = DiscipleEntity.this.blockPosition();
                ZombieVillagerMinionEntity summonedentity = new ZombieVillagerMinionEntity(ModEntityType.ZOMBIE_VILLAGER_MINION.get(), DiscipleEntity.this.level);
                summonedentity.moveTo(blockpos, 0.0F, 0.0F);
                summonedentity.setOwnerId(DiscipleEntity.this.getUUID());
                summonedentity.setLimitedLife(60 * (90 + DiscipleEntity.this.level.random.nextInt(180)));
                summonedentity.finalizeSpawn((IServerWorld) DiscipleEntity.this.level, DiscipleEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                summonedentity.setTarget(livingentity);
                DiscipleEntity.this.level.addFreshEntity(summonedentity);
                DiscipleEntity.this.cooldown = 0;
                DiscipleEntity.this.spellcycle = 0;
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return ModSounds.CULTIST_PREPARE_SPELL.get();
        }

        @Override
        protected SpellcastingCultistEntity.SpellType getSpellType() {
            return SpellType.ZOMBIE;
        }
    }

    class SkeletonSpellGoal extends SpellcastingCultistEntity.UseSpellGoal {
        private SkeletonSpellGoal() {
        }

        @Override
        public boolean canUse() {
            return super.canUse()
                    && DiscipleEntity.this.cooldown >= 45
                    && DiscipleEntity.this.spellcycle == 3
                    && DiscipleEntity.this.getTarget() != null
                    && DiscipleEntity.this.canSee(DiscipleEntity.this.getTarget());
        }

        protected int getCastingTime() {
            return 60;
        }

        protected int getCastingInterval() {
            return 0;
        }

        public void castSpell() {
            LivingEntity livingentity = DiscipleEntity.this.getTarget();
            if (livingentity != null) {
                BlockPos blockpos = DiscipleEntity.this.blockPosition();
                SkeletonVillagerMinionEntity summonedentity = new SkeletonVillagerMinionEntity(ModEntityType.SKELETON_VILLAGER_MINION.get(), DiscipleEntity.this.level);
                summonedentity.moveTo(blockpos, 0.0F, 0.0F);
                summonedentity.setOwnerId(DiscipleEntity.this.getUUID());
                summonedentity.setLimitedLife(60 * (90 + DiscipleEntity.this.level.random.nextInt(180)));
                summonedentity.finalizeSpawn((IServerWorld) DiscipleEntity.this.level, DiscipleEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                summonedentity.setTarget(livingentity);
                DiscipleEntity.this.level.addFreshEntity(summonedentity);
                DiscipleEntity.this.cooldown = 0;
                DiscipleEntity.this.spellcycle = 0;
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return ModSounds.CULTIST_PREPARE_SPELL.get();
        }

        @Override
        protected SpellcastingCultistEntity.SpellType getSpellType() {
            return SpellType.SKELETON;
        }
    }

    class RoarSpellGoal extends SpellcastingCultistEntity.UseSpellGoal {
        private int lastTargetId;
        private RoarSpellGoal() {
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else if (DiscipleEntity.this.getTarget() == null) {
                return false;
            } else if (DiscipleEntity.this.getTarget().getId() == this.lastTargetId) {
                return false;
            } else return DiscipleEntity.this.distanceTo(DiscipleEntity.this.getTarget()) < 4.0F;
        }

        public void start() {
            super.start();
            this.lastTargetId = Objects.requireNonNull(DiscipleEntity.this.getTarget()).getId();
        }

        protected int getCastingTime() {
            return 200;
        }

        protected int getCastingInterval() {
            return 120;
        }

        public void castSpell() {
            DiscipleEntity.this.setFiring(true);
            DiscipleEntity.this.cooldown = 0;
            DiscipleEntity.this.playSound(ModSounds.ROAR_SPELL.get(), 1.0F, 1.0F);
        }

        protected SoundEvent getSpellPrepareSound() {
            return ModSounds.CULTIST_PREPARE_SPELL.get();
        }

        @Override
        protected SpellcastingCultistEntity.SpellType getSpellType() {
            return SpellType.ROAR;
        }
    }

    class SacrificeSpellGoal extends SpellcastingCultistEntity.UseSpellGoal {
        private SacrificeSpellGoal() {
        }

        @Override
        public boolean canUse() {
            return super.canUse()
                    && DiscipleEntity.this.getHealth() < DiscipleEntity.this.getMaxHealth()/3
                    && DiscipleEntity.this.getTarget() != null
                    && DiscipleEntity.this.level.getDifficulty() == Difficulty.HARD
                    && DiscipleEntity.this.canSee(DiscipleEntity.this.getTarget());
        }

        protected int getCastingTime() {
            return 200;
        }

        protected int getCastingInterval() {
            return 0;
        }

        public void castSpell() {
            DiscipleEntity discipleEntity = DiscipleEntity.this;
            LivingEntity livingentity = DiscipleEntity.this.getTarget();
            if (livingentity != null) {
                BlockPos blockpos = discipleEntity.blockPosition();
                ZPiglinBruteMinionEntity summonedentity = new ZPiglinBruteMinionEntity(ModEntityType.ZPIGLIN_BRUTE_MINION.get(), discipleEntity.level);
                summonedentity.moveTo(blockpos, 0.0F, 0.0F);
                summonedentity.setHostile(true);
                summonedentity.finalizeSpawn((IServerWorld) DiscipleEntity.this.level, DiscipleEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                summonedentity.setTarget(livingentity);
                summonedentity.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, Integer.MAX_VALUE));
                summonedentity.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, Integer.MAX_VALUE));
                discipleEntity.level.addFreshEntity(summonedentity);
                discipleEntity.level.explode(discipleEntity, blockpos.getX(), blockpos.getY(), blockpos.getZ(), 2.0F, Explosion.Mode.NONE);
                discipleEntity.remove();
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
        }

        @Override
        protected SpellcastingCultistEntity.SpellType getSpellType() {
            return SpellType.SACRIFICE;
        }
    }
}
