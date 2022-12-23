package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.entities.hostile.dead.IDeadMob;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.ModDamageSource;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class HauntedCactusBlock extends Block implements net.minecraftforge.common.IPlantable {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
    protected static final VoxelShape COLLISION_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);
    protected static final VoxelShape OUTLINE_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

    public HauntedCactusBlock() {
        super(Properties.of(Material.CACTUS)
                .randomTicks()
                .strength(0.4F)
                .sound(SoundType.WOOL)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    public void tick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRand) {
        if (!pLevel.isAreaLoaded(pPos, 1)) {
            return;
        }
        if (!pState.canSurvive(pLevel, pPos)) {
            pLevel.destroyBlock(pPos, true);
        }

    }

    public void randomTick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom) {
        BlockPos blockpos = pPos.above();
        if (pLevel.isEmptyBlock(blockpos)) {
            int i;
            for(i = 1; pLevel.getBlockState(pPos.below(i)).is(this); ++i) {
            }

            if (i < 6) {
                int j = pState.getValue(AGE);
                if(net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, blockpos, pState, true)) {
                    if (j == 15) {
                        pLevel.setBlockAndUpdate(blockpos, this.defaultBlockState());
                        BlockState blockstate = pState.setValue(AGE, 0);
                        pLevel.setBlock(pPos, blockstate, 4);
                        blockstate.neighborChanged(pLevel, blockpos, this, pPos, false);
                    } else {
                        pLevel.setBlock(pPos, pState.setValue(AGE, j + 1), 4);
                    }
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
                }
            }
        }
    }

    public VoxelShape getCollisionShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return COLLISION_SHAPE;
    }

    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return OUTLINE_SHAPE;
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, IWorld pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (!pState.canSurvive(pLevel, pCurrentPos)) {
            pLevel.getBlockTicks().scheduleTick(pCurrentPos, this, 1);
        }

        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    public boolean canSurvive(BlockState pState, IWorldReader pLevel, BlockPos pPos) {
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState blockstate = pLevel.getBlockState(pPos.relative(direction));
            Material material = blockstate.getMaterial();
            if (material.isSolid() || pLevel.getFluidState(pPos.relative(direction)).is(FluidTags.LAVA)) {
                return false;
            }
        }

        BlockState blockstate1 = pLevel.getBlockState(pPos.below());
        return this.canSustainThis(blockstate1, pLevel, pPos, Direction.UP, this) && !pLevel.getBlockState(pPos.above()).getMaterial().isLiquid();
    }

    public void entityInside(BlockState pState, World pLevel, BlockPos pPos, Entity pEntity) {
        if (pEntity instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) pEntity;
            if (pEntity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) livingEntity;
                if (MobUtil.playerValidity(player, true)) {
                    player.hurt(ModDamageSource.DESICCATE, 1.0F);
                    player.addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 600));
                }
            } else {
                if (livingEntity.getMobType() != CreatureAttribute.UNDEAD && !(pEntity instanceof IDeadMob)){
                    livingEntity.hurt(ModDamageSource.DESICCATE, 1.0F);
                    livingEntity.addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 600));
                }
            }
        } else {
            pEntity.hurt(ModDamageSource.DESICCATE, 1.0F);
        }
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE);
    }

    public boolean isPathfindable(BlockState pState, IBlockReader pLevel, BlockPos pPos, PathType pType) {
        return false;
    }

    public PathNodeType getAiPathNodeType(BlockState state, IBlockReader world, BlockPos pos, @Nullable MobEntity entity) {
        return PathNodeType.DAMAGE_CACTUS;
    }

    @Override
    public BlockState getPlant(IBlockReader world, BlockPos pos) {
        return defaultBlockState();
    }

    public boolean canSustainThis(BlockState state, IBlockReader world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable) {
        BlockState plant = plantable.getPlant(world, pos.relative(facing));
        if (plant.getBlock() == this.getBlock()){
            return state.is(ModBlocks.HAUNTED_CACTUS.get()) || state.is(ModBlocks.DEAD_SAND.get()) || state.getBlock() instanceof SandBlock;
        } else {
            return false;
        }
    }
}
