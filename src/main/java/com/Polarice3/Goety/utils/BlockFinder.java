package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.init.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.Tags;

public class BlockFinder {
    public static boolean NotDeadSandImmune(BlockState state, IWorldReader world, BlockPos pos, BlockPos sand){
        return !state.is(ModTags.Blocks.DEAD_SAND_IMMUNE) && state.canOcclude()
                && state.getMaterial() != Material.AIR && state.getMaterial() != Material.WATER && state.getMaterial() != Material.NETHER_WOOD
                && state.getMaterial() != Material.LAVA;
    }

}
