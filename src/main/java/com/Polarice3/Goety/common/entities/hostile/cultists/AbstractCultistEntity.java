package com.Polarice3.Goety.common.entities.hostile.cultists;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.hostile.dead.IDeadMob;
import com.Polarice3.Goety.common.entities.neutral.ICustomAttributes;
import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public abstract class AbstractCultistEntity extends AbstractRaiderEntity implements ICustomAttributes {
    private static final DataParameter<Float> DATA_REINFORCEMENT_CHANCE = EntityDataManager.defineId(AbstractCultistEntity.class, DataSerializers.FLOAT);
    private BlockPos pilgrimTarget;
    private boolean pilgrimLeader;
    private boolean pilgrimage;

    protected AbstractCultistEntity(EntityType<? extends AbstractCultistEntity> type, World worldIn) {
        super(type, worldIn);
        ICustomAttributes.applyAttributesForEntity(type, this);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(PathNodeType.DAMAGE_FIRE, -1.0F);
        this.getNavigation().setCanFloat(true);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new FindTargetGoal(this, 10.0F));
        this.goalSelector.addGoal(4, new PilgrimGoal<>(this, 0.7D, 0.595D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this).setAlertOthers()));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, AbstractCultistEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, WitchEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return null;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_REINFORCEMENT_CHANCE, 0.0F);
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.pilgrimTarget != null) {
            pCompound.put("PilgrimTarget", NBTUtil.writeBlockPos(this.pilgrimTarget));
        }

        pCompound.putBoolean("PilgrimLeader", this.pilgrimLeader);
        pCompound.putBoolean("Pilgrimage", this.pilgrimage);
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("PilgrimTarget")) {
            this.pilgrimTarget = NBTUtil.readBlockPos(pCompound.getCompound("PilgrimTarget"));
        }

        this.pilgrimLeader = pCompound.getBoolean("PilgrimLeader");
        this.pilgrimage = pCompound.getBoolean("Pilgrimage");
    }

    public boolean canBePilgrimLeader() {
        return this.getEntity() instanceof ICultist;
    }

    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return !this.pilgrimage || pDistanceToClosestPlayer > 16384.0D;
    }

    public void setPilgrimTarget(BlockPos p_213631_1_) {
        this.pilgrimTarget = p_213631_1_;
        this.pilgrimage = true;
    }

    public BlockPos getPilgrimTarget() {
        return this.pilgrimTarget;
    }

    public boolean hasPilgrimTarget() {
        return this.pilgrimTarget != null;
    }

    public void setPilgrimLeader(boolean pIsLeader) {
        this.pilgrimLeader = pIsLeader;
        this.pilgrimage = true;
    }

    public boolean isPilgrimLeader() {
        return this.pilgrimLeader;
    }

    public boolean canJoinPilgrimage() {
        return this.canChangeDimensions();
    }

    public void findNewPilgrimTarget() {
        BlockPos blockPos = this.blockPosition().offset(-500 + this.random.nextInt(1000), 0, -500 + this.random.nextInt(1000));
        if (this.level instanceof ServerWorld){
            blockPos = ((ServerWorld)this.level).getChunkSource().getGenerator().findNearestMapFeature((ServerWorld)this.level, Structure.NETHER_BRIDGE, this.blockPosition(), 100, false);
            if (blockPos == null){
                blockPos = this.blockPosition().offset(-500 + this.random.nextInt(1000), 0, -500 + this.random.nextInt(1000));
            }
        }
        this.pilgrimTarget = blockPos;
        this.pilgrimage = true;
    }

    protected boolean isOnPilgrimage() {
        return this.pilgrimage;
    }

    protected void setOnPilgrimage(boolean p_226541_1_) {
        this.pilgrimage = p_226541_1_;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getEntity() instanceof ICultist){
            this.conversion();
        }
        if (this.getReinforcementChance() < 0.0F){
            this.setReinforcementChance(0.0F);
        }
    }

    public void conversion(){
        if (MainConfig.CultistSpread.get()) {
            int timer = 1200;
            if (this.hasActiveRaid()) {
                timer = 400;
            }
            if (this.tickCount % timer == 0) {
                MobUtil.secretConversion(this);
            }
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isPassenger() && this.getVehicle() instanceof CrimsonSpiderEntity){
            if (pSource == DamageSource.IN_WALL){
                return false;
            }
        }
        if (this.level instanceof ServerWorld){
            ServerWorld serverworld = (ServerWorld)this.level;
            if (this.getEntity() instanceof ICultist) {
                LivingEntity livingentity = this.getTarget();
                if (livingentity == null && pSource.getEntity() instanceof LivingEntity) {
                    livingentity = (LivingEntity) pSource.getEntity();
                }

                int i = MathHelper.floor(this.getX());
                int j = MathHelper.floor(this.getY());
                int k = MathHelper.floor(this.getZ());

                if (livingentity != null && this.level.getDifficulty() == Difficulty.HARD && this.random.nextFloat() < this.getReinforcementChance() && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                    AbstractCultistEntity cultist = null;
                    int random = this.random.nextInt(7);
                    switch (random){
                        case 0:
                            cultist = ModEntityType.FANATIC.get().create(serverworld);
                            break;
                        case 2:
                            cultist = ModEntityType.ZEALOT.get().create(serverworld);
                            break;
                        case 4:
                            cultist = ModEntityType.DISCIPLE.get().create(serverworld);
                            break;
                        case 5:
                            cultist = ModEntityType.BELDAM.get().create(serverworld);
                            break;
                        case 6:
                            cultist = ModEntityType.THUG.get().create(serverworld);
                    }
                    if (cultist != null) {
                        for (int l = 0; l < 50; ++l) {
                            int i1 = i + MathHelper.nextInt(this.random, 7, 16) * MathHelper.nextInt(this.random, -1, 1);
                            int j1 = j + MathHelper.nextInt(this.random, 7, 16) * MathHelper.nextInt(this.random, -1, 1);
                            int k1 = k + MathHelper.nextInt(this.random, 7, 16) * MathHelper.nextInt(this.random, -1, 1);
                            BlockPos blockpos = new BlockPos(i1, j1, k1);
                            EntityType<?> entitytype = cultist.getType();
                            EntitySpawnPlacementRegistry.PlacementType entityspawnplacementregistry$placementtype = EntitySpawnPlacementRegistry.getPlacementType(entitytype);
                            if (WorldEntitySpawner.isSpawnPositionOk(entityspawnplacementregistry$placementtype, this.level, blockpos, entitytype) && EntitySpawnPlacementRegistry.checkSpawnRules(entitytype, serverworld, SpawnReason.REINFORCEMENT, blockpos, this.level.random)) {
                                cultist.setPos((double) i1, (double) j1, (double) k1);
                                if (!this.level.hasNearbyAlivePlayer((double) i1, (double) j1, (double) k1, 7.0D) && this.level.isUnobstructed(cultist) && this.level.noCollision(cultist) && !this.level.containsAnyLiquid(cultist.getBoundingBox())) {
                                    cultist.setTarget(livingentity);
                                    cultist.finalizeSpawn(serverworld, this.level.getCurrentDifficultyAt(cultist.blockPosition()), SpawnReason.REINFORCEMENT, (ILivingEntityData) null, (CompoundNBT) null);
                                    serverworld.addFreshEntityWithPassengers(cultist);
                                    this.setReinforcementChance(this.getReinforcementChance() - 0.05F);
                                    if (cultist instanceof ICultist){
                                        cultist.setReinforcementChance(cultist.getReinforcementChance() - 0.05F);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        this.alertWitches();
        return super.hurt(pSource, pAmount);
    }

    protected void alertWitches(){
        double d0 = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        AxisAlignedBB axisalignedbb = AxisAlignedBB.unitCubeFromLowerCorner(this.position()).inflate(d0, 10.0D, d0);
        List<MobEntity> list = this.level.getLoadedEntitiesOfClass(MobEntity.class, axisalignedbb);

        for (MobEntity mob : list){
            if (this.getLastHurtByMob() != null && !(this.getLastHurtByMob() instanceof AbstractRaiderEntity) && !mob.isAlliedTo(this.getLastHurtByMob()) && !this.isAlliedTo(this.getLastHurtByMob())) {
                if (mob instanceof WitchEntity) {
                    mob.setTarget(this.getLastHurtByMob());
                }
            }
        }
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        if (this.getEntity() instanceof ICultist){
            this.randomizeReinforcementsChance();
            if (this.random.nextFloat() < difficultyIn.getSpecialMultiplier() * 0.05F) {
                this.setReinforcementChance(this.getReinforcementChance() +(float) (this.random.nextDouble() * 0.25D + 0.5D));
            }
        }

        if (reason == SpawnReason.EVENT && worldIn.getLevel().dimension() == World.NETHER){
            this.pilgrimage = true;
        }

        if ((reason == SpawnReason.NATURAL || reason == SpawnReason.STRUCTURE) && worldIn.getLevel().dimension() == World.NETHER){
            this.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public static ItemStack getBanner() {
        ItemStack itemstack = new ItemStack(Items.RED_BANNER);
        CompoundNBT compoundnbt = itemstack.getOrCreateTagElement("BlockEntityTag");
        ListNBT listnbt = (new BannerPattern.Builder()).addPattern(BannerPattern.CROSS, DyeColor.YELLOW).addPattern(BannerPattern.TRIANGLE_BOTTOM, DyeColor.RED).addPattern(BannerPattern.TRIANGLE_TOP, DyeColor.RED).addPattern(BannerPattern.STRIPE_MIDDLE, DyeColor.YELLOW).addPattern(BannerPattern.STRIPE_CENTER, DyeColor.RED).addPattern(BannerPattern.STRIPE_CENTER, DyeColor.YELLOW).addPattern(BannerPattern.STRAIGHT_CROSS, DyeColor.RED).addPattern(BannerPattern.BORDER, DyeColor.RED).addPattern(BannerPattern.RHOMBUS_MIDDLE, DyeColor.RED).addPattern(BannerPattern.CIRCLE_MIDDLE, DyeColor.YELLOW).toListTag();
        compoundnbt.put("Patterns", listnbt);
        itemstack.hideTooltipPart(ItemStack.TooltipDisplayFlags.ADDITIONAL);
        itemstack.setHoverName((new TranslationTextComponent("block.goety.cultist_banner")));
        return itemstack;
    }

    protected void randomizeReinforcementsChance() {
        this.setReinforcementChance((float) (this.random.nextDouble() * 0.1D));
    }

    public void setReinforcementChance (float reinforcementChance){
        this.entityData.set(DATA_REINFORCEMENT_CHANCE, reinforcementChance);
    }

    public float getReinforcementChance() {
        return this.entityData.get(DATA_REINFORCEMENT_CHANCE);
    }

    @OnlyIn(Dist.CLIENT)
    public ArmPose getArmPose() {
        return ArmPose.CROSSED;
    }

    @Override
    public void applyRaidBuffs(int wave, boolean p_213660_2_) {
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 1.62F;
    }

    public boolean canBeLeader() {
        return false;
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof WitchEntity) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else if (entityIn instanceof AbstractCultistEntity) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else if (entityIn instanceof IDeadMob) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else if (entityIn instanceof AbstractPiglinEntity){
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return entityIn instanceof OwnedEntity && ((OwnedEntity) entityIn).getTrueOwner() instanceof AbstractCultistEntity;
        }
    }

    public static boolean spawnCultistsRules(EntityType<?> pType, IWorld pLevel, SpawnReason pReason, BlockPos pPos, Random pRandom) {
        return pLevel.getBrightness(LightType.BLOCK, pPos) <= 8
                && pLevel.getDifficulty() != Difficulty.PEACEFUL
                && (pReason == SpawnReason.SPAWNER || pLevel.getBlockState(pPos).isValidSpawn(pLevel, pPos, pType));
    }

    public static class PilgrimGoal<T extends AbstractCultistEntity> extends Goal {
        private final T mob;
        private final double speedModifier;
        private final double leaderSpeedModifier;
        private long cooldownUntil;

        public PilgrimGoal(T p_i50070_1_, double p_i50070_2_, double p_i50070_4_) {
            this.mob = p_i50070_1_;
            this.speedModifier = p_i50070_2_;
            this.leaderSpeedModifier = p_i50070_4_;
            this.cooldownUntil = -1L;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            boolean flag = this.mob.level.getGameTime() < this.cooldownUntil;
            return this.mob.isOnPilgrimage() && this.mob.getTarget() == null && !this.mob.isVehicle() && this.mob.hasPilgrimTarget() && !flag && this.mob.level.dimension() == World.NETHER;
        }

        public void start() {
        }

        public void stop() {
        }

        public void tick() {
            boolean flag = this.mob.isPilgrimLeader();
            PathNavigator pathnavigator = this.mob.getNavigation();
            if (pathnavigator.isDone()) {
                List<AbstractCultistEntity> list = this.findPilgrims();
                if (this.mob.isOnPilgrimage() && list.isEmpty()) {
                    this.mob.setOnPilgrimage(false);
                } else if (flag && this.mob.getPilgrimTarget().closerThan(this.mob.position(), 10.0D)) {
                    this.mob.findNewPilgrimTarget();
                } else {
                    Vector3d vector3d = Vector3d.atBottomCenterOf(this.mob.getPilgrimTarget());
                    Vector3d vector3d1 = this.mob.position();
                    Vector3d vector3d2 = vector3d1.subtract(vector3d);
                    vector3d = vector3d2.yRot(90.0F).scale(0.4D).add(vector3d);
                    Vector3d vector3d3 = vector3d.subtract(vector3d1).normalize().scale(10.0D).add(vector3d1);
                    BlockPos blockpos = new BlockPos(vector3d3);
                    if (this.mob.level instanceof ServerWorld){
                        blockpos = ((ServerWorld)this.mob.level).getChunkSource().getGenerator().findNearestMapFeature((ServerWorld)this.mob.level, Structure.NETHER_BRIDGE, blockpos, 100, false);
                    } else {
                        blockpos = this.mob.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos);
                    }
                    if (blockpos != null) {
                        if (!pathnavigator.moveTo((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ(), flag ? this.leaderSpeedModifier : this.speedModifier)) {
                            this.moveRandomly();
                            this.cooldownUntil = this.mob.level.getGameTime() + 200L;
                        } else if (flag) {
                            for (AbstractCultistEntity patrollerentity : list) {
                                patrollerentity.setPilgrimTarget(blockpos);
                            }
                        }
                    }
                }
            }

        }

        private List<AbstractCultistEntity> findPilgrims() {
            return this.mob.level.getEntitiesOfClass(AbstractCultistEntity.class, this.mob.getBoundingBox().inflate(16.0D), (p_226543_1_) -> {
                return p_226543_1_.canJoinPilgrimage() && !p_226543_1_.is(this.mob);
            });
        }

        private boolean moveRandomly() {
            Random random = this.mob.getRandom();
            BlockPos blockpos = ((ServerWorld)this.mob.level).getChunkSource().getGenerator().findNearestMapFeature((ServerWorld)this.mob.level, Structure.NETHER_BRIDGE, this.mob.blockPosition(), 100, false);
            if (blockpos == null){
                blockpos = this.mob.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, this.mob.blockPosition().offset(-8 + random.nextInt(16), 0, -8 + random.nextInt(16)));
            }
            return this.mob.getNavigation().moveTo((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), this.speedModifier);
        }
    }

    public class FindTargetGoal extends Goal {
        private final AbstractCultistEntity mob;
        private final float hostileRadiusSqr;
        public final EntityPredicate shoutTargeting = (new EntityPredicate()).range(8.0D).allowNonAttackable().allowInvulnerable().allowSameTeam().allowUnseeable().ignoreInvisibilityTesting();

        public FindTargetGoal(AbstractCultistEntity p_i50573_2_, float p_i50573_3_) {
            this.mob = p_i50573_2_;
            this.hostileRadiusSqr = p_i50573_3_ * p_i50573_3_;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.mob.getLastHurtByMob();
            return this.mob.isOnPilgrimage() && this.mob.getTarget() != null && !this.mob.isAggressive() && (livingentity == null || livingentity.getType() != EntityType.PLAYER);
        }

        public void start() {
            super.start();
            this.mob.getNavigation().stop();

            for(AbstractCultistEntity cultist : this.mob.level.getNearbyEntities(AbstractCultistEntity.class, this.shoutTargeting, this.mob, this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D))) {
                cultist.setTarget(this.mob.getTarget());
            }

        }

        public void stop() {
            super.stop();
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null) {
                for(AbstractCultistEntity cultist : this.mob.level.getNearbyEntities(AbstractCultistEntity.class, this.shoutTargeting, this.mob, this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D))) {
                    cultist.setTarget(livingentity);
                    cultist.setAggressive(true);
                }

                this.mob.setAggressive(true);
            }

        }

        public void tick() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null) {
                if (this.mob.distanceToSqr(livingentity) > (double)this.hostileRadiusSqr) {
                    this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                    if (this.mob.random.nextInt(50) == 0) {
                        this.mob.playAmbientSound();
                    }
                } else {
                    this.mob.setAggressive(true);
                }

                super.tick();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static enum ArmPose {
        CROSSED,
        ATTACKING,
        SPELLCASTING,
        PRAYING,
        SPELL_AND_WEAPON,
        BOW_AND_ARROW,
        TORCH_AND_WEAPON,
        CROSSBOW_HOLD,
        CROSSBOW_CHARGE,
        BOMB_AND_WEAPON,
        THROW_SPEAR,
        DYING,
        NEUTRAL;
    }
}
