package com.Polarice3.Goety.common.world.structures.pieces;

import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.ConstantPaths;
import com.Polarice3.Goety.utils.ModLootTables;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class CrimsonShrinePiece extends ModStructurePiece {

    public CrimsonShrinePiece(TemplateManager mgr, BlockPos templatePosition, Rotation pRotation) {
        super(IModPieceType.CRIMSON_SHRINE_PIECE, mgr, templatePosition, pRotation);
        this.setYOffset(-1);
    }

    public CrimsonShrinePiece(TemplateManager mgr, CompoundNBT nbt) {
        super(IModPieceType.CRIMSON_SHRINE_PIECE, mgr, nbt);
        this.setYOffset(-1);
    }

    @Override
    public ResourceLocation getStructureName() {
        return ConstantPaths.getCrimsonShrine();
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pPos, IServerWorld pLevel, Random pRandom, MutableBoundingBox pSbb) {
        if (function.equals("statue")) {
            pLevel.setBlock(pPos, ModBlocks.CULT_STATUE.get().defaultBlockState(), 2);
        } else if (function.equals("treasure_block")){
            Rotation rotation = this.placeSettings.getRotation();
            BlockState blockstate = Blocks.BARREL.defaultBlockState().setValue(BarrelBlock.FACING, rotation.rotate(Direction.UP));
            BlockFinder.createBarrel(pLevel, pSbb, pRandom, pPos, ModLootTables.CRIMSON_SHRINE_TREASURE, blockstate);
        } else if (function.startsWith("barrel")) {
            Rotation rotation = this.placeSettings.getRotation();
            BlockState blockstate = Blocks.BARREL.defaultBlockState().setValue(BarrelBlock.FACING, rotation.rotate(Direction.UP));
            BlockFinder.createBarrel(pLevel, pSbb, pRandom, pPos, LootTables.PIGLIN_BARTERING, blockstate);
        } else {
            MobEntity cultist = null;
            if (function.equals("guard")) {
                cultist = ModEntityType.THUG.get().create(pLevel.getLevel());
            }
            if (function.equals("guard_inner")) {
                if (pRandom.nextBoolean()) {
                    cultist = ModEntityType.FANATIC.get().create(pLevel.getLevel());
                } else {
                    cultist = ModEntityType.ZEALOT.get().create(pLevel.getLevel());
                }
            }
            if (function.equals("victim")) {
                if (pRandom.nextBoolean()) {
                    cultist = EntityType.WANDERING_TRADER.create(pLevel.getLevel());
                } else {
                    cultist = EntityType.VILLAGER.create(pLevel.getLevel());
                }
            }
            if (function.equals("sentry")){
                if (pRandom.nextBoolean()){
                    cultist = ModEntityType.ZEALOT.get().create(pLevel.getLevel());
                } else {
                    cultist = ModEntityType.WRAITH.get().create(pLevel.getLevel());
                }
            }

            if (cultist != null) {
                cultist.setPersistenceRequired();
                if (!cultist.fireImmune()) {
                    cultist.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
                }
                cultist.moveTo(pPos, 0.0F, 0.0F);
                cultist.finalizeSpawn(pLevel, pLevel.getCurrentDifficultyAt(cultist.blockPosition()), SpawnReason.STRUCTURE, null, null);
                pLevel.addFreshEntityWithPassengers(cultist);
                pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 2);
            }
        }
    }
}
