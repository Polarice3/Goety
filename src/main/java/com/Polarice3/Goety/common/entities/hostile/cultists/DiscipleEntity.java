package com.Polarice3.Goety.common.entities.hostile.cultists;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Predicate;

public class DiscipleEntity extends SpellcastingCultistEntity implements ICultist{
    private int f;
    private final Predicate<Entity> field_213690_b = Entity::isAlive;
    private boolean roarparticles;

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
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.PILLAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        if ((double)worldIn.getRandom().nextFloat() < 0.05D) {
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
        int random = this.level.random.nextInt(3);
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.NETHER_BOOK.get()));
        this.setDropChance(EquipmentSlotType.MAINHAND, 0.025F);
        this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.CULTISTHELM.get()));
        this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(ModItems.CULTISTROBE.get()));
        this.setDropChance(EquipmentSlotType.HEAD, 0.0F);
        this.setDropChance(EquipmentSlotType.CHEST, 0.0F);
        switch (random){
            case 0:
                break;
            case 1:
                this.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
                this.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.LEATHER_BOOTS));
                break;
            case 2:
                this.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                this.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.IRON_BOOTS));
                break;
        }
    }

    public boolean isFiring(){
        return this.roarparticles;
    }

    public void setFiring(boolean firing){
        this.roarparticles = firing;
    }

    @Override
    protected SoundEvent getCastingSoundEvent () {
        return SoundEvents.EVOKER_CAST_SPELL;
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
                if (this.level.isClientSide) {
                    for (int i = 0; i < 40; ++i) {
                        double d0 = this.random.nextGaussian() * 0.2D;
                        double d1 = this.random.nextGaussian() * 0.2D;
                        double d2 = this.random.nextGaussian() * 0.2D;
                        new ParticleUtil(ParticleTypes.POOF, vector3d.x, vector3d.y, vector3d.z, d0, d1, d2);
                    }
                }
            }
            if (this.f >= 10){
                this.setFiring(false);
                this.f = 0;
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
            return super.canUse() && DiscipleEntity.this.canSee(DiscipleEntity.this.getTarget());
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
                double d0 = DiscipleEntity.this.distanceToSqr(livingentity);
                float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;
                double d1 = livingentity.getX() - DiscipleEntity.this.getX();
                double d2 = livingentity.getY(0.5D) - DiscipleEntity.this.getY(0.5D);
                double d3 = livingentity.getZ() - DiscipleEntity.this.getZ();
                for(int i = 0; i < 3; ++i) {
                    SmallFireballEntity smallfireballentity = new SmallFireballEntity(DiscipleEntity.this.level, DiscipleEntity.this, d1 + DiscipleEntity.this.getRandom().nextGaussian() * (double)f, d2, d3 + DiscipleEntity.this.getRandom().nextGaussian() * (double)f);
                    smallfireballentity.setPos(smallfireballentity.getX(), DiscipleEntity.this.getY(0.5), smallfireballentity.getZ());
                    DiscipleEntity.this.level.addFreshEntity(smallfireballentity);
                }
                if (!DiscipleEntity.this.isSilent()) {
                    DiscipleEntity.this.level.levelEvent(null, 1016, DiscipleEntity.this.blockPosition(), 0);
                }
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_CAST_SPELL;
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
            return super.canUse() && DiscipleEntity.this.canSee(DiscipleEntity.this.getTarget());
        }

        protected int getCastingTime() {
            return MainConfig.ZombieDuration.get();
        }

        protected int getCastingInterval() {
            return 250;
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
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_CAST_SPELL;
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
            return super.canUse() && DiscipleEntity.this.canSee(DiscipleEntity.this.getTarget());
        }

        protected int getCastingTime() {
            return MainConfig.SkeletonDuration.get();
        }

        protected int getCastingInterval() {
            return 250;
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
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_CAST_SPELL;
        }

        @Override
        protected SpellcastingCultistEntity.SpellType getSpellType() {
            return SpellType.ZOMBIE;
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
            DiscipleEntity.this.playSound(ModSounds.ROAR_SPELL.get(), 1.0F, 1.0F);
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_CAST_SPELL;
        }

        @Override
        protected SpellcastingCultistEntity.SpellType getSpellType() {
            return SpellType.ROAR;
        }
    }
}
