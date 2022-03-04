package com.Polarice3.Goety.common.blocks;
/*

import com.Polarice3.Goety.common.tileentities.SoulForgeTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.stateDefinition;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.random;

public class SoulForgeBlock extends ContainerBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public SoulForgeBlock() {
        super(AbstractBlock.Properties.of(Material.STONE)
                .strength(3.0F, 9.0F)
                .sound(SoundType.STONE)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)
        );
        this.registerDefaultState(this.stateDefinition.any().with(LIT, Boolean.FALSE));
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader worldIn) {
        return new SoulForgeTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit){
        if (!worldIn.isClientSide()){
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof SoulForgeTileEntity){
                NetworkHooks.openGui((ServerPlayerEntity) player, (SoulForgeTileEntity) tileEntity, pos);
            }
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random random) {
        if (stateIn.get(LIT)) {
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY();
            double d2 = (double)pos.getZ() + 0.5D;
            if (random.nextDouble() < 0.1D) {
                worldIn.playSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            double d4 = random.nextDouble() * 0.6D - 0.3D;
            double d6 = random.nextDouble() * 6.0D / 16.0D;
            worldIn.addParticle(ParticleTypes.SMOKE, d0 + d4, d1 + d6, d2 + d4, 0.0D, 0.0D, 0.0D);
            worldIn.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 + d4, d1 + d6, d2 + d4, 0.0D, 0.0D, 0.0D);
        }
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    protected void createBlockStateDefinition(stateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

}
*/
