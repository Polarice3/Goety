package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.client.particles.ShockwaveParticleOption;
import com.Polarice3.Goety.common.tileentities.HookBellTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BellAttachment;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.BellTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class HookBellBlock extends ContainerBlock {
   public static final DirectionProperty FACING = HorizontalBlock.FACING;
   public static final EnumProperty<BellAttachment> ATTACHMENT = BlockStateProperties.BELL_ATTACHMENT;
   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
   private static final VoxelShape NORTH_SOUTH_FLOOR_SHAPE = Block.box(0.0D, 0.0D, 4.0D, 16.0D, 16.0D, 12.0D);
   private static final VoxelShape EAST_WEST_FLOOR_SHAPE = Block.box(4.0D, 0.0D, 0.0D, 12.0D, 16.0D, 16.0D);
   private static final VoxelShape BELL_TOP_SHAPE = Block.box(5.0D, 6.0D, 5.0D, 11.0D, 13.0D, 11.0D);
   private static final VoxelShape BELL_BOTTOM_SHAPE = Block.box(4.0D, 4.0D, 4.0D, 12.0D, 6.0D, 12.0D);
   private static final VoxelShape BELL_SHAPE = VoxelShapes.or(BELL_BOTTOM_SHAPE, BELL_TOP_SHAPE);
   private static final VoxelShape NORTH_SOUTH_BETWEEN = VoxelShapes.or(BELL_SHAPE, Block.box(7.0D, 13.0D, 0.0D, 9.0D, 15.0D, 16.0D));
   private static final VoxelShape EAST_WEST_BETWEEN = VoxelShapes.or(BELL_SHAPE, Block.box(0.0D, 13.0D, 7.0D, 16.0D, 15.0D, 9.0D));
   private static final VoxelShape TO_WEST = VoxelShapes.or(BELL_SHAPE, Block.box(0.0D, 13.0D, 7.0D, 13.0D, 15.0D, 9.0D));
   private static final VoxelShape TO_EAST = VoxelShapes.or(BELL_SHAPE, Block.box(3.0D, 13.0D, 7.0D, 16.0D, 15.0D, 9.0D));
   private static final VoxelShape TO_NORTH = VoxelShapes.or(BELL_SHAPE, Block.box(7.0D, 13.0D, 0.0D, 9.0D, 15.0D, 13.0D));
   private static final VoxelShape TO_SOUTH = VoxelShapes.or(BELL_SHAPE, Block.box(7.0D, 13.0D, 3.0D, 9.0D, 15.0D, 16.0D));
   private static final VoxelShape CEILING_SHAPE = VoxelShapes.or(BELL_SHAPE, Block.box(7.0D, 13.0D, 7.0D, 9.0D, 16.0D, 9.0D));

   public HookBellBlock() {
      super(Properties
              .of(Material.METAL, MaterialColor.COLOR_GREEN)
              .requiresCorrectToolForDrops()
              .strength(5.0F)
              .sound(SoundType.ANVIL));
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ATTACHMENT, BellAttachment.FLOOR).setValue(POWERED, Boolean.valueOf(false)));
   }

   public void neighborChanged(BlockState pState, World pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
      boolean flag = pLevel.hasNeighborSignal(pPos);
      if (flag != pState.getValue(POWERED)) {
         pLevel.setBlock(pPos, pState.setValue(POWERED, Boolean.valueOf(flag)), 3);
      }

   }

   public void onProjectileHit(World pLevel, BlockState pState, BlockRayTraceResult pHit, ProjectileEntity pProjectile) {
      Entity entity = pProjectile.getOwner();
      PlayerEntity playerentity = entity instanceof PlayerEntity ? (PlayerEntity)entity : null;
      this.onHit(pLevel, pState, pHit, playerentity, true);
   }

   public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
      return this.onHit(pLevel, pState, pHit, pPlayer, true) ? ActionResultType.sidedSuccess(pLevel.isClientSide) : ActionResultType.PASS;
   }

   public boolean onHit(World pLevel, BlockState pState, BlockRayTraceResult pResult, @Nullable PlayerEntity pPlayer, boolean pCanRingBell) {
      Direction direction = pResult.getDirection();
      BlockPos blockpos = pResult.getBlockPos();
      boolean flag = !pCanRingBell || this.isProperHit(pState, direction, pResult.getLocation().y - (double)blockpos.getY());
      if (flag && pPlayer != null) {
         boolean flag1 = this.attemptToRing(pLevel, blockpos, direction);
         if (flag1) {
            pPlayer.awardStat(Stats.BELL_RING);
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean isProperHit(BlockState pPos, Direction pDirection, double pDistanceY) {
      if (pDirection.getAxis() != Direction.Axis.Y && !(pDistanceY > (double)0.8124F)) {
         Direction direction = pPos.getValue(FACING);
         BellAttachment bellattachment = pPos.getValue(ATTACHMENT);
         switch(bellattachment) {
         case FLOOR:
            return direction.getAxis() == pDirection.getAxis();
         case SINGLE_WALL:
         case DOUBLE_WALL:
            return direction.getAxis() != pDirection.getAxis();
         case CEILING:
            return true;
         default:
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean attemptToRing(World pLevel, BlockPos pPos, @Nullable Direction pDirection) {
      TileEntity tileentity = pLevel.getBlockEntity(pPos);
      if (!pLevel.isClientSide && tileentity instanceof HookBellTileEntity) {
         if (pDirection == null) {
            pDirection = pLevel.getBlockState(pPos).getValue(FACING);
         }

         ((HookBellTileEntity)tileentity).onHit(pDirection);
         pLevel.playSound((PlayerEntity)null, pPos, SoundEvents.BELL_BLOCK, SoundCategory.BLOCKS, 10.0F, 0.5F);
         if (pLevel instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld) pLevel;
            serverWorld.sendParticles(new ShockwaveParticleOption(0), pPos.getX() + 0.5D, pPos.getY() + 0.5F, pPos.getZ() + 0.5D, 0, 0.0D, 0.0D, 0.0D, 0);
         }
         return true;
      } else {
         return false;
      }
   }

   private VoxelShape getVoxelShape(BlockState pState) {
      Direction direction = pState.getValue(FACING);
      BellAttachment bellattachment = pState.getValue(ATTACHMENT);
      if (bellattachment == BellAttachment.FLOOR) {
         return direction != Direction.NORTH && direction != Direction.SOUTH ? EAST_WEST_FLOOR_SHAPE : NORTH_SOUTH_FLOOR_SHAPE;
      } else if (bellattachment == BellAttachment.CEILING) {
         return CEILING_SHAPE;
      } else if (bellattachment == BellAttachment.DOUBLE_WALL) {
         return direction != Direction.NORTH && direction != Direction.SOUTH ? EAST_WEST_BETWEEN : NORTH_SOUTH_BETWEEN;
      } else if (direction == Direction.NORTH) {
         return TO_NORTH;
      } else if (direction == Direction.SOUTH) {
         return TO_SOUTH;
      } else {
         return direction == Direction.EAST ? TO_EAST : TO_WEST;
      }
   }

   public VoxelShape getCollisionShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
      return this.getVoxelShape(pState);
   }

   public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
      return this.getVoxelShape(pState);
   }

   public BlockRenderType getRenderShape(BlockState pState) {
      return BlockRenderType.MODEL;
   }

   @Nullable
   public BlockState getStateForPlacement(BlockItemUseContext pContext) {
      Direction direction = pContext.getClickedFace();
      BlockPos blockpos = pContext.getClickedPos();
      World world = pContext.getLevel();
      Direction.Axis direction$axis = direction.getAxis();
      if (direction$axis == Direction.Axis.Y) {
         BlockState blockstate = this.defaultBlockState().setValue(ATTACHMENT, direction == Direction.DOWN ? BellAttachment.CEILING : BellAttachment.FLOOR).setValue(FACING, pContext.getHorizontalDirection());
         if (blockstate.canSurvive(pContext.getLevel(), blockpos)) {
            return blockstate;
         }
      } else {
         boolean flag = direction$axis == Direction.Axis.X && world.getBlockState(blockpos.west()).isFaceSturdy(world, blockpos.west(), Direction.EAST) && world.getBlockState(blockpos.east()).isFaceSturdy(world, blockpos.east(), Direction.WEST) || direction$axis == Direction.Axis.Z && world.getBlockState(blockpos.north()).isFaceSturdy(world, blockpos.north(), Direction.SOUTH) && world.getBlockState(blockpos.south()).isFaceSturdy(world, blockpos.south(), Direction.NORTH);
         BlockState blockstate1 = this.defaultBlockState().setValue(FACING, direction.getOpposite()).setValue(ATTACHMENT, flag ? BellAttachment.DOUBLE_WALL : BellAttachment.SINGLE_WALL);
         if (blockstate1.canSurvive(pContext.getLevel(), pContext.getClickedPos())) {
            return blockstate1;
         }

         boolean flag1 = world.getBlockState(blockpos.below()).isFaceSturdy(world, blockpos.below(), Direction.UP);
         blockstate1 = blockstate1.setValue(ATTACHMENT, flag1 ? BellAttachment.FLOOR : BellAttachment.CEILING);
         if (blockstate1.canSurvive(pContext.getLevel(), pContext.getClickedPos())) {
            return blockstate1;
         }
      }

      return null;
   }

   public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, IWorld pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
      BellAttachment bellattachment = pState.getValue(ATTACHMENT);
      Direction direction = getConnectedDirection(pState).getOpposite();
      if (direction == pFacing && !pState.canSurvive(pLevel, pCurrentPos) && bellattachment != BellAttachment.DOUBLE_WALL) {
         return Blocks.AIR.defaultBlockState();
      } else {
         if (pFacing.getAxis() == pState.getValue(FACING).getAxis()) {
            if (bellattachment == BellAttachment.DOUBLE_WALL && !pFacingState.isFaceSturdy(pLevel, pFacingPos, pFacing)) {
               return pState.setValue(ATTACHMENT, BellAttachment.SINGLE_WALL).setValue(FACING, pFacing.getOpposite());
            }

            if (bellattachment == BellAttachment.SINGLE_WALL && direction.getOpposite() == pFacing && pFacingState.isFaceSturdy(pLevel, pFacingPos, pState.getValue(FACING))) {
               return pState.setValue(ATTACHMENT, BellAttachment.DOUBLE_WALL);
            }
         }

         return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
      }
   }

   public boolean canSurvive(BlockState pState, IWorldReader pLevel, BlockPos pPos) {
      Direction direction = getConnectedDirection(pState).getOpposite();
      return direction == Direction.UP ? Block.canSupportCenter(pLevel, pPos.above(), Direction.DOWN) : HorizontalFaceBlock.canAttach(pLevel, pPos, direction);
   }

   private static Direction getConnectedDirection(BlockState pState) {
      switch((BellAttachment)pState.getValue(ATTACHMENT)) {
      case FLOOR:
         return Direction.UP;
      case CEILING:
         return Direction.DOWN;
      default:
         return pState.getValue(FACING).getOpposite();
      }
   }

   public PushReaction getPistonPushReaction(BlockState pState) {
      return PushReaction.DESTROY;
   }

   protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
      pBuilder.add(FACING, ATTACHMENT, POWERED);
   }

   @Nullable
   public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
      return new HookBellTileEntity();
   }

   public boolean isPathfindable(BlockState pState, IBlockReader pLevel, BlockPos pPos, PathType pType) {
      return false;
   }
}