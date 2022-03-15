package com.Polarice3.Goety.common.world.structures.pieces;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.hostile.cultists.AbstractCultistEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.google.common.collect.Lists;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.*;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.EmptyJigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.JigsawJunction;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;

public class SalvagedFortPieces extends TemplateStructurePiece {
    private static final ResourceLocation STRUCTURE = new ResourceLocation(Goety.MOD_ID, "salvaged_fort");
    private static final Logger LOGGER = LogManager.getLogger();
    protected final JigsawPiece element;
    private final ResourceLocation templateLocation;
    private final int groundLevelDelta;
    private final List<JigsawJunction> junctions = Lists.newArrayList();
    private final Rotation rotation;
    private final TemplateManager structureManager;

    public SalvagedFortPieces(TemplateManager pStructureManager, JigsawPiece pElement, ResourceLocation pLocation, BlockPos pPos, int pGroundLevelDelta , Rotation pRotation) {
        super(IStructurePieceType.RUINED_PORTAL, 0);
        this.templatePosition = pPos;
        this.templateLocation = pLocation;
        this.element = pElement;
        this.groundLevelDelta = pGroundLevelDelta;
        this.rotation = pRotation;
        this.structureManager = pStructureManager;
        this.loadTemplate(pStructureManager);
    }

    public SalvagedFortPieces(TemplateManager p_i232110_1_, CompoundNBT p_i232110_2_) {
        super(IStructurePieceType.RUINED_PORTAL, p_i232110_2_);
        this.structureManager = p_i232110_1_;
        this.groundLevelDelta = p_i232110_2_.getInt("ground_level_delta");
        this.element = JigsawPiece.CODEC.parse(NBTDynamicOps.INSTANCE, p_i232110_2_.getCompound("pool_element")).resultOrPartial(LOGGER::error).orElse(EmptyJigsawPiece.INSTANCE);
        this.templateLocation = new ResourceLocation(p_i232110_2_.getString("Template"));
        this.rotation = Rotation.valueOf(p_i232110_2_.getString("Rotation"));
        ListNBT listnbt = p_i232110_2_.getList("junctions", 10);
        this.junctions.clear();
        listnbt.forEach((p_214827_1_) -> {
            this.junctions.add(JigsawJunction.deserialize(new Dynamic<>(NBTDynamicOps.INSTANCE, p_214827_1_)));
        });
        this.loadTemplate(p_i232110_1_);
    }

    private void loadTemplate(TemplateManager p_207614_1_) {
        Template template = p_207614_1_.getOrCreate(this.templateLocation);
        PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE).setRotationPivot(templatePosition).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
        this.setup(template, this.templatePosition, placementsettings);
    }

    protected void addAdditionalSaveData(CompoundNBT p_143011_1_) {
        super.addAdditionalSaveData(p_143011_1_);
        p_143011_1_.putInt("ground_level_delta", this.groundLevelDelta);
        JigsawPiece.CODEC.encodeStart(NBTDynamicOps.INSTANCE, this.element).resultOrPartial(LOGGER::error).ifPresent((p_237002_1_) -> {
            p_143011_1_.put("pool_element", p_237002_1_);
        });
        p_143011_1_.putString("rotation", this.rotation.name());
        ListNBT listnbt = new ListNBT();

        for(JigsawJunction jigsawjunction : this.junctions) {
            listnbt.add(jigsawjunction.serialize(NBTDynamicOps.INSTANCE).getValue());
        }

        p_143011_1_.put("junctions", listnbt);
        p_143011_1_.putString("Template", this.templateLocation.toString());
        p_143011_1_.putString("Rot", this.rotation.name());
    }

    protected void handleDataMarker(String pFunction, BlockPos pPos, IServerWorld pLevel, Random pRandom, MutableBoundingBox pSbb) {
        AbstractCultistEntity cultist;
        switch(pFunction) {
            case "zealot":
                cultist = ModEntityType.ZEALOT.get().create(pLevel.getLevel());
                break;
            case "fanatic":
                cultist = ModEntityType.FANATIC.get().create(pLevel.getLevel());
                break;
            case "disciple":
                cultist = ModEntityType.DISCIPLE.get().create(pLevel.getLevel());
                break;
            case "channeller":
                cultist = ModEntityType.CHANNELLER.get().create(pLevel.getLevel());
                break;
            case "apostle":
                cultist = ModEntityType.APOSTLE.get().create(pLevel.getLevel());
                break;
            default:
                return;
        }

        assert cultist != null;
        cultist.setPersistenceRequired();
        cultist.moveTo(pPos, 0.0F, 0.0F);
        cultist.finalizeSpawn(pLevel, pLevel.getCurrentDifficultyAt(cultist.blockPosition()), SpawnReason.STRUCTURE, (ILivingEntityData)null, (CompoundNBT)null);
        pLevel.addFreshEntityWithPassengers(cultist);
        pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 2);

    }

    public boolean postProcess(ISeedReader pLevel, StructureManager pStructureManager, ChunkGenerator pChunkGenerator, Random pRandom, MutableBoundingBox pBox, ChunkPos pChunkPos, BlockPos pPos) {
        return this.place(pLevel, pStructureManager, pChunkGenerator, pRandom, pBox, pPos, false);
    }

    public boolean place(ISeedReader pLevel, StructureManager pStructureManager, ChunkGenerator pChunkGenerator, Random pRandom, MutableBoundingBox pBox, BlockPos pPos, boolean pKeepJigsaws) {
        return this.element.place(this.structureManager, pLevel, pStructureManager, pChunkGenerator, this.templatePosition, pPos, this.rotation, pBox, pRandom, pKeepJigsaws);
    }

    public String toString() {
        return String.format("<%s | %s | %s | %s>", this.getClass().getSimpleName(), this.templatePosition, this.rotation, this.element);
    }

    public JigsawPiece getElement() {
        return this.element;
    }

    public BlockPos getPosition() {
        return this.templatePosition;
    }

    public int getGroundLevelDelta() {
        return this.groundLevelDelta;
    }

    public void addJunction(JigsawJunction pJunction) {
        this.junctions.add(pJunction);
    }

    public List<JigsawJunction> getJunctions() {
        return this.junctions;
    }

}
