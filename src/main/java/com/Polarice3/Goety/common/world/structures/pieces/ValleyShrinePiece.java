package com.Polarice3.Goety.common.world.structures.pieces;

import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.ConstantPaths;
import com.Polarice3.Goety.utils.ModLootTables;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
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

public class ValleyShrinePiece extends ModStructurePiece {

    public ValleyShrinePiece(TemplateManager mgr, BlockPos templatePosition, Rotation pRotation) {
        super(IModPieceType.VALLEY_SHRINE_PIECE, mgr, templatePosition, pRotation);
        this.setYOffset(-1);
    }

    public ValleyShrinePiece(TemplateManager mgr, CompoundNBT nbt) {
        super(IModPieceType.VALLEY_SHRINE_PIECE, mgr, nbt);
        this.setYOffset(-1);
    }

    @Override
    public ResourceLocation getStructureName() {
        return ConstantPaths.getValleyShrine();
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pPos, IServerWorld pLevel, Random pRandom, MutableBoundingBox pSbb) {
        switch (function) {
            case "bone_chest": {
                Rotation rotation = this.placeSettings.getRotation();
                BlockState blockstate = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, rotation.rotate(Direction.WEST));
                this.createChest(pLevel, pSbb, pRandom, pPos, ModLootTables.VALLEY_SHRINE_BONE_CHEST, blockstate);
                break;
            }
            case "barrel": {
                Rotation rotation = this.placeSettings.getRotation();
                BlockState blockstate = Blocks.BARREL.defaultBlockState().setValue(BarrelBlock.FACING, rotation.rotate(Direction.UP));
                BlockFinder.createBarrel(pLevel, pSbb, pRandom, pPos, ModLootTables.VALLEY_SHRINE_BARREL, blockstate);
                break;
            }
            case "barrel_milk": {
                Rotation rotation = this.placeSettings.getRotation();
                BlockState blockstate = Blocks.BARREL.defaultBlockState().setValue(BarrelBlock.FACING, rotation.rotate(Direction.UP));
                BlockFinder.createBarrel(pLevel, pSbb, pRandom, pPos, ModLootTables.VALLEY_SHRINE_BARREL_MILK, blockstate);
                break;
            }
            case "barrel_blaze": {
                Rotation rotation = this.placeSettings.getRotation();
                BlockState blockstate = Blocks.BARREL.defaultBlockState().setValue(BarrelBlock.FACING, rotation.rotate(Direction.UP));
                BlockFinder.createBarrel(pLevel, pSbb, pRandom, pPos, ModLootTables.VALLEY_SHRINE_BARREL_BLAZE, blockstate);
                break;
            }
            case "treasure_barrel": {
                Rotation rotation = this.placeSettings.getRotation();
                BlockState blockstate = Blocks.BARREL.defaultBlockState().setValue(BarrelBlock.FACING, rotation.rotate(Direction.UP));
                BlockFinder.createBarrel(pLevel, pSbb, pRandom, pPos, ModLootTables.VALLEY_SHRINE_TREASURE, blockstate);
                break;
            }
            case "north_chest": {
                Rotation rotation = this.placeSettings.getRotation();
                BlockState blockstate = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, rotation.rotate(Direction.NORTH));
                this.createChest(pLevel, pSbb, pRandom, pPos, ModLootTables.VALLEY_SHRINE_WEAPONS, blockstate);
                break;
            }
            default:
                MobEntity mob = null;
                if (function.equals("wither_skeleton")) {
                    mob = EntityType.WITHER_SKELETON.create(pLevel.getLevel());
                }
                if (function.equals("guard")) {
                    mob = ModEntityType.THUG.get().create(pLevel.getLevel());
                }
                if (function.equals("cultist")) {
                    if (pRandom.nextFloat() <= 0.05F) {
                        mob = ModEntityType.DISCIPLE.get().create(pLevel.getLevel());
                    } else if (pRandom.nextFloat() <= 0.5F) {
                        mob = ModEntityType.ZEALOT.get().create(pLevel.getLevel());
                    } else if (pRandom.nextFloat() <= 0.75F) {
                        mob = ModEntityType.FANATIC.get().create(pLevel.getLevel());
                    }
                }
                if (function.equals("witch")) {
                    mob = ModEntityType.BELDAM.get().create(pLevel.getLevel());
                }
                if (function.equals("blaze")) {
                    mob = EntityType.BLAZE.create(pLevel.getLevel());
                }

                if (mob != null) {
                    mob.setPersistenceRequired();
                    if (!mob.fireImmune()) {
                        mob.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
                    }
                    mob.moveTo(pPos, 0.0F, 0.0F);
                    mob.finalizeSpawn(pLevel, pLevel.getCurrentDifficultyAt(mob.blockPosition()), SpawnReason.STRUCTURE, null, null);
                    pLevel.addFreshEntityWithPassengers(mob);
                    pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 2);
                }
                break;
        }
    }
}
