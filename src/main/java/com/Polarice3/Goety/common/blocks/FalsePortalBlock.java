package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.entities.hostile.HogLordEntity;
import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class FalsePortalBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    protected static final VoxelShape X_AXIS_AABB = Block.box(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
    protected static final VoxelShape Z_AXIS_AABB = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);

    public FalsePortalBlock() {
        super(AbstractBlock.Properties.of(Material.PORTAL)
                .noCollission()
                .randomTicks()
                .strength(-1.0F)
                .sound(SoundType.GLASS)
                .lightLevel((p_235463_0_) -> 11));
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        switch((Direction.Axis)pState.getValue(AXIS)) {
            case Z:
                return Z_AXIS_AABB;
            case X:
            default:
                return X_AXIS_AABB;
        }
    }

    public void randomTick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom) {
        if (pLevel.dimensionType().natural() && pLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING) && pRandom.nextInt(1000) < pLevel.getDifficulty().getId()) {

            double d0 = (double)pPos.getX() + (pRandom.nextDouble() - pRandom.nextDouble()) * 4.5D;
            double d2 = (double)pPos.getZ() + (pRandom.nextDouble() - pRandom.nextDouble()) * 4.5D;

            BlockPos.Mutable spawnPos = new BlockPos.Mutable(d0, pPos.getY(), d2);
            while (spawnPos.getY() > 0 && !pLevel.getBlockState(spawnPos).getMaterial().blocksMotion()) {
                spawnPos.move(Direction.DOWN);
            }

            float random = pRandom.nextFloat();

            if (random < 0.01F){
                if (pLevel.getBlockState(spawnPos).isValidSpawn(pLevel, spawnPos, EntityType.GHAST)) {
                    Entity entity = EntityType.GHAST.spawn(pLevel, (CompoundNBT) null, (ITextComponent) null, (PlayerEntity) null, spawnPos.above(), SpawnReason.STRUCTURE, false, false);
                    if (entity != null) {
                        entity.setPortalCooldown();
                    }
                }
            } else if (random > 0.01F && random < 0.25F){
                if (pRandom.nextBoolean()) {
                    if (pLevel.getBlockState(spawnPos).isValidSpawn(pLevel, spawnPos, EntityType.ZOMBIFIED_PIGLIN)) {
                        Entity entity = EntityType.ZOMBIFIED_PIGLIN.spawn(pLevel, (CompoundNBT) null, (ITextComponent) null, (PlayerEntity) null, spawnPos.above(), SpawnReason.STRUCTURE, false, false);
                        if (entity != null) {
                            entity.setPortalCooldown();
                        }
                    }
                } else {
                    if (pLevel.getBlockState(spawnPos).isValidSpawn(pLevel, spawnPos, EntityType.MAGMA_CUBE)) {
                        Entity entity = EntityType.MAGMA_CUBE.spawn(pLevel, (CompoundNBT) null, (ITextComponent) null, (PlayerEntity) null, spawnPos.above(), SpawnReason.STRUCTURE, false, false);
                        if (entity != null) {
                            entity.setPortalCooldown();
                        }
                    }
                }
            } else if (random >= 0.25F && random < 0.5F){
                if (pLevel.getBlockState(spawnPos).isValidSpawn(pLevel, spawnPos, EntityType.ZOMBIFIED_PIGLIN)) {
                    Entity entity = EntityType.ZOMBIFIED_PIGLIN.spawn(pLevel, (CompoundNBT) null, (ITextComponent) null, (PlayerEntity) null, spawnPos.above(), SpawnReason.STRUCTURE, false, false);
                    if (entity != null) {
                        entity.setPortalCooldown();
                    }
                }
            } else if (random >= 0.5F && random < 0.75F){
                if (pLevel.getBlockState(spawnPos).isValidSpawn(pLevel, spawnPos, EntityType.SKELETON)) {
                    Entity entity = EntityType.SKELETON.spawn(pLevel, (CompoundNBT) null, (ITextComponent) null, (PlayerEntity) null, spawnPos.above(), SpawnReason.STRUCTURE, false, false);
                    if (entity != null) {
                        entity.setPortalCooldown();
                    }
                }
            } else if (random >= 0.75F) {
                if (pLevel.getBlockState(spawnPos).isValidSpawn(pLevel, spawnPos, EntityType.MAGMA_CUBE)) {
                    Entity entity = EntityType.MAGMA_CUBE.spawn(pLevel, (CompoundNBT) null, (ITextComponent) null, (PlayerEntity) null, spawnPos.above(), SpawnReason.STRUCTURE, false, false);
                    if (entity != null) {
                        entity.setPortalCooldown();
                    }
                }
            }
        }

    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, IWorld pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        Direction.Axis direction$axis = pFacing.getAxis();
        Direction.Axis direction$axis1 = pState.getValue(AXIS);
        boolean flag = direction$axis1 != direction$axis && direction$axis.isHorizontal();
        return !flag && !pFacingState.is(this) && !(new PortalSize(pLevel, pCurrentPos, direction$axis1)).isComplete() ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    public void entityInside(BlockState pState, World pLevel, BlockPos pPos, Entity pEntity) {
        if (pEntity instanceof EnderPearlEntity) {
            pEntity.remove();
            HogLordEntity hogLord = ModEntityType.HOGLORD.get().create(pLevel);
            if (hogLord != null){
                hogLord.moveTo(pPos, hogLord.yRot, hogLord.xRot);
                hogLord.setPersistenceRequired();
                pLevel.addFreshEntity(hogLord);
            }
            pLevel.setBlockAndUpdate(pPos, Blocks.AIR.defaultBlockState());
            for (int e = 0; e <= 2; ++e){
                pLevel.explode(null, pPos.getX(), pPos.getY() + e, pPos.getZ(), 3.0F, Explosion.Mode.DESTROY);
            }
            for (int i = -1; i <= 1; ++i) {
                for (int j = -1; j <= 1; ++j) {
                    for (int k = -1; k <= 1; ++k) {
                        BlockPos blockpos1 = pPos.offset(i, j, k);
                        BlockState blockstate = pLevel.getBlockState(blockpos1);

                        if (blockstate.getBlock() == Blocks.OBSIDIAN || blockstate.getBlock() == Blocks.CRYING_OBSIDIAN) {
                            pLevel.destroyBlock(blockpos1, true);
                        }
                    }
                }
            }
        }

    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState pState, World pLevel, BlockPos pPos, Random pRand) {
        if (pRand.nextInt(24) == 0) {
            pLevel.playLocalSound((double)pPos.getX() + 0.5D, (double)pPos.getY() + 0.5D, (double)pPos.getZ() + 0.5D, SoundEvents.PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, pRand.nextFloat() * 0.4F + 0.2F, false);
        }

        if (pRand.nextInt(240) == 0){
            if (pRand.nextBoolean()){
                pLevel.playLocalSound((double)pPos.getX() + 0.5D, (double)pPos.getY() + 0.5D, (double)pPos.getZ() + 0.5D, SoundEvents.AMBIENT_WARPED_FOREST_MOOD, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            } else {
                pLevel.playLocalSound((double)pPos.getX() + 0.5D, (double)pPos.getY() + 0.5D, (double)pPos.getZ() + 0.5D, SoundEvents.AMBIENT_NETHER_WASTES_MOOD, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }
        }

        for(int i = 0; i < 4; ++i) {
            double d0 = (double)pPos.getX() + pRand.nextDouble();
            double d1 = (double)pPos.getY() + pRand.nextDouble();
            double d2 = (double)pPos.getZ() + pRand.nextDouble();
            double d3 = ((double)pRand.nextFloat() - 0.5D) * 0.5D;
            double d4 = ((double)pRand.nextFloat() - 0.5D) * 0.5D;
            double d5 = ((double)pRand.nextFloat() - 0.5D) * 0.5D;
            int j = pRand.nextInt(2) * 2 - 1;
            if (!pLevel.getBlockState(pPos.west()).is(this) && !pLevel.getBlockState(pPos.east()).is(this)) {
                d0 = (double)pPos.getX() + 0.5D + 0.25D * (double)j;
                d3 = (double)(pRand.nextFloat() * 2.0F * (float)j);
            } else {
                d2 = (double)pPos.getZ() + 0.5D + 0.25D * (double)j;
                d5 = (double)(pRand.nextFloat() * 2.0F * (float)j);
            }

            pLevel.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }

    }

    public ItemStack getCloneItemStack(IBlockReader pLevel, BlockPos pPos, BlockState pState) {
        return ItemStack.EMPTY;
    }

    public BlockState rotate(BlockState pState, Rotation pRotation) {
        switch(pRotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch((Direction.Axis)pState.getValue(AXIS)) {
                    case Z:
                        return pState.setValue(AXIS, Direction.Axis.X);
                    case X:
                        return pState.setValue(AXIS, Direction.Axis.Z);
                    default:
                        return pState;
                }
            default:
                return pState;
        }
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AXIS);
    }
}
