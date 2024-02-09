package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.ally.SummonedEntity;
import com.Polarice3.Goety.common.entities.hostile.HuskarlEntity;
import com.Polarice3.Goety.common.entities.hostile.WraithEntity;
import com.Polarice3.Goety.common.entities.hostile.cultists.FanaticEntity;
import com.Polarice3.Goety.common.entities.hostile.dead.IDeadMob;
import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.common.items.magic.SoulWand;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.init.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.play.server.SEntityTeleportPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MobUtil {
    public static final Predicate<LivingEntity> NO_CREATIVE_OR_SPECTATOR = (p_200824_0_) -> {
        return !(p_200824_0_ instanceof PlayerEntity) || !p_200824_0_.isSpectator() && !((PlayerEntity)p_200824_0_).isCreative();
    };

    public static void deadSandConvert(Entity entity, boolean natural){
        if (!(entity instanceof IDeadMob)){
            MobEntity corrupt = null;
            if (entity instanceof MobEntity) {
                MobEntity monster = (MobEntity) entity;
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
                    if (monster.level.random.nextFloat() < 0.1F) {
                        corrupt = monster.convertTo(ModEntityType.MARCIRE.get(), false);
                    } else {
                        corrupt = monster.convertTo(ModEntityType.DESICCATED.get(), true);
                    }
                }
                if (monster instanceof WraithEntity){
                    corrupt = monster.convertTo(ModEntityType.BLIGHT.get(), false);
                }
                if (corrupt != null) {
                    corrupt.finalizeSpawn((IServerWorld) corrupt.level, corrupt.level.getCurrentDifficultyAt(corrupt.blockPosition()), SpawnReason.CONVERSION, null, null);
                    if (!natural) {
                        net.minecraftforge.event.ForgeEventFactory.onLivingConvert((LivingEntity) entity, corrupt);
                        new SoundUtil(corrupt, SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundCategory.HOSTILE, 1.0F, 1.0F);
                    }
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

    public static boolean validEntity(Entity entity){
        if (entity instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entity;
            return playerValidity(player, false);
        } else {
            return entity.isAttackable();
        }
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

    public static boolean undeadAndLich(LivingEntity pLivingEntity){
        if (pLivingEntity.isInvertedHealAndHarm() || pLivingEntity.getMobType() == CreatureAttribute.UNDEAD) {
            return true;
        } else {
            if (pLivingEntity instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) pLivingEntity;
                return LichdomHelper.isLich(player);
            } else {
                return false;
            }
        }
    }

    public static boolean areAllies(@Nullable Entity entity, @Nullable Entity entity1){
        if (entity != null && entity1 != null) {
            return entity.isAlliedTo(entity1) || entity1.isAlliedTo(entity);
        } else {
            return false;
        }
    }

    public static boolean areFullAllies(@Nullable Entity entity, @Nullable Entity entity1){
        if (entity != null && entity1 != null) {
            return entity.isAlliedTo(entity1) && entity1.isAlliedTo(entity);
        } else {
            return false;
        }
    }

    public static LootContext.Builder createLootContext(DamageSource pDamageSource, LivingEntity livingEntity) {
        LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld)livingEntity.level)).withRandom(livingEntity.getRandom()).withParameter(LootParameters.THIS_ENTITY, livingEntity).withParameter(LootParameters.ORIGIN, livingEntity.position()).withParameter(LootParameters.DAMAGE_SOURCE, pDamageSource).withOptionalParameter(LootParameters.KILLER_ENTITY, pDamageSource.getEntity()).withOptionalParameter(LootParameters.DIRECT_KILLER_ENTITY, pDamageSource.getDirectEntity());
        if (livingEntity.getLastHurtByMob() != null && livingEntity.getLastHurtByMob() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) livingEntity.getLastHurtByMob();
            lootcontext$builder = lootcontext$builder.withParameter(LootParameters.LAST_DAMAGE_PLAYER, player).withLuck(player.getLuck());
        }

        return lootcontext$builder;
    }

    public static class MinionMovementController extends MovementController {
        public MinionMovementController(MobEntity mob) {
            super(mob);
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

    public static class WraithMoveController extends MovementController {
        public WraithMoveController(MobEntity mob) {
            super(mob);
        }

        public void tick() {
            if (this.mob.isNoGravity()) {
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
                            this.mob.yRot = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI);
                        } else {
                            double d2 = this.mob.getTarget().getX() - this.mob.getX();
                            double d1 = this.mob.getTarget().getZ() - this.mob.getZ();
                            this.mob.yRot = -((float) MathHelper.atan2(d2, d1)) * (180F / (float) Math.PI);
                        }
                        this.mob.yBodyRot = this.mob.yRot;
                    }

                }
            } else {
                super.tick();
            }
        }
    }

    public static void twister(Entity pEntity, double pX, double pY, double pZ){
        pEntity.setDeltaMovement(pX, pY, pZ);
        pEntity.hasImpulse = true;
    }

    public static int getSummonLifespan(World world){
        return 20 * (30 + world.random.nextInt(90));
    }

    public static void secretConversion(LivingEntity livingEntity){
        if (MainConfig.CultistSpread.get()) {
            for (VillagerEntity villager : livingEntity.level.getEntitiesOfClass(VillagerEntity.class, livingEntity.getBoundingBox().inflate(4.0F))) {
                if (!villager.getTags().contains(ConstantPaths.secretCultist())) {
                    float chance = 0.05F;
                    if (villager.isBaby()) {
                        chance = 0.25F;
                    }
                    if (livingEntity.getType() == ModEntityType.CHANNELLER.get()) {
                        chance += 0.2F;
                    }
                    if (livingEntity.getType() == ModEntityType.APOSTLE.get()) {
                        chance += 0.5F;
                    }
                    if (livingEntity.getRandom().nextFloat() <= chance) {
                        villager.addTag(ConstantPaths.secretCultist());
                    }
                }
            }
        }
    }

    public static boolean getWitnesses(LivingEntity livingEntity){
        for (AbstractVillagerEntity villager : livingEntity.level.getEntitiesOfClass(AbstractVillagerEntity.class, livingEntity.getBoundingBox().inflate(16.0F))){
            if (villager.getSensing().canSee(livingEntity)){
                return true;
            }
        }
        for (IronGolemEntity golemEntity : livingEntity.level.getEntitiesOfClass(IronGolemEntity.class, livingEntity.getBoundingBox().inflate(16.0F))){
            if (golemEntity.getSensing().canSee(livingEntity)){
                return true;
            }
        }
        return false;
    }

    public static void villagerReleasePoi(VillagerEntity villager){
        villager.releasePoi(MemoryModuleType.HOME);
        villager.releasePoi(MemoryModuleType.JOB_SITE);
        villager.releasePoi(MemoryModuleType.POTENTIAL_JOB_SITE);
        villager.releasePoi(MemoryModuleType.MEETING_POINT);
    }

    public static void revealCultist(ServerWorld pLevel, VillagerEntity villager){
        if (MainConfig.CultistSpread.get()) {
            if (pLevel.getDifficulty() != Difficulty.PEACEFUL) {
                if (villager.getTags().contains(ConstantPaths.secretCultist()) && !villager.isBaby()) {
                    VillagerProfession profession = villager.getVillagerData().getProfession();
                    MonsterEntity cultist = ModEntityType.FANATIC.get().create(pLevel);
                    if (profession == VillagerProfession.CLERIC || profession == VillagerProfession.LIBRARIAN) {
                        if (pLevel.random.nextBoolean()) {
                            cultist = ModEntityType.DISCIPLE.get().create(pLevel);
                        } else {
                            cultist = ModEntityType.BELDAM.get().create(pLevel);
                        }
                    }
                    if (villager.getVillagerData().getType() == VillagerType.SWAMP){
                        if (pLevel.random.nextFloat() <= 0.25F){
                            cultist = EntityType.WITCH.create(pLevel);
                        }
                    }
                    if (cultist != null) {
                        cultist.moveTo(villager.getX(), villager.getY(), villager.getZ(), villager.yRot, villager.xRot);
                        cultist.finalizeSpawn(pLevel, pLevel.getCurrentDifficultyAt(cultist.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData) null, (CompoundNBT) null);
                        cultist.setNoAi(villager.isNoAi());
                        if (villager.hasCustomName()) {
                            cultist.setCustomName(villager.getCustomName());
                            cultist.setCustomNameVisible(villager.isCustomNameVisible());
                        }
                        if (profession == VillagerProfession.ARMORER) {
                            cultist.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
                            cultist.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                            cultist.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                            cultist.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.IRON_BOOTS));
                        }
                        if (profession == VillagerProfession.WEAPONSMITH) {
                            cultist.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
                        }
                        if (profession == VillagerProfession.BUTCHER) {
                            if (cultist instanceof FanaticEntity) {
                                FanaticEntity fanatic = (FanaticEntity) cultist;
                                fanatic.setOutfitType(0);
                            }
                            cultist.setItemSlot(EquipmentSlotType.CHEST, ItemStack.EMPTY);
                            cultist.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
                        }
                        if (profession == VillagerProfession.SHEPHERD || profession == VillagerProfession.FARMER) {
                            cultist.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.PITCHFORK.get()));
                        }
                        if (profession != VillagerProfession.BUTCHER && cultist instanceof FanaticEntity){
                            FanaticEntity fanatic = (FanaticEntity) cultist;
                            if (villager.getVillagerData().getType() == VillagerType.SWAMP){
                                fanatic.setOutfitType(5);
                            }
                        }
                        cultist.setPersistenceRequired();
                        cultist.addTag(ConstantPaths.revealedCultist());
                        pLevel.addFreshEntityWithPassengers(cultist);
                        MobUtil.villagerReleasePoi(villager);
                        villager.remove();
                    }
                }
            }
        }
    }

    public static boolean notImmuneToFrost(LivingEntity livingEntity){
        return !(livingEntity.getType().is(ModTags.EntityTypes.FROST_IMMUNE));
    }

    public static boolean immuneToFrost(LivingEntity livingEntity){
        return (livingEntity.getType().is(ModTags.EntityTypes.FROST_IMMUNE));
    }

    public static boolean extraFrostDamage(LivingEntity livingEntity){
        return livingEntity instanceof BlazeEntity || livingEntity instanceof StriderEntity || livingEntity instanceof MagmaCubeEntity || livingEntity.getType().is(ModTags.EntityTypes.FROST_EXTRA_DAMAGE);
    }

    public static void push(Entity pEntity, double pX, double pY, double pZ) {
        if (pEntity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) pEntity;
            if (MobUtil.playerValidity(player, false)) {
                player.hurtMarked = true;
                if (!player.level.isClientSide){
                    player.setOnGround(false);
                }
            }
        }
        pEntity.setDeltaMovement(pEntity.getDeltaMovement().add(pX, pY, pZ));
        pEntity.hasImpulse = true;
    }

    public static boolean isInRain(Entity pEntity){
        BlockPos blockpos = pEntity.blockPosition();
        return pEntity.level.isRainingAt(blockpos) || pEntity.level.isRainingAt(new BlockPos((double)blockpos.getX(), pEntity.getBoundingBox().maxY, (double)blockpos.getZ()));
    }

    public static boolean healthIsHalved(LivingEntity livingEntity){
        return livingEntity.getHealth() <= livingEntity.getMaxHealth()/2;
    }

    /**
     * Target Codes based of codes from @TeamTwilight
     */
    public static List<Entity> getTargets(LivingEntity pSource, double pRange) {
        return getTargets(pSource.level, pSource, pRange, 3.0D);
    }

    public static List<Entity> getTargets(World level, LivingEntity pSource, double pRange, double pRadius) {
        List<Entity> list = new ArrayList<>();
        Vector3d srcVec = pSource.getEyePosition(1.0F);
        Vector3d lookVec = pSource.getViewVector(1.0F);
        double[] lookRange = new double[] {lookVec.x() * pRange, lookVec.y() * pRange, lookVec.z() * pRange};
        Vector3d destVec = srcVec.add(lookRange[0], lookRange[1], lookRange[2]);
        List<Entity> possibleList = level.getEntities(pSource, pSource.getBoundingBox().expandTowards(lookRange[0], lookRange[1], lookRange[2]).inflate(pRadius, pRadius, pRadius),
                EntityPredicates.NO_CREATIVE_OR_SPECTATOR.and(EntityPredicates.ENTITY_STILL_ALIVE).and(entity -> !MobUtil.areAllies(entity, pSource)));
        double hitDist = 0.0D;

        for (Entity hit : possibleList) {
            if (hit.isPickable() && pSource.canSee(hit) && hit != pSource) {
                float maxSize = pSource instanceof MobEntity ? 2.0F : 0.8F;
                float borderSize = Math.max(maxSize, hit.getPickRadius());
                AxisAlignedBB collisionBB = hit.getBoundingBox().inflate(borderSize);
                Optional<Vector3d> interceptPos = collisionBB.clip(srcVec, destVec);
                if (collisionBB.contains(srcVec)) {
                    if (0.0D <= hitDist) {
                        list.add(hit);
                        hitDist = 0.0D;
                    }
                } else if (interceptPos.isPresent()) {
                    double possibleDist = srcVec.distanceTo(interceptPos.get());

                    if (possibleDist < hitDist || hitDist == 0.0D) {
                        list.add(hit);
                        hitDist = possibleDist;
                    }
                }
            }
        }
        return list;
    }

    @Nullable
    public static Entity getSingleTarget(World pLevel, LivingEntity pSource, double pRange, double pRadius) {
        return getSingleTarget(pLevel, pSource, pRange, pRadius, EntityPredicates.NO_CREATIVE_OR_SPECTATOR.and(EntityPredicates.ENTITY_STILL_ALIVE).and(entity -> !MobUtil.areAllies(entity, pSource) && entity.isPickable()));
    }

    @Nullable
    public static Entity getSingleTarget(World pLevel, LivingEntity pSource, double pRange, double pRadius, Predicate<? super Entity> predicate) {
        Entity target = null;
        Vector3d srcVec = pSource.getEyePosition(1.0F);
        Vector3d lookVec = pSource.getViewVector(1.0F);
        double[] lookRange = new double[] {lookVec.x() * pRange, lookVec.y() * pRange, lookVec.z() * pRange};
        Vector3d destVec = srcVec.add(lookRange[0], lookRange[1], lookRange[2]);
        List<Entity> possibleList = pLevel.getEntities(pSource, pSource.getBoundingBox().expandTowards(lookRange[0], lookRange[1], lookRange[2]).inflate(pRadius, pRadius, pRadius),
                predicate);
        double hitDist = 0.0D;

        for (Entity hit : possibleList) {
            if (pSource.canSee(pSource) && hit != pSource) {
                float maxSize = pSource instanceof MobEntity ? 2.0F : 0.8F;
                float borderSize = Math.max(maxSize, hit.getPickRadius());
                AxisAlignedBB collisionBB = hit.getBoundingBox().inflate(borderSize, borderSize, borderSize);
                Optional<Vector3d> interceptPos = collisionBB.clip(srcVec, destVec);

                if (collisionBB.contains(srcVec)) {
                    if (0.0D <= hitDist) {
                        target = hit;
                        hitDist = 0.0D;
                    }
                } else if (interceptPos.isPresent()) {
                    double possibleDist = srcVec.distanceTo(interceptPos.get());

                    if (possibleDist < hitDist || hitDist == 0.0D) {
                        target = hit;
                        hitDist = possibleDist;
                    }
                }
            }
        }
        return target;
    }

    /**
     * Code based of @BobMowzies Sunstrike positioning.
     */
    public static void moveDownToGround(Entity entity) {
        RayTraceResult rayTrace = rayTrace(entity);
        if (rayTrace.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult hitResult = (BlockRayTraceResult) rayTrace;
            if (hitResult.getDirection() == Direction.UP) {
                BlockState hitBlock = entity.level.getBlockState(hitResult.getBlockPos());
                if (hitBlock.getBlock() instanceof SlabBlock && hitBlock.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.BOTTOM) {
                    entity.setPos(entity.getX(), hitResult.getBlockPos().getY() + 1.0625F - 0.5f, entity.getZ());
                } else {
                    entity.setPos(entity.getX(), hitResult.getBlockPos().getY() + 1.0625F, entity.getZ());
                }
                if (entity.level instanceof ServerWorld) {
                    ((ServerWorld) entity.level).getChunkSource().broadcastAndSend(entity, new SEntityTeleportPacket(entity));
                }
            }
        }
    }

    public static RayTraceResult rayTrace(Entity entity) {
        Vector3d startPos = new Vector3d(entity.getX(), entity.getY(), entity.getZ());
        Vector3d endPos = new Vector3d(entity.getX(), 0, entity.getZ());
        return entity.level.clip(new RayTraceContext(startPos, endPos, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entity));
    }

    public static BlockRayTraceResult rayTrace(Entity entity, double distance, boolean fluids) {
        return (BlockRayTraceResult) entity.pick(distance, 1.0F, fluids);
    }

    public static List<EntityType<?>> getEntityTypesConfig(List<? extends String> config){
        List<EntityType<?>> list = new ArrayList<>();
        if (!config.isEmpty()){
            for (String id : config){
                EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(id));
                if (entityType != null){
                    list.add(entityType);
                }
            }
        }
        return list;
    }

    public static boolean hasEntityTypesConfig(List<? extends String> config, EntityType<?> entityType){
        return !getEntityTypesConfig(config).isEmpty() && getEntityTypesConfig(config).contains(entityType);
    }

    public static void summonTame(Entity entity, PlayerEntity player){
        if (entity instanceof TameableEntity){
            TameableEntity tamableAnimal = (TameableEntity) entity;
            tamableAnimal.tame(player);
        } else if (entity instanceof AbstractHorseEntity){
            AbstractHorseEntity horse = (AbstractHorseEntity) entity;
            horse.setTamed(true);
            horse.setOwnerUUID(player.getUUID());
        } else if (entity instanceof OwnedEntity) {
            OwnedEntity summonedEntity = (OwnedEntity) entity;
            summonedEntity.setPersistenceRequired();
            summonedEntity.setOwnerId(player.getUUID());
            if (summonedEntity instanceof SummonedEntity){
                ((SummonedEntity) summonedEntity).setWandering(false);
            }
        }
    }

    /**
     * Mind Bending, lol.
     */
    public static boolean ownerStack(OwnedEntity owned0, OwnedEntity owned1){
        LivingEntity masterOwner0 = owned0.getMasterOwner();
        LivingEntity masterOwner1 = owned1.getMasterOwner();
        LivingEntity trueOwner0 = owned0.getTrueOwner();
        LivingEntity trueOwner1 = owned1.getTrueOwner();
        if (trueOwner0 != null && trueOwner1 != null){
            if (masterOwner0 != null && masterOwner1 != null){
                return masterOwner0 == masterOwner1;
            } else if (masterOwner0 != null){
                return masterOwner0 == trueOwner1;
            } else if (masterOwner1 != null){
                return masterOwner1 == trueOwner0;
            } else {
                return trueOwner0 == trueOwner1;
            }
        }
        return false;
    }

    /**
     * Copy of Vanilla's getEquipmentDropChance. Had to accesstransformer handDropChances and armorDropChances
     */
    public static float getEquipmentDropChance(MobEntity mob, EquipmentSlotType p_21520_) {
        float f;
        switch (p_21520_.getType()) {
            case HAND:
                f = mob.handDropChances[p_21520_.getIndex()];
                break;
            case ARMOR:
                f = mob.armorDropChances[p_21520_.getIndex()];
                break;
            default:
                f = 0.0F;
        }

        return f;
    }

    @Nullable
    public static Entity convertTo(Entity originalEntity, EntityType<?> convertedType, boolean loot, boolean newEquip, PlayerEntity player) {
        if (originalEntity.removed) {
            return null;
        } else {
            Entity newEntity = convertedType.create(originalEntity.level);
            if (newEntity != null) {
                newEntity.copyPosition(originalEntity);
                if (originalEntity instanceof MobEntity && newEntity instanceof MobEntity) {
                    MobEntity originalMob = (MobEntity) originalEntity;
                    MobEntity newMob = (MobEntity) newEntity;
                    newMob.setBaby(originalMob.isBaby());
                    newMob.setNoAi(originalMob.isNoAi());
                    if (originalMob.hasCustomName()) {
                        newEntity.setCustomName(originalMob.getCustomName());
                        newEntity.setCustomNameVisible(originalMob.isCustomNameVisible());
                    }

                    if (originalMob.isPersistenceRequired()) {
                        newMob.setPersistenceRequired();
                    }

                    newMob.setInvulnerable(originalMob.isInvulnerable());
                    if (loot) {
                        newMob.setCanPickUpLoot(originalMob.canPickUpLoot());

                        for (EquipmentSlotType equipmentslot : EquipmentSlotType.values()) {
                            ItemStack itemstack = originalMob.getItemBySlot(equipmentslot);
                            if (!itemstack.isEmpty()) {
                                newMob.setItemSlot(equipmentslot, itemstack.copy());
                                newMob.setDropChance(equipmentslot, getEquipmentDropChance(originalMob, equipmentslot));
                                itemstack.setCount(0);
                            }
                        }
                    }
                    if (player != null){
                        summonTame(newMob, player);
                    }

                    if (originalMob.level instanceof ServerWorld) {
                        ServerWorld serverLevel = (ServerWorld) originalMob.level;
                        if (originalMob instanceof VillagerEntity && newMob instanceof ZombieVillagerEntity) {
                            VillagerEntity villager = (VillagerEntity) originalMob;
                            ZombieVillagerEntity zombievillager = (ZombieVillagerEntity) newMob;
                            zombievillager.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(zombievillager.blockPosition()), SpawnReason.CONVERSION, new ZombieEntity.GroupData(false, true), null);
                            zombievillager.setVillagerData(villager.getVillagerData());
                            zombievillager.setGossips(villager.getGossips().store(NBTDynamicOps.INSTANCE).getValue());
                            zombievillager.setTradeOffers(villager.getOffers().createTag());
                            zombievillager.setVillagerXp(villager.getVillagerXp());
                            if (!originalMob.isSilent()) {
                                serverLevel.levelEvent(null, 1026, originalMob.blockPosition(), 0);
                            }
                        } else if (newEquip && newMob instanceof OwnedEntity){
                            OwnedEntity owned = (OwnedEntity) newMob;
                            owned.convertNewEquipment(originalEntity);
                        }
                    }

                    originalMob.level.addFreshEntity(newEntity);
                    if (originalMob.isPassenger()) {
                        Entity entity = originalMob.getVehicle();
                        if (entity != null) {
                            originalMob.stopRiding();
                            newEntity.startRiding(entity, true);
                        }
                    }
                }

                originalEntity.remove();
            }
            return newEntity;
        }
    }

    public static boolean isSpellCasting(LivingEntity livingEntity){
        return livingEntity.isUsingItem() && livingEntity.getUseItem().getItem() instanceof SoulWand && !WandUtil.findFocus(livingEntity).isEmpty();
    }

}
