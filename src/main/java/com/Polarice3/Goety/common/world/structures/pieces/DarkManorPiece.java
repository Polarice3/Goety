package com.Polarice3.Goety.common.world.structures.pieces;

import com.Polarice3.Goety.utils.ConstantPaths;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.IllusionerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

/**
 * This class is based on a part of the Astral Sorcery Mod
 * The source code that is based for this mod can be found on Astral Sorcery github.
 * Code based made by HellFirePvP
 */

public class DarkManorPiece extends ModStructurePiece {

    public DarkManorPiece(TemplateManager mgr, BlockPos templatePosition, Rotation pRotation) {
        super(IModPieceType.DARK_MANOR_PIECE, mgr, templatePosition, pRotation);
        this.setYOffset(-1);
    }

    public DarkManorPiece(TemplateManager mgr, CompoundNBT nbt) {
        super(IModPieceType.DARK_MANOR_PIECE, mgr, nbt);
        this.setYOffset(-1);
    }

    @Override
    public ResourceLocation getStructureName() {
        return ConstantPaths.getDarkManor();
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pPos, IServerWorld pLevel, Random pRandom, MutableBoundingBox pSbb) {
        AbstractRaiderEntity illager;
        switch (function) {
            case "pillager":
                illager = EntityType.PILLAGER.create(pLevel.getLevel());
                break;
            case "warrior":
                illager = EntityType.VINDICATOR.create(pLevel.getLevel());
                break;
            case "master":
                illager = EntityType.ILLUSIONER.create(pLevel.getLevel());
                break;
            default:
                return;
        }
        assert illager != null;
        illager.setPersistenceRequired();
        if (illager instanceof IllusionerEntity){
            illager.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));
            illager.addEffect(new EffectInstance(Effects.ABSORPTION, Integer.MAX_VALUE, 1, false, false));
            illager.addEffect(new EffectInstance(Effects.REGENERATION, Integer.MAX_VALUE, 1, false, false));
        }
        illager.moveTo(pPos, 0.0F, 0.0F);
        illager.finalizeSpawn(pLevel, pLevel.getCurrentDifficultyAt(illager.blockPosition()), SpawnReason.STRUCTURE, (ILivingEntityData) null, (CompoundNBT) null);
        pLevel.addFreshEntityWithPassengers(illager);
        pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 2);
    }
}
