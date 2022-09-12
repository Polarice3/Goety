package com.Polarice3.Goety.common.world.structures.pieces;

import com.Polarice3.Goety.common.blocks.FalsePortalBlock;
import com.Polarice3.Goety.common.world.features.template.BlockMossRuinProcessor;
import com.Polarice3.Goety.common.world.features.template.BlockRuinessProcessor;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.ModLootTables;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.ZoglinEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;

public class RuinedRitualPiece extends TemplateStructurePiece {
    private static final Logger LOGGER = LogManager.getLogger();
    private final ResourceLocation templateLocation;
    private final Rotation rotation;
    private final Mirror mirror;
    private final RuinedRitualPiece.Serializer properties;

    public RuinedRitualPiece(BlockPos p_i232111_1_, RuinedRitualPiece.Serializer p_i232111_3_, ResourceLocation p_i232111_4_, Template p_i232111_5_, Rotation p_i232111_6_, Mirror p_i232111_7_, BlockPos p_i232111_8_) {
        super(IModPieceType.RUINED_RITUAL_PIECE, 0);
        this.templatePosition = p_i232111_1_;
        this.templateLocation = p_i232111_4_;
        this.rotation = p_i232111_6_;
        this.mirror = p_i232111_7_;
        this.properties = p_i232111_3_;
        this.loadTemplate(p_i232111_5_, p_i232111_8_);
    }

    public RuinedRitualPiece(TemplateManager p_i232110_1_, CompoundNBT p_i232110_2_) {
        super(IModPieceType.RUINED_RITUAL_PIECE, p_i232110_2_);
        this.templateLocation = new ResourceLocation(p_i232110_2_.getString("Template"));
        this.rotation = Rotation.valueOf(p_i232110_2_.getString("Rotation"));
        this.mirror = Mirror.valueOf(p_i232110_2_.getString("Mirror"));
        this.properties = RuinedRitualPiece.Serializer.CODEC.parse(new Dynamic<>(NBTDynamicOps.INSTANCE, p_i232110_2_.get("Properties"))).getOrThrow(true, LOGGER::error);
        Template template = p_i232110_1_.getOrCreate(this.templateLocation);
        this.loadTemplate(template, new BlockPos(template.getSize().getX() / 2, 0, template.getSize().getZ() / 2));
    }

    protected void addAdditionalSaveData(CompoundNBT p_143011_1_) {
        super.addAdditionalSaveData(p_143011_1_);
        p_143011_1_.putString("Template", this.templateLocation.toString());
        p_143011_1_.putString("Rotation", this.rotation.name());
        p_143011_1_.putString("Mirror", this.mirror.name());
        RuinedRitualPiece.Serializer.CODEC.encodeStart(NBTDynamicOps.INSTANCE, this.properties).resultOrPartial(LOGGER::error).ifPresent((p_237018_1_) -> {
            p_143011_1_.put("Properties", p_237018_1_);
        });
    }

    private void loadTemplate(Template p_237014_1_, BlockPos p_237014_2_) {
        BlockIgnoreStructureProcessor blockignorestructureprocessor = BlockIgnoreStructureProcessor.STRUCTURE_AND_AIR;
        List<RuleEntry> list = Lists.newArrayList();
        list.add(getBlockReplaceRule(Blocks.GRASS_BLOCK, 0.07F, Blocks.NETHERRACK));
        list.add(this.getLavaProcessorRule());
        if (!this.properties.cold) {
            list.add(getBlockReplaceRule(Blocks.NETHERRACK, 0.07F, Blocks.MAGMA_BLOCK));
        }

        PlacementSettings placementsettings = (new PlacementSettings().setIgnoreEntities(true)).setRotation(this.rotation).setMirror(this.mirror).setRotationPivot(p_237014_2_).addProcessor(blockignorestructureprocessor).addProcessor(new RuleStructureProcessor(list)).addProcessor(new BlockRuinessProcessor(this.properties.mossiness)).addProcessor(new LavaSubmergingProcessor());

        if (this.properties.overgrown){
            placementsettings = (new PlacementSettings().setIgnoreEntities(true)).setRotation(this.rotation).setMirror(this.mirror).setRotationPivot(p_237014_2_).addProcessor(blockignorestructureprocessor).addProcessor(new RuleStructureProcessor(list)).addProcessor(new BlockMossRuinProcessor(this.properties.mossiness)).addProcessor(new LavaSubmergingProcessor());
        }

        this.setup(p_237014_1_, this.templatePosition, placementsettings);
    }

    private RuleEntry getLavaProcessorRule() {
        return this.properties.cold ? getBlockReplaceRule(Blocks.LAVA, Blocks.NETHERRACK) : getBlockReplaceRule(Blocks.LAVA, 0.2F, Blocks.MAGMA_BLOCK);
    }

    public boolean postProcess(ISeedReader pLevel, StructureManager pStructureManager, ChunkGenerator pChunkGenerator, Random pRandom, MutableBoundingBox pBox, ChunkPos pChunkPos, BlockPos pPos) {
        if (!pBox.isInside(this.templatePosition)){
            return true;
        } else {
            pBox.expand(this.template.getBoundingBox(this.placeSettings, this.templatePosition));
            BlockPos original = this.templatePosition;
            this.templatePosition = original.above(-1);
            boolean flag = super.postProcess(pLevel, pStructureManager, pChunkGenerator, pRandom, pBox, pChunkPos, pPos);
            this.spreadNetherrack(pRandom, pLevel);
            this.addNetherrackDripColumnsBelowPortal(pRandom, pLevel);
            BlockPos.betweenClosedStream(this.getBoundingBox()).forEach((blockPos) -> {
                this.maybeAddObsidianAbove(pRandom, pLevel, blockPos);
            });
            if (this.properties.vines || this.properties.overgrown) {
                BlockPos.betweenClosedStream(this.getBoundingBox()).forEach((blockPos) -> {
                    if (this.properties.vines) {
                        this.maybeAddVines(pRandom, pLevel, blockPos);
                    }

                    if (this.properties.overgrown) {
                        this.maybeAddLeavesAbove(pRandom, pLevel, blockPos);
                    }

                });
            }

            return flag;
        }
    }

    protected void handleDataMarker(String pFunction, BlockPos pPos, IServerWorld pLevel, Random pRandom, MutableBoundingBox pSbb) {
        if (pFunction.startsWith("treasure")) {
            BlockState blockstate = Blocks.CHEST.defaultBlockState();
            ResourceLocation loot = LootTables.RUINED_PORTAL;
            this.createChest(pLevel, pSbb, pRandom, pPos, loot, blockstate);
        } else {
            if (pFunction.startsWith("portal")){
                BlockState blockstate = ModBlocks.FALSE_PORTAL.get().defaultBlockState();
                if (this.rotation == Rotation.NONE || this.rotation == Rotation.CLOCKWISE_180){
                    blockstate = blockstate.setValue(FalsePortalBlock.AXIS, Direction.Axis.X);
                } else {
                    blockstate = blockstate.setValue(FalsePortalBlock.AXIS, Direction.Axis.Z);
                }
                pLevel.setBlock(pPos, blockstate, 2);
            }
            if (pFunction.startsWith("barrel")){
                if (pRandom.nextFloat() <= 0.25F){
                    pLevel.setBlock(pPos, Blocks.COARSE_DIRT.defaultBlockState(), 2);
                } else {
                    BlockState blockstate = Blocks.BARREL.defaultBlockState();
                    blockstate = blockstate.setValue(BarrelBlock.FACING, rotation.rotate(Direction.UP));
                    ResourceLocation loot = ModLootTables.RUINED_RITUAL_BARREL;
                    BlockFinder.createBarrel(pLevel, pSbb, pRandom, pPos, loot, blockstate);
                }
            }
            if (pFunction.startsWith("zoglin")) {
                ZoglinEntity zoglinEntity = EntityType.ZOGLIN.create(pLevel.getLevel());
                if (zoglinEntity != null) {
                    zoglinEntity.setPersistenceRequired();
                    zoglinEntity.moveTo(pPos, 0.0F, 0.0F);
                    zoglinEntity.finalizeSpawn(pLevel, pLevel.getCurrentDifficultyAt(pPos), SpawnReason.STRUCTURE, (ILivingEntityData) null, (CompoundNBT) null);
                    pLevel.addFreshEntityWithPassengers(zoglinEntity);
                    pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 2);
                    pLevel.setBlock(pPos.above(), Blocks.AIR.defaultBlockState(), 2);
                }
                if (pLevel.getRandom().nextFloat() < 0.25F){
                    ZoglinEntity zoglinEntity2 = EntityType.ZOGLIN.create(pLevel.getLevel());
                    if (zoglinEntity2 != null) {
                        zoglinEntity2.setPersistenceRequired();
                        zoglinEntity2.moveTo(pPos.east(), 0.0F, 0.0F);
                        zoglinEntity2.finalizeSpawn(pLevel, pLevel.getCurrentDifficultyAt(pPos), SpawnReason.STRUCTURE, (ILivingEntityData) null, (CompoundNBT) null);
                        pLevel.addFreshEntityWithPassengers(zoglinEntity2);
                        pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 2);
                        pLevel.setBlock(pPos.above(), Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            }
        }
    }

    private void maybeAddVines(Random pRandom, IWorld pLevel, BlockPos pPos) {
        BlockState blockstate = pLevel.getBlockState(pPos);
        if (!blockstate.isAir() && !blockstate.is(Blocks.VINE)) {
            Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(pRandom);
            BlockPos blockpos = pPos.relative(direction);
            BlockState blockstate1 = pLevel.getBlockState(blockpos);
            if (blockstate1.isAir()) {
                if (Block.isFaceFull(blockstate.getCollisionShape(pLevel, pPos), direction)) {
                    BooleanProperty booleanproperty = VineBlock.getPropertyForFace(direction.getOpposite());
                    pLevel.setBlock(blockpos, Blocks.VINE.defaultBlockState().setValue(booleanproperty, Boolean.valueOf(true)), 3);
                }
            }
        }
    }

    private void maybeAddLeavesAbove(Random pRandom, IWorld pLevel, BlockPos pPos) {
        if (pRandom.nextFloat() < 0.5F && pLevel.getBlockState(pPos).is(Blocks.NETHERRACK) && pLevel.getBlockState(pPos.above()).isAir()) {
            pLevel.setBlock(pPos.above(), Blocks.JUNGLE_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, Boolean.valueOf(true)), 3);
        }

    }

    private void maybeAddObsidianAbove(Random pRandom, IWorld pLevel, BlockPos pPos) {
        if (pRandom.nextFloat() < 0.05F && pLevel.getBlockState(pPos).is(Blocks.NETHERRACK) && pLevel.getBlockState(pPos.above()).isAir()) {
            pLevel.setBlock(pPos.above(), Blocks.OBSIDIAN.defaultBlockState(), 3);
        }

    }

    private void addNetherrackDripColumnsBelowPortal(Random pRandom, IWorld pLevel) {
        for(int i = this.boundingBox.x0 + 1; i < this.boundingBox.x1; ++i) {
            for(int j = this.boundingBox.z0 + 1; j < this.boundingBox.z1; ++j) {
                BlockPos blockpos = new BlockPos(i, this.boundingBox.y0, j);
                if (pLevel.getBlockState(blockpos).is(Blocks.NETHERRACK)) {
                    this.addNetherrackDripColumn(pRandom, pLevel, blockpos.below());
                }
            }
        }

    }

    private void addNetherrackDripColumn(Random pRandom, IWorld pLevel, BlockPos pPos) {
        BlockPos.Mutable blockpos$mutable = pPos.mutable();
        this.placeNetherrackOrMagma(pRandom, pLevel, blockpos$mutable);
        int i = 8;

        while(i > 0 && pRandom.nextFloat() < 0.5F) {
            blockpos$mutable.move(Direction.DOWN);
            --i;
            this.placeNetherrackOrMagma(pRandom, pLevel, blockpos$mutable);
        }

    }

    private void spreadNetherrack(Random pRandom, IWorld pLevel) {
        Vector3i vector3i = this.boundingBox.getCenter();
        int i = vector3i.getX();
        int j = vector3i.getZ();
        float[] afloat = new float[]{1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.9F, 0.9F, 0.8F, 0.7F, 0.6F, 0.4F, 0.2F};
        int k = afloat.length;
        int l = (this.boundingBox.getXSpan() + this.boundingBox.getZSpan()) / 2;
        int i1 = pRandom.nextInt(Math.max(1, 8 - l / 2));
        int j1 = 3;
        BlockPos.Mutable blockpos$mutable = BlockPos.ZERO.mutable();

        for(int k1 = i - k; k1 <= i + k; ++k1) {
            for(int l1 = j - k; l1 <= j + k; ++l1) {
                int i2 = Math.abs(k1 - i) + Math.abs(l1 - j);
                int j2 = Math.max(0, i2 + i1);
                if (j2 < k) {
                    float f = afloat[j2];
                    if (pRandom.nextDouble() < (double)f) {
                        int k2 = getSurfaceY(pLevel, k1, l1);
                        blockpos$mutable.set(k1, k2, l1);
                        if (Math.abs(k2 - this.boundingBox.y0) <= 3 && this.canBlockBeReplacedByNetherrackOrMagma(pLevel, blockpos$mutable)) {
                            this.placeNetherrackOrMagma(pRandom, pLevel, blockpos$mutable);
                            if (this.properties.overgrown) {
                                this.maybeAddLeavesAbove(pRandom, pLevel, blockpos$mutable);
                            }

                            this.addNetherrackDripColumn(pRandom, pLevel, blockpos$mutable.below());
                        }
                    }
                }
            }
        }

    }

    private boolean canBlockBeReplacedByNetherrackOrMagma(IWorld pLevel, BlockPos pPos) {
        BlockState blockstate = pLevel.getBlockState(pPos);
        return !blockstate.is(Blocks.AIR) && !blockstate.is(Blocks.OBSIDIAN) && !blockstate.is(Blocks.CHEST) && !blockstate.is(Blocks.LAVA);
    }

    private void placeNetherrackOrMagma(Random pRandom, IWorld pLevel, BlockPos pPos) {
        if (!this.properties.cold && pRandom.nextFloat() < 0.07F) {
            pLevel.setBlock(pPos, Blocks.MAGMA_BLOCK.defaultBlockState(), 3);
        } else {
            pLevel.setBlock(pPos, Blocks.NETHERRACK.defaultBlockState(), 3);
        }

    }

    private static int getSurfaceY(IWorld pLevel, int pX, int pZ) {
        return pLevel.getHeight(getHeightMapType(), pX, pZ) - 1;
    }

    public static Heightmap.Type getHeightMapType() {
        return Heightmap.Type.WORLD_SURFACE_WG;
    }

    private static RuleEntry getBlockReplaceRule(Block pBlock, float pProbability, Block pReplaceBlock) {
        return new RuleEntry(new RandomBlockMatchRuleTest(pBlock, pProbability), AlwaysTrueRuleTest.INSTANCE, pReplaceBlock.defaultBlockState());
    }

    private static RuleEntry getBlockReplaceRule(Block pBlock, Block pReplaceBlock) {
        return new RuleEntry(new BlockMatchRuleTest(pBlock), AlwaysTrueRuleTest.INSTANCE, pReplaceBlock.defaultBlockState());
    }

    public static class Serializer {
        public static final Codec<RuinedRitualPiece.Serializer> CODEC = RecordCodecBuilder.create((p_237031_0_) -> {
            return p_237031_0_.group(Codec.BOOL.fieldOf("cold").forGetter((p_237037_0_) -> {
                return p_237037_0_.cold;
            }), Codec.FLOAT.fieldOf("mossiness").forGetter((p_237036_0_) -> {
                return p_237036_0_.mossiness;
            }), Codec.BOOL.fieldOf("overgrown").forGetter((p_237034_0_) -> {
                return p_237034_0_.overgrown;
            }), Codec.BOOL.fieldOf("vines").forGetter((p_237033_0_) -> {
                return p_237033_0_.vines;
            })).apply(p_237031_0_, RuinedRitualPiece.Serializer::new);
        });
        public boolean cold;
        public float mossiness = 0.2F;
        public boolean overgrown;
        public boolean vines;

        public Serializer() {
        }

        public <T> Serializer(boolean p_i232112_1_, float p_i232112_2_, boolean p_i232112_4_, boolean p_i232112_5_) {
            this.cold = p_i232112_1_;
            this.mossiness = p_i232112_2_;
            this.overgrown = p_i232112_4_;
            this.vines = p_i232112_5_;
        }
    }
}
