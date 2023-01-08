package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.ally.RottreantEntity;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.pattern.BlockMaterialMatcher;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class AnimalisCoreItem extends Item {
    private static final Predicate<BlockState> HAUNTED_WOOD_PREDICATE = (p_210301_0_) -> {
        return p_210301_0_ != null && (p_210301_0_.is(ModBlocks.HAUNTED_LOG.get()) || p_210301_0_.is(ModBlocks.HAUNTED_WOOD.get()) || p_210301_0_.is(ModBlocks.STRIPPED_HAUNTED_LOG.get()) || p_210301_0_.is(ModBlocks.STRIPPED_HAUNTED_WOOD.get()));
    };
    private static final Predicate<BlockState> MUSK_WOOD_PREDICATE = (p_210301_0_) -> {
        return p_210301_0_ != null && (p_210301_0_.is(ModBlocks.MURK_LOG.get()) || p_210301_0_.is(ModBlocks.MURK_WOOD.get()) || p_210301_0_.is(ModBlocks.STRIPPED_MURK_LOG.get()) || p_210301_0_.is(ModBlocks.STRIPPED_MURK_WOOD.get()));
    };
    private static final Predicate<BlockState> GLOOM_WOOD_PREDICATE = (p_210301_0_) -> {
        return p_210301_0_ != null && (p_210301_0_.is(ModBlocks.GLOOM_LOG.get()) || p_210301_0_.is(ModBlocks.GLOOM_WOOD.get()) || p_210301_0_.is(ModBlocks.STRIPPED_GLOOM_LOG.get()) || p_210301_0_.is(ModBlocks.STRIPPED_GLOOM_WOOD.get()));
    };

    public AnimalisCoreItem() {
        super(new Properties().tab(Goety.TAB));
    }

    public ActionResultType useOn(ItemUseContext pContext) {
        World world = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);
        PlayerEntity player = pContext.getPlayer();
        if (player != null){
            if (RobeArmorFinder.FindFelArmor(player)){
                if (blockstate.getBlock() == ModBlocks.ROTTEN_PUMPKIN.get()){
                    if (this.trySpawnRotTree(world, blockpos, player)){
                        pContext.getItemInHand().shrink(1);
                        return ActionResultType.CONSUME;
                    }
                }
            }
        }
        return ActionResultType.PASS;
    }

    private boolean trySpawnRotTree(World pLevel, BlockPos pPos, LivingEntity pEntity) {
        RottreantEntity rottreantEntity = ModEntityType.ROT_TREE.get().create(pLevel);
        BlockPattern.PatternHelper hauntedPattern = this.getOrCreateHauntedRotTree().find(pLevel, pPos);
        BlockPattern.PatternHelper murkPattern = this.getOrCreateMurkRotTree().find(pLevel, pPos);
        BlockPattern.PatternHelper gloomPattern = this.getOrCreateGloomRotTree().find(pLevel, pPos);
        if (hauntedPattern != null) {
            for (int j = 0; j < this.getOrCreateHauntedRotTree().getWidth(); ++j) {
                for (int k = 0; k < this.getOrCreateHauntedRotTree().getHeight(); ++k) {
                    CachedBlockInfo cachedblockinfo2 = hauntedPattern.getBlock(j, k, 0);
                    pLevel.setBlock(cachedblockinfo2.getPos(), Blocks.AIR.defaultBlockState(), 2);
                    pLevel.levelEvent(2001, cachedblockinfo2.getPos(), Block.getId(cachedblockinfo2.getState()));
                }
            }

            BlockPos blockpos1 = hauntedPattern.getBlock(1, 2, 0).getPos();
            if (rottreantEntity != null) {
                rottreantEntity.setWoodType(RottreantEntity.RotTreeWoodType.HAUNTED);
                rottreantEntity.setTrueOwner(pEntity);
                rottreantEntity.moveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.05D, (double) blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
                pLevel.addFreshEntity(rottreantEntity);
                for (int j = 0; j < this.getOrCreateHauntedRotTree().getWidth(); ++j) {
                    for (int k = 0; k < this.getOrCreateHauntedRotTree().getHeight(); ++k) {
                        CachedBlockInfo cachedblockinfo2 = hauntedPattern.getBlock(j, k, 0);
                        pLevel.blockUpdated(cachedblockinfo2.getPos(), Blocks.AIR);
                    }
                }
                return true;
            }

        }
        if (murkPattern != null) {
            for (int j = 0; j < this.getOrCreateMurkRotTree().getWidth(); ++j) {
                for (int k = 0; k < this.getOrCreateMurkRotTree().getHeight(); ++k) {
                    CachedBlockInfo cachedblockinfo2 = murkPattern.getBlock(j, k, 0);
                    pLevel.setBlock(cachedblockinfo2.getPos(), Blocks.AIR.defaultBlockState(), 2);
                    pLevel.levelEvent(2001, cachedblockinfo2.getPos(), Block.getId(cachedblockinfo2.getState()));
                }
            }

            BlockPos blockpos1 = murkPattern.getBlock(1, 2, 0).getPos();
            if (rottreantEntity != null) {
                rottreantEntity.setWoodType(RottreantEntity.RotTreeWoodType.MURK);
                rottreantEntity.setTrueOwner(pEntity);
                rottreantEntity.moveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.05D, (double) blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
                pLevel.addFreshEntity(rottreantEntity);
                for (int j = 0; j < this.getOrCreateMurkRotTree().getWidth(); ++j) {
                    for (int k = 0; k < this.getOrCreateMurkRotTree().getHeight(); ++k) {
                        CachedBlockInfo cachedblockinfo2 = murkPattern.getBlock(j, k, 0);
                        pLevel.blockUpdated(cachedblockinfo2.getPos(), Blocks.AIR);
                    }
                }
                return true;
            }

        }
        if (gloomPattern != null) {
            for (int j = 0; j < this.getOrCreateGloomRotTree().getWidth(); ++j) {
                for (int k = 0; k < this.getOrCreateGloomRotTree().getHeight(); ++k) {
                    CachedBlockInfo cachedblockinfo2 = gloomPattern.getBlock(j, k, 0);
                    pLevel.setBlock(cachedblockinfo2.getPos(), Blocks.AIR.defaultBlockState(), 2);
                    pLevel.levelEvent(2001, cachedblockinfo2.getPos(), Block.getId(cachedblockinfo2.getState()));
                }
            }

            BlockPos blockpos1 = gloomPattern.getBlock(1, 2, 0).getPos();
            if (rottreantEntity != null) {
                rottreantEntity.setWoodType(RottreantEntity.RotTreeWoodType.GLOOM);
                rottreantEntity.setTrueOwner(pEntity);
                rottreantEntity.moveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.05D, (double) blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
                pLevel.addFreshEntity(rottreantEntity);
                for (int j = 0; j < this.getOrCreateGloomRotTree().getWidth(); ++j) {
                    for (int k = 0; k < this.getOrCreateGloomRotTree().getHeight(); ++k) {
                        CachedBlockInfo cachedblockinfo2 = gloomPattern.getBlock(j, k, 0);
                        pLevel.blockUpdated(cachedblockinfo2.getPos(), Blocks.AIR);
                    }
                }
                return true;
            }

        }
        return false;
    }

    private BlockPattern getOrCreateHauntedRotTree() {
        return BlockPatternBuilder.start().aisle("~%~", "#$#", "~#~").where('%', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(ModBlocks.ROTTEN_PUMPKIN.get()))).where('#', CachedBlockInfo.hasState(HAUNTED_WOOD_PREDICATE)).where('$', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(ModBlocks.CURSED_STONE_BLOCK.get()))).where('~', CachedBlockInfo.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
    }

    private BlockPattern getOrCreateMurkRotTree() {
        return BlockPatternBuilder.start().aisle("~%~", "#$#", "~#~").where('%', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(ModBlocks.ROTTEN_PUMPKIN.get()))).where('#', CachedBlockInfo.hasState(MUSK_WOOD_PREDICATE)).where('$', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(ModBlocks.MURK_LEAVES.get()))).where('~', CachedBlockInfo.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
    }

    private BlockPattern getOrCreateGloomRotTree() {
        return BlockPatternBuilder.start().aisle("~%~", "#$#", "~#~").where('%', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(ModBlocks.ROTTEN_PUMPKIN.get()))).where('#', CachedBlockInfo.hasState(GLOOM_WOOD_PREDICATE)).where('$', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(ModBlocks.GLOOM_LEAVES.get()))).where('~', CachedBlockInfo.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
    }

}
