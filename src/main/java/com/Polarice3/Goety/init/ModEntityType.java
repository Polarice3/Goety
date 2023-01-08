package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.ally.*;
import com.Polarice3.Goety.common.entities.bosses.ApostleEntity;
import com.Polarice3.Goety.common.entities.bosses.VizierEntity;
import com.Polarice3.Goety.common.entities.hostile.*;
import com.Polarice3.Goety.common.entities.hostile.cultists.*;
import com.Polarice3.Goety.common.entities.hostile.dead.*;
import com.Polarice3.Goety.common.entities.hostile.illagers.ConquillagerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.EnviokerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.InquillagerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.TormentorEntity;
import com.Polarice3.Goety.common.entities.items.ModBoatEntity;
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

    public static final RegistryObject<EntityType<WitchBombEntity>> WITCHBOMB = ENTITY_TYPES.register("witchbomb",
            () -> EntityType.Builder.<WitchBombEntity>of(WitchBombEntity::new, EntityClassification.MISC)
                    .sized(0.25f,0.25f)
                    .clientTrackingRange(4)
                    .build(new ResourceLocation(Goety.MOD_ID, "witchbomb").toString()));

    public static final RegistryObject<EntityType<BurningPotionEntity>> BURNING_POTION = ENTITY_TYPES.register("burning_potion",
            () -> EntityType.Builder.<BurningPotionEntity>of(BurningPotionEntity::new, EntityClassification.MISC)
                    .sized(0.25f,0.25f)
                    .clientTrackingRange(4)
                    .build(new ResourceLocation(Goety.MOD_ID, "burning_potion").toString()));

    public static final RegistryObject<EntityType<NetherMeteorEntity>> NETHER_METEOR = ENTITY_TYPES.register("nether_meteor",
            () -> EntityType.Builder.<NetherMeteorEntity>of(NetherMeteorEntity::new, EntityClassification.MISC)
                    .sized(1.0f,1.0f)
                    .clientTrackingRange(4)
                    .build(new ResourceLocation(Goety.MOD_ID, "nether_meteor").toString()));

    public static final RegistryObject<EntityType<ModFireballEntity>> MOD_FIREBALL = ENTITY_TYPES.register("fireball",
            () -> EntityType.Builder.<ModFireballEntity>of(ModFireballEntity::new, EntityClassification.MISC)
                    .sized(0.3125F, 0.3125F)
                    .clientTrackingRange(4)
                    .build(new ResourceLocation(Goety.MOD_ID, "fireball").toString()));

    public static final RegistryObject<EntityType<LavaballEntity>> LAVABALL = ENTITY_TYPES.register("lavaball",
            () -> EntityType.Builder.<LavaballEntity>of(LavaballEntity::new, EntityClassification.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(4)
                    .build(new ResourceLocation(Goety.MOD_ID, "lavaball").toString()));

    public static final RegistryObject<EntityType<GrandLavaballEntity>> GRAND_LAVABALL = ENTITY_TYPES.register("grand_lavaball",
            () -> EntityType.Builder.<GrandLavaballEntity>of(GrandLavaballEntity::new, EntityClassification.MISC)
                    .sized(1.0f,1.0f)
                    .clientTrackingRange(4)
                    .build(new ResourceLocation(Goety.MOD_ID, "grand_lavaball").toString()));

    public static final RegistryObject<EntityType<ModDragonFireballEntity>> MOD_DRAGON_FIREBALL = ENTITY_TYPES.register("dragon_fireball",
            () -> EntityType.Builder.<ModDragonFireballEntity>of(ModDragonFireballEntity::new, EntityClassification.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(4)
                    .build(new ResourceLocation(Goety.MOD_ID, "dragon_fireball").toString()));

    public static final RegistryObject<EntityType<FrostBallEntity>> FROST_BALL = ENTITY_TYPES.register("frost_ball",
            () -> EntityType.Builder.<FrostBallEntity>of(FrostBallEntity::new, EntityClassification.MISC)
                    .sized(0.3125F, 0.3125F)
                    .clientTrackingRange(4)
                    .build(new ResourceLocation(Goety.MOD_ID, "frost_ball").toString()));

    public static final RegistryObject<EntityType<DeadSlimeBallEntity>> DEAD_SLIME_BALL = ENTITY_TYPES.register("dead_slime_ball",
            () -> EntityType.Builder.<DeadSlimeBallEntity>of(DeadSlimeBallEntity::new, EntityClassification.MISC)
                    .sized(0.25f,0.25f)
                    .clientTrackingRange(4)
                    .build(new ResourceLocation(Goety.MOD_ID, "dead_slime_ball").toString()));

    public static final RegistryObject<EntityType<SwordProjectileEntity>> SWORD = ENTITY_TYPES.register("sword",
            () -> EntityType.Builder.<SwordProjectileEntity>of(SwordProjectileEntity::new, EntityClassification.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .build(new ResourceLocation(Goety.MOD_ID, "sword").toString()));

    public static final RegistryObject<EntityType<ScytheProjectileEntity>> SCYTHE = ENTITY_TYPES.register("scythe",
            () -> EntityType.Builder.<ScytheProjectileEntity>of(ScytheProjectileEntity::new, EntityClassification.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .build(new ResourceLocation(Goety.MOD_ID, "scythe").toString()));

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

    public static final RegistryObject<EntityType<SoulSkullEntity>> SOULSKULL = ENTITY_TYPES.register("soulskull",
            () -> EntityType.Builder.<SoulSkullEntity>of(SoulSkullEntity::new, EntityClassification.MISC)
                    .sized(0.3125F, 0.3125F)
                    .clientTrackingRange(4)
                    .updateInterval(1)
                    .build(new ResourceLocation(Goety.MOD_ID, "soulskull").toString()));

    public static final RegistryObject<EntityType<DesiccatedSkullEntity>> DESICCATEDSKULL = ENTITY_TYPES.register("corruptskull",
            () -> EntityType.Builder.<DesiccatedSkullEntity>of(DesiccatedSkullEntity::new, EntityClassification.MISC)
                    .sized(0.3125F, 0.3125F)
                    .clientTrackingRange(4)
                    .updateInterval(1)
                    .build(new ResourceLocation(Goety.MOD_ID, "corruptskull").toString()));

    public static final RegistryObject<EntityType<DeadTNTEntity>> DEAD_TNT = ENTITY_TYPES.register("dead_tnt",
            () -> EntityType.Builder.<DeadTNTEntity>of(DeadTNTEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(10)
                    .updateInterval(1)
                    .build(new ResourceLocation(Goety.MOD_ID, "dead_tnt").toString()));

    public static final RegistryObject<EntityType<IceStormEntity>> ICE_STORM = ENTITY_TYPES.register("ice_storm",
            () -> EntityType.Builder.<IceStormEntity>of(IceStormEntity::new, EntityClassification.MISC)
                    .sized(2.0F, 2.0F)
                    .clientTrackingRange(4)
                    .updateInterval(1)
                    .build(new ResourceLocation(Goety.MOD_ID, "ice_storm").toString()));

    public static final RegistryObject<EntityType<SoulLightEntity>> SOUL_LIGHT = ENTITY_TYPES.register("soul_light",
            () -> EntityType.Builder.<SoulLightEntity>of(SoulLightEntity::new, EntityClassification.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(1)
                    .build(new ResourceLocation(Goety.MOD_ID, "soul_light").toString()));

    public static final RegistryObject<EntityType<GlowLightEntity>> GLOW_LIGHT = ENTITY_TYPES.register("glow_light",
            () -> EntityType.Builder.<GlowLightEntity>of(GlowLightEntity::new, EntityClassification.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(1)
                    .build(new ResourceLocation(Goety.MOD_ID, "glow_light").toString()));

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

    public static final RegistryObject<EntityType<SpikeEntity>> SPIKE = ENTITY_TYPES.register("spike",
            () -> EntityType.Builder.<SpikeEntity>of(SpikeEntity::new, EntityClassification.MISC)
                    .sized(0.5F, 0.8F)
                    .clientTrackingRange(6)
                    .updateInterval(2)
                    .build(new ResourceLocation(Goety.MOD_ID, "spike").toString()));

    public static final RegistryObject<EntityType<WitchGaleEntity>> WITCHGALE = ENTITY_TYPES.register("witchgale",
            () -> EntityType.Builder.<WitchGaleEntity>of(WitchGaleEntity::new, EntityClassification.MISC)
                    .sized(1.0F,1.0F)
                    .clientTrackingRange(4)
                    .updateInterval(1)
                    .build(new ResourceLocation(Goety.MOD_ID, "witchgale").toString()));

    public static final RegistryObject<EntityType<FireTornadoEntity>> FIRETORNADO = ENTITY_TYPES.register("fire_tornado",
            () -> EntityType.Builder.<FireTornadoEntity>of(FireTornadoEntity::new, EntityClassification.MISC)
                    .sized(2.0F, 2.0F)
                    .clientTrackingRange(4)
                    .updateInterval(1)
                    .build(new ResourceLocation(Goety.MOD_ID, "fire_tornado").toString()));

    public static final RegistryObject<EntityType<ModBoatEntity>> MOD_BOAT = ENTITY_TYPES.register("boat",
            () -> EntityType.Builder.<ModBoatEntity>of(ModBoatEntity::new, EntityClassification.MISC)
                    .sized(1.375F, 0.5625F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(Goety.MOD_ID, "boat").toString()));

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

    public static final RegistryObject<EntityType<BeldamEntity>> BELDAM = ENTITY_TYPES.register("beldam",
            () -> EntityType.Builder.of(BeldamEntity::new, EntityClassification.MONSTER)
                    .canSpawnFarFromPlayer()
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "beldam").toString()));

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

    public static final RegistryObject<EntityType<ReturnedEntity>> RETURNED = ENTITY_TYPES.register("returned",
            () -> EntityType.Builder.of(ReturnedEntity::new, EntityClassification.MONSTER)
                    .canSpawnFarFromPlayer()
                    .sized(0.6F, 1.95F)
                    .fireImmune()
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "returned").toString()));

    public static final RegistryObject<EntityType<MalghastEntity>> MALGHAST = ENTITY_TYPES.register("malghast",
            () -> EntityType.Builder.of(MalghastEntity::new, EntityClassification.MONSTER)
                    .sized(2.0F, 2.0F)
                    .fireImmune()
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(Goety.MOD_ID, "malghast").toString()));

    public static final RegistryObject<EntityType<HogLordEntity>> HOGLORD = ENTITY_TYPES.register("hoglord",
            () -> EntityType.Builder.of(HogLordEntity::new, EntityClassification.MONSTER)
                    .sized(2.0947266F, 2.1F)
                    .fireImmune()
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "hoglord").toString()));

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

    public static final RegistryObject<EntityType<DredenEntity>> DREDEN = ENTITY_TYPES.register("dreden",
            () -> EntityType.Builder.of(DredenEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "dreden").toString()));

    public static final RegistryObject<EntityType<UrbhadhachEntity>> URBHADHACH = ENTITY_TYPES.register("urbhadhach",
            () -> EntityType.Builder.of(UrbhadhachEntity::new, EntityClassification.MONSTER)
                    .sized(1.4F, 1.4F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(Goety.MOD_ID, "urbhadhach").toString()));

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

    public static final RegistryObject<EntityType<FallenEntity>> FALLEN = ENTITY_TYPES.register("fallen",
            () -> EntityType.Builder.of(FallenEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "fallen").toString()));

    public static final RegistryObject<EntityType<DesiccatedEntity>> DESICCATED = ENTITY_TYPES.register("desiccated",
            () -> EntityType.Builder.of(DesiccatedEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "desiccated").toString()));

    public static final RegistryObject<EntityType<MarcireEntity>> MARCIRE = ENTITY_TYPES.register("marcire",
            () -> EntityType.Builder.of(MarcireEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "marcire").toString()));

    public static final RegistryObject<EntityType<LocustEntity>> LOCUST = ENTITY_TYPES.register("locust",
            () -> EntityType.Builder.of(LocustEntity::new, EntityClassification.MONSTER)
                    .sized(0.7F, 0.6F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "locust").toString()));

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
            () -> EntityType.Builder.of(SacredFishEntity::new, EntityClassification.WATER_AMBIENT)
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

    public static final RegistryObject<EntityType<DrownedMinionEntity>> DROWNED_MINION = ENTITY_TYPES.register("drowned_minion",
            () -> EntityType.Builder.of(DrownedMinionEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "drowned_minion").toString()));

    public static final RegistryObject<EntityType<SkeletonMinionEntity>> SKELETON_MINION = ENTITY_TYPES.register("skeleton_minion",
            () -> EntityType.Builder.of(SkeletonMinionEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "skeleton_minion").toString()));

    public static final RegistryObject<EntityType<StrayMinionEntity>> STRAY_MINION = ENTITY_TYPES.register("stray_minion",
            () -> EntityType.Builder.of(StrayMinionEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "stray_minion").toString()));

    public static final RegistryObject<EntityType<DredenMinionEntity>> DREDEN_MINION = ENTITY_TYPES.register("dreden_minion",
            () -> EntityType.Builder.of(DredenMinionEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "dreden_minion").toString()));

    public static final RegistryObject<EntityType<FarmerMinionEntity>> FARMER_MINION = ENTITY_TYPES.register("farmer_minion",
            () -> EntityType.Builder.of(FarmerMinionEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "farmer_minion").toString()));

    public static final RegistryObject<EntityType<UndeadWolfEntity>> UNDEAD_WOLF_MINION = ENTITY_TYPES.register("undead_wolf",
            () -> EntityType.Builder.of(UndeadWolfEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 0.85F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(Goety.MOD_ID, "undead_wolf").toString()));

    public static final RegistryObject<EntityType<PhantomMinionEntity>> PHANTOM_MINION = ENTITY_TYPES.register("phantom_minion",
            () -> EntityType.Builder.of(PhantomMinionEntity::new, EntityClassification.MONSTER)
                    .sized(0.9F, 0.5F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "phantom_minion").toString()));

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

    public static final RegistryObject<EntityType<FelFlyEntity>> FEL_FLY = ENTITY_TYPES.register("fel_fly",
            () -> EntityType.Builder.of(FelFlyEntity::new, EntityClassification.MONSTER)
                    .sized(0.45F, 0.3F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "fel_fly").toString()));

    public static final RegistryObject<EntityType<RottreantEntity>> ROT_TREE = ENTITY_TYPES.register("rottreant",
            () -> EntityType.Builder.of(RottreantEntity::new, EntityClassification.MONSTER)
                    .fireImmune()
                    .sized(1.4F, 2.7F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(Goety.MOD_ID, "rottreant").toString()));

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

    public static final RegistryObject<EntityType<SkullLordEntity>> SKULL_LORD = ENTITY_TYPES.register("skull_lord",
            () -> EntityType.Builder.of(SkullLordEntity::new, EntityClassification.MONSTER)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "skull_lord").toString()));

    public static final RegistryObject<EntityType<BoneLordEntity>> BONE_LORD = ENTITY_TYPES.register("bone_lord",
            () -> EntityType.Builder.of(BoneLordEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(Goety.MOD_ID, "bone_lord").toString()));

    public static final RegistryObject<EntityType<SentinelEntity>> SENTINEL = ENTITY_TYPES.register("sentinel",
            () -> EntityType.Builder.of(SentinelEntity::new, EntityClassification.MONSTER)
                    .fireImmune()
                    .sized(1.4F, 2.7F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(Goety.MOD_ID, "sentinel").toString()));

    public static final RegistryObject<EntityType<LightningTrapEntity>> LIGHTNINGTRAP = ENTITY_TYPES.register("lightningtrap",
            () -> EntityType.Builder.<LightningTrapEntity>of(LightningTrapEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(1.0F, 0.5F)
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

    public static final RegistryObject<EntityType<ArrowRainTrapEntity>> ARROWRAINTRAP = ENTITY_TYPES.register("arrowraintrap",
            () -> EntityType.Builder.<ArrowRainTrapEntity>of(ArrowRainTrapEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(2.0F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build(new ResourceLocation(Goety.MOD_ID, "arrowraintrap").toString()));

    public static final RegistryObject<EntityType<FireTornadoTrapEntity>> FIRETORNADOTRAP = ENTITY_TYPES.register("firetornadotrap",
            () -> EntityType.Builder.<FireTornadoTrapEntity>of(FireTornadoTrapEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(2.0F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build(new ResourceLocation(Goety.MOD_ID, "fireraintrap").toString()));

    public static final RegistryObject<EntityType<FireBlastTrapEntity>> FIREBLASTTRAP = ENTITY_TYPES.register("fireblasttrap",
            () -> EntityType.Builder.<FireBlastTrapEntity>of(FireBlastTrapEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(1.0F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build(new ResourceLocation(Goety.MOD_ID, "fireblasttrap").toString()));

    public static final RegistryObject<EntityType<MagicBlastTrapEntity>> MAGICBLASTTRAP = ENTITY_TYPES.register("magicblasttrap",
            () -> EntityType.Builder.<MagicBlastTrapEntity>of(MagicBlastTrapEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(1.0F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build(new ResourceLocation(Goety.MOD_ID, "magicblasttrap").toString()));

    public static final RegistryObject<EntityType<BurningGroundEntity>> BURNING_GROUND = ENTITY_TYPES.register("burning_ground",
            () -> EntityType.Builder.<BurningGroundEntity>of(BurningGroundEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(6.0F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build(new ResourceLocation(Goety.MOD_ID, "burning_ground").toString()));

    public static final RegistryObject<EntityType<PoisonGroundEntity>> POISON_GROUND = ENTITY_TYPES.register("poison_ground",
            () -> EntityType.Builder.<PoisonGroundEntity>of(PoisonGroundEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(6.0F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build(new ResourceLocation(Goety.MOD_ID, "poison_ground").toString()));

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

    public static final RegistryObject<EntityType<LaserEntity>> LASER = ENTITY_TYPES.register("laser",
            () -> EntityType.Builder.of(LaserEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(Goety.MOD_ID, "laser").toString()));

}