package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.ally.*;
import com.Polarice3.Goety.common.entities.bosses.ApostleEntity;
import com.Polarice3.Goety.common.entities.bosses.PenanceEntity;
import com.Polarice3.Goety.common.entities.bosses.VizierEntity;
import com.Polarice3.Goety.common.entities.hostile.*;
import com.Polarice3.Goety.common.entities.hostile.cultists.*;
import com.Polarice3.Goety.common.entities.hostile.illagers.ConquillagerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.EnviokerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.InquillagerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.TormentorEntity;
import com.Polarice3.Goety.common.entities.neutral.*;
import com.Polarice3.Goety.common.entities.projectiles.*;
import com.Polarice3.Goety.common.entities.utilities.*;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityType {

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Goety.MOD_ID);

    public static final RegistryObject<EntityType<TankEntity>> TANK = ENTITY_TYPES.register("tank",
            () -> EntityType.Builder.of(TankEntity::new, EntityClassification.MONSTER)
                    .fireImmune()
                    .sized(2.0f, 2.5f)
                    .build(new ResourceLocation(Goety.MOD_ID, "tank").toString()));

    public static final RegistryObject<EntityType<FriendlyTankEntity>> FRIENDTANK = ENTITY_TYPES.register("friendtank",
            () -> EntityType.Builder.of(FriendlyTankEntity::new, EntityClassification.MONSTER)
                    .fireImmune()
                    .sized(2.0f, 2.5f)
                    .build(new ResourceLocation(Goety.MOD_ID, "friendtank").toString()));

    public static final RegistryObject<EntityType<WitchBombEntity>> WITCHBOMB = ENTITY_TYPES.register("witchbomb",
            () -> EntityType.Builder.<WitchBombEntity>of(WitchBombEntity::new, EntityClassification.MISC)
                    .sized(0.25f,0.25f)
                    .clientTrackingRange(4)
                    .build(new ResourceLocation(Goety.MOD_ID, "witchbomb").toString()));

    public static final RegistryObject<EntityType<SoulFireballEntity>> SOUL_FIREBALL = ENTITY_TYPES.register("soulfireball",
            () -> EntityType.Builder.<SoulFireballEntity>of(SoulFireballEntity::new, EntityClassification.MISC)
                    .sized(1.0f,1.0f)
                    .clientTrackingRange(4)
                    .build(new ResourceLocation(Goety.MOD_ID, "soulfireball").toString()));

    public static final RegistryObject<EntityType<WarpedSpearEntity>> WARPED_SPEAR = ENTITY_TYPES.register("warped_spear",
            () -> EntityType.Builder.<WarpedSpearEntity>of(WarpedSpearEntity::new, EntityClassification.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build(new ResourceLocation(Goety.MOD_ID, "warped_spear").toString()));

    public static final RegistryObject<EntityType<PitchforkEntity>> PITCHFORK = ENTITY_TYPES.register("pitchfork",
            () -> EntityType.Builder.<PitchforkEntity>of(PitchforkEntity::new, EntityClassification.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build(new ResourceLocation(Goety.MOD_ID, "pitchfork").toString()));

    public static final RegistryObject<EntityType<NetherBallEntity>> NETHERBALL = ENTITY_TYPES.register("scorchball",
            () -> EntityType.Builder.<NetherBallEntity>of(NetherBallEntity::new, EntityClassification.MISC)
                    .sized(1.0f,1.0f)
                    .clientTrackingRange(4)
                    .build(new ResourceLocation(Goety.MOD_ID, "scorchball").toString()));

    public static final RegistryObject<EntityType<SoulSkullEntity>> SOULSKULL = ENTITY_TYPES.register("soulskull",
            () -> EntityType.Builder.<SoulSkullEntity>of(SoulSkullEntity::new, EntityClassification.MISC)
                    .sized(0.3125F, 0.3125F)
                    .clientTrackingRange(4)
                    .updateInterval(1)
                    .build(new ResourceLocation(Goety.MOD_ID, "soulskull").toString()));

    public static final RegistryObject<EntityType<SoulBulletEntity>> SOUL_BULLET = ENTITY_TYPES.register("soul_bullet",
            () -> EntityType.Builder.<SoulBulletEntity>of(SoulBulletEntity::new, EntityClassification.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(1)
                    .build(new ResourceLocation(Goety.MOD_ID, "soul_bullet").toString()));

    public static final RegistryObject<EntityType<PoisonBallEntity>> POISON_BALL = ENTITY_TYPES.register("poison_ball",
            () -> EntityType.Builder.<PoisonBallEntity>of(PoisonBallEntity::new, EntityClassification.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(1)
                    .build(new ResourceLocation(Goety.MOD_ID, "poison_ball").toString()));

    public static final RegistryObject<EntityType<FangEntity>> FANG = ENTITY_TYPES.register("fang",
            () -> EntityType.Builder.<FangEntity>of(FangEntity::new, EntityClassification.MISC)
                    .sized(0.5F, 0.8F)
                    .clientTrackingRange(6)
                    .updateInterval(2)
                    .build(new ResourceLocation(Goety.MOD_ID, "fang").toString()));

    public static final RegistryObject<EntityType<WitchGaleEntity>> WITCHGALE = ENTITY_TYPES.register("witchgale",
            () -> EntityType.Builder.<WitchGaleEntity>of(WitchGaleEntity::new, EntityClassification.MISC)
                    .sized(1.0f,1.0f)
                    .clientTrackingRange(4)
                    .build(new ResourceLocation(Goety.MOD_ID, "witchgale").toString()));

    public static final RegistryObject<EntityType<FireTornadoEntity>> FIRETORNADO = ENTITY_TYPES.register("fire_tornado",
            () -> EntityType.Builder.<FireTornadoEntity>of(FireTornadoEntity::new, EntityClassification.MISC)
                    .sized(2.0f,3.0f)
                    .clientTrackingRange(4)
                    .build(new ResourceLocation(Goety.MOD_ID, "fire_tornado").toString()));

    public static final RegistryObject<EntityType<ChannellerEntity>> CHANNELLER = ENTITY_TYPES.register("channeller",
            () -> EntityType.Builder.of(ChannellerEntity::new, EntityClassification.MONSTER)
                    .canSpawnFarFromPlayer()
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "channeller").toString()));

    public static final RegistryObject<EntityType<FanaticEntity>> FANATIC = ENTITY_TYPES.register("fanatic",
            () -> EntityType.Builder.of(FanaticEntity::new, EntityClassification.MONSTER)
                    .canSpawnFarFromPlayer()
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "fanatic").toString()));

    public static final RegistryObject<EntityType<ZealotEntity>> ZEALOT = ENTITY_TYPES.register("zealot",
            () -> EntityType.Builder.of(ZealotEntity::new, EntityClassification.MONSTER)
                    .canSpawnFarFromPlayer()
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "zealot").toString()));

    public static final RegistryObject<EntityType<ThugEntity>> THUG = ENTITY_TYPES.register("thug",
            () -> EntityType.Builder.of(ThugEntity::new, EntityClassification.MONSTER)
                    .canSpawnFarFromPlayer()
                    .sized(1.4F, 2.7F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(Goety.MOD_ID, "thug").toString()));

    public static final RegistryObject<EntityType<CrimsonSpiderEntity>> CRIMSON_SPIDER = ENTITY_TYPES.register("crimson_spider",
            () -> EntityType.Builder.of(CrimsonSpiderEntity::new, EntityClassification.MONSTER)
                    .sized(1.4F, 0.9F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "crimson_spider").toString()));

    public static final RegistryObject<EntityType<DiscipleEntity>> DISCIPLE = ENTITY_TYPES.register("disciple",
            () -> EntityType.Builder.of(DiscipleEntity::new, EntityClassification.MONSTER)
                    .canSpawnFarFromPlayer()
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "disciple").toString()));

    public static final RegistryObject<EntityType<ApostleEntity>> APOSTLE = ENTITY_TYPES.register("apostle",
            () -> EntityType.Builder.of(ApostleEntity::new, EntityClassification.MONSTER)
                    .canSpawnFarFromPlayer()
                    .fireImmune()
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "apostle").toString()));

    public static final RegistryObject<EntityType<ZombieVillagerMinionEntity>> ZOMBIE_VILLAGER_MINION = ENTITY_TYPES.register("zombievillagerminion",
            () -> EntityType.Builder.of(ZombieVillagerMinionEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "zombievillagerminion").toString()));

    public static final RegistryObject<EntityType<SkeletonVillagerMinionEntity>> SKELETON_VILLAGER_MINION = ENTITY_TYPES.register("skeletonvillagerminion",
            () -> EntityType.Builder.of(SkeletonVillagerMinionEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "skeletonvillagerminion").toString()));

    public static final RegistryObject<EntityType<ZPiglinMinionEntity>> ZPIGLIN_MINION = ENTITY_TYPES.register("zpiglinminion",
            () -> EntityType.Builder.of(ZPiglinMinionEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .fireImmune()
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "zpiglinminion").toString()));

    public static final RegistryObject<EntityType<ZPiglinBruteMinionEntity>> ZPIGLIN_BRUTE_MINION = ENTITY_TYPES.register("zpiglinbruteminion",
            () -> EntityType.Builder.of(ZPiglinBruteMinionEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .fireImmune()
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "zpiglinbruteminion").toString()));

    public static final RegistryObject<EntityType<IllusionCloneEntity>> ILLUSION_CLONE = ENTITY_TYPES.register("illusion_clone",
            () -> EntityType.Builder.of(IllusionCloneEntity::new, EntityClassification.MISC)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .fireImmune()
                    .build(new ResourceLocation(Goety.MOD_ID, "illusion_clone").toString()));

    public static final RegistryObject<EntityType<EnviokerEntity>> ENVIOKER = ENTITY_TYPES.register("envioker",
            () -> EntityType.Builder.of(EnviokerEntity::new, EntityClassification.MONSTER)
                    .canSpawnFarFromPlayer()
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "envioker").toString()));

    public static final RegistryObject<EntityType<InquillagerEntity>> INQUILLAGER = ENTITY_TYPES.register("inquillager",
            () -> EntityType.Builder.of(InquillagerEntity::new, EntityClassification.MONSTER)
                    .canSpawnFarFromPlayer()
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "inquillager").toString()));

    public static final RegistryObject<EntityType<ConquillagerEntity>> CONQUILLAGER = ENTITY_TYPES.register("conquillager",
            () -> EntityType.Builder.of(ConquillagerEntity::new, EntityClassification.MONSTER)
                    .canSpawnFarFromPlayer()
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "conquillager").toString()));

    public static final RegistryObject<EntityType<TormentorEntity>> TORMENTOR = ENTITY_TYPES.register("tormentor",
            () -> EntityType.Builder.of(TormentorEntity::new, EntityClassification.MONSTER)
                    .fireImmune()
                    .canSpawnFarFromPlayer()
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "tormentor").toString()));

    public static final RegistryObject<EntityType<HuskarlEntity>> HUSKARL = ENTITY_TYPES.register("huskarl",
            () -> EntityType.Builder.of(HuskarlEntity::new, EntityClassification.MONSTER)
                    .canSpawnFarFromPlayer()
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "huskarl").toString()));

    public static final RegistryObject<EntityType<ShadeEntity>> SHADE = ENTITY_TYPES.register("shade",
            () -> EntityType.Builder.of(ShadeEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "shade").toString()));

    public static final RegistryObject<EntityType<BoomerEntity>> BOOMER = ENTITY_TYPES.register("boomer",
            () -> EntityType.Builder.of(BoomerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.7F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "boomer").toString()));

    public static final RegistryObject<EntityType<DuneSpiderEntity>> DUNE_SPIDER = ENTITY_TYPES.register("dune_spider",
            () -> EntityType.Builder.of(DuneSpiderEntity::new, EntityClassification.MONSTER)
                    .sized(1.4F, 0.9F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "dune_spider").toString()));

    public static final RegistryObject<EntityType<MutatedCowEntity>> MUTATED_COW = ENTITY_TYPES.register("mutatedcow",
            () -> EntityType.Builder.of(MutatedCowEntity::new, EntityClassification.MONSTER)
                    .sized(0.9F, 1.95F)
                    .build(new ResourceLocation(Goety.MOD_ID, "mutatedcow").toString()));

    public static final RegistryObject<EntityType<MutatedChickenEntity>> MUTATED_CHICKEN = ENTITY_TYPES.register("mutatedchicken",
            () -> EntityType.Builder.of(MutatedChickenEntity::new, EntityClassification.MONSTER)
                    .sized(0.5F, 1.0F)
                    .build(new ResourceLocation(Goety.MOD_ID, "mutatedchicken").toString()));

    public static final RegistryObject<EntityType<MutatedSheepEntity>> MUTATED_SHEEP = ENTITY_TYPES.register("mutatedsheep",
            () -> EntityType.Builder.of(MutatedSheepEntity::new, EntityClassification.MONSTER)
                    .sized(0.9F, 1.95F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(Goety.MOD_ID, "mutatedsheep").toString()));

    public static final RegistryObject<EntityType<MutatedPigEntity>> MUTATED_PIG = ENTITY_TYPES.register("mutatedpig",
            () -> EntityType.Builder.of(MutatedPigEntity::new, EntityClassification.MONSTER)
                    .sized(0.9F, 1.75F)
                    .build(new ResourceLocation(Goety.MOD_ID, "mutatedpig").toString()));

    public static final RegistryObject<EntityType<MutatedRabbitEntity>> MUTATED_RABBIT = ENTITY_TYPES.register("mutatedrabbit",
            () -> EntityType.Builder.of(MutatedRabbitEntity::new, EntityClassification.MONSTER)
                    .sized(0.8F, 1.0F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "mutatedrabbit").toString()));

    public static final RegistryObject<EntityType<SacredFishEntity>> SACRED_FISH = ENTITY_TYPES.register("sacredfish",
            () -> EntityType.Builder.of(SacredFishEntity::new, EntityClassification.MISC)
                    .sized(0.7F, 0.4F)
                    .clientTrackingRange(4)
                    .build(new ResourceLocation(Goety.MOD_ID, "sacredfish").toString()));

    public static final RegistryObject<EntityType<ParasiteEntity>> PARASITE = ENTITY_TYPES.register("parasite",
            () -> EntityType.Builder.of(ParasiteEntity::new, EntityClassification.MONSTER)
                    .sized(0.4F, 0.3F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "parasite").toString()));

    public static final RegistryObject<EntityType<FriendlyVexEntity>> FRIENDLY_VEX = ENTITY_TYPES.register("friendly_vex",
            () -> EntityType.Builder.of(FriendlyVexEntity::new, EntityClassification.MONSTER)
                    .fireImmune()
                    .sized(0.4F, 0.8F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "friendly_vex").toString()));

    public static final RegistryObject<EntityType<FriendlyScorchEntity>> FRIENDLY_SCORCH = ENTITY_TYPES.register("friendly_scorch",
            () -> EntityType.Builder.of(FriendlyScorchEntity::new, EntityClassification.MONSTER)
                    .fireImmune()
                    .sized(0.4F, 0.8F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "friendly_scorch").toString()));

    public static final RegistryObject<EntityType<ZombieMinionEntity>> ZOMBIE_MINION = ENTITY_TYPES.register("zombie_minion",
            () -> EntityType.Builder.of(ZombieMinionEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "zombie_minion").toString()));

    public static final RegistryObject<EntityType<SkeletonMinionEntity>> SKELETON_MINION = ENTITY_TYPES.register("skeleton_minion",
            () -> EntityType.Builder.of(SkeletonMinionEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "skeleton_minion").toString()));

    public static final RegistryObject<EntityType<FarmerMinionEntity>> FARMER_MINION = ENTITY_TYPES.register("farmer_minion",
            () -> EntityType.Builder.of(FarmerMinionEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "farmer_minion").toString()));

    public static final RegistryObject<EntityType<SpiderlingMinionEntity>> SPIDERLING_MINION = ENTITY_TYPES.register("spiderling_minion",
            () -> EntityType.Builder.of(SpiderlingMinionEntity::new, EntityClassification.MONSTER)
                    .sized(0.4F, 0.2F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "spiderling_minion").toString()));

    public static final RegistryObject<EntityType<CreeperlingMinionEntity>> CREEPERLING_MINION = ENTITY_TYPES.register("creeperling_minion",
            () -> EntityType.Builder.of(CreeperlingMinionEntity::new, EntityClassification.MONSTER)
                    .sized(0.4F, 0.2F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "creeperling_minion").toString()));

    public static final RegistryObject<EntityType<LoyalSpiderEntity>> TAMED_SPIDER = ENTITY_TYPES.register("tamed_spider",
            () -> EntityType.Builder.of(LoyalSpiderEntity::new, EntityClassification.MONSTER)
                    .sized(1.4F, 0.9F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "tamed_spider").toString()));

    public static final RegistryObject<EntityType<VizierEntity>> VIZIER = ENTITY_TYPES.register("vizier",
            () -> EntityType.Builder.of(VizierEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "vizier").toString()));

    public static final RegistryObject<EntityType<IrkEntity>> IRK = ENTITY_TYPES.register("irk",
            () -> EntityType.Builder.of(IrkEntity::new, EntityClassification.MONSTER)
                    .fireImmune()
                    .sized(0.4F, 0.8F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "irk").toString()));

    public static final RegistryObject<EntityType<ScorchEntity>> SCORCH = ENTITY_TYPES.register("scorch",
            () -> EntityType.Builder.of(ScorchEntity::new, EntityClassification.MONSTER)
                    .fireImmune()
                    .sized(0.4F, 0.8F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "scorch").toString()));

    public static final RegistryObject<EntityType<NethernalEntity>> NETHERNAL = ENTITY_TYPES.register("nethernal",
            () -> EntityType.Builder.of(NethernalEntity::new, EntityClassification.MONSTER)
                    .fireImmune()
                    .sized(1.4F, 2.7F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(Goety.MOD_ID, "netheruin").toString()));

    public static final RegistryObject<EntityType<PenanceEntity>> PENANCE = ENTITY_TYPES.register("penance",
            () -> EntityType.Builder.of(PenanceEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "penance").toString()));

    public static final RegistryObject<EntityType<LightningTrapEntity>> LIGHTNINGTRAP = ENTITY_TYPES.register("lightningtrap",
            () -> EntityType.Builder.<LightningTrapEntity>of(LightningTrapEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(2.0F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build(new ResourceLocation(Goety.MOD_ID, "lightningtrap").toString()));

    public static final RegistryObject<EntityType<FireRainTrapEntity>> FIRERAINTRAP = ENTITY_TYPES.register("fireraintrap",
            () -> EntityType.Builder.<FireRainTrapEntity>of(FireRainTrapEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(2.0F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build(new ResourceLocation(Goety.MOD_ID, "fireraintrap").toString()));

    public static final RegistryObject<EntityType<FireTornadoTrapEntity>> FIRETORNADOTRAP = ENTITY_TYPES.register("firetornadotrap",
            () -> EntityType.Builder.<FireTornadoTrapEntity>of(FireTornadoTrapEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(2.0F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build(new ResourceLocation(Goety.MOD_ID, "fireraintrap").toString()));

    public static final RegistryObject<EntityType<StormEntity>> STORMUTIL = ENTITY_TYPES.register("stormutil",
            () -> EntityType.Builder.of(StormEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build(new ResourceLocation(Goety.MOD_ID, "stormutil").toString()));

    public static final RegistryObject<EntityType<SummonApostleTrapEntity>> SUMMON_APOSTLE = ENTITY_TYPES.register("summon_apostle",
            () -> EntityType.Builder.of(SummonApostleTrapEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(2.0F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build(new ResourceLocation(Goety.MOD_ID, "summon_apostle").toString()));

}