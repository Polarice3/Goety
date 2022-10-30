package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.hostile.HuskarlEntity;
import com.Polarice3.Goety.common.entities.hostile.cultists.FanaticEntity;
import com.Polarice3.Goety.common.entities.hostile.dead.IDeadMob;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.init.ModTags;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.function.Predicate;

public class MobUtil {
    public static final Predicate<LivingEntity> NO_CREATIVE_OR_SPECTATOR = (p_200824_0_) -> {
        return !(p_200824_0_ instanceof PlayerEntity) || !p_200824_0_.isSpectator() && !((PlayerEntity)p_200824_0_).isCreative();
    };

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
                    new SoundUtil(corrupt, SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundCategory.HOSTILE, 1.0F, 1.0F);
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

    public static boolean validEntity(LivingEntity livingEntity){
        if (livingEntity instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) livingEntity;
            return playerValidity(player, false);
        } else {
            return true;
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
        return !(livingEntity instanceof SnowGolemEntity) && !(livingEntity instanceof StrayEntity) && !(livingEntity instanceof PolarBearEntity) && !(livingEntity instanceof WitherEntity) && !(livingEntity.getType().is(ModTags.EntityTypes.FROST_IMMUNE));
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
}
