package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.common.blocks.IDeadBlock;
import com.Polarice3.Goety.common.tileentities.PithosTileEntity;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModTags;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.BarrelTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Predicate;

public class BlockFinder {

    public static boolean NoBreak(BlockState state){
        return state.getMaterial() == Material.STONE || state.getMaterial() == Material.METAL || state.getMaterial() == Material.HEAVY_METAL;
    }

    public static boolean NotDeadSandImmune(BlockState state){
        return state.is(ModTags.Blocks.DEAD_SAND_SPREADABLE)
                && state.getMaterial() != Material.AIR && state.getMaterial() != Material.LAVA
                && !state.hasTileEntity();
    }

    public static boolean ActivateDeadSand(BlockState state){
        return (state.is(ModTags.Blocks.DEAD_SAND_SPREADABLE) || state.getMaterial() == Material.AIR || state.getFluidState().is(ModTags.Fluids.QUICKSAND)) && state.getMaterial() != Material.LAVA;
    }

    public static boolean LivingBlocks(BlockState state){
        return state.getBlock() instanceof HugeMushroomBlock || state.getBlock() instanceof StemGrownBlock || state.is(ModTags.Blocks.DEAD_BLOCK_SPREADABLE);
    }

    public static void DeadSandReplaceLagFree(BlockPos pPos, World pLevel){
        BlockState blockstate = pLevel.getBlockState(pPos);
        if (BlockFinder.NotDeadSandImmune(blockstate)) {
            if (blockstate.getMaterial() == Material.STONE) {
                pLevel.setBlockAndUpdate(pPos, ModBlocks.DEAD_SANDSTONE.get().defaultBlockState());
            } else if (blockstate.is(BlockTags.LOGS)) {
                pLevel.setBlockAndUpdate(pPos, ModBlocks.HAUNTED_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, blockstate.getValue(RotatedPillarBlock.AXIS)));
            } else if (blockstate.is(BlockTags.ICE)) {
                pLevel.removeBlock(pPos, false);
            } else if (blockstate.getBlock() instanceof BushBlock && !LivingBlocks(blockstate)) {
                pLevel.removeBlock(pPos, false);
                if (validPlants(pPos, pLevel)){
                    pLevel.setBlockAndUpdate(pPos, ModBlocks.HAUNTED_BUSH.get().defaultBlockState());
                }
            } else {
                pLevel.setBlockAndUpdate(pPos, ModBlocks.DEAD_SAND.get().defaultBlockState());
            }
        }
        if (LivingBlocks(blockstate)){
            pLevel.removeBlock(pPos, false);
            pLevel.setBlockAndUpdate(pPos, ModBlocks.DEAD_BLOCK.get().defaultBlockState());
        }
    }

    public static boolean validPlants(BlockPos pPos, IBlockReader pLevel){
        BlockState blockstate = pLevel.getBlockState(pPos);
        if (blockstate.getBlock() instanceof IPlantable){
            IPlantable plantable = (IPlantable) blockstate.getBlock();
            PlantType type = plantable.getPlantType(pLevel, pPos);
            return !type.equals(PlantType.BEACH) && !type.equals(PlantType.WATER) && !type.equals(PlantType.NETHER)
                    && !(blockstate.getBlock() instanceof TallGrassBlock)
                    && !(blockstate.getBlock() instanceof ILiquidContainer);
        }
        return false;
    }

    public static void WebMovement(LivingEntity livingEntity){
        AxisAlignedBB axisalignedbb = livingEntity.getBoundingBox();
        BlockPos blockpos = new BlockPos(axisalignedbb.minX + 0.001D, axisalignedbb.minY + 0.001D, axisalignedbb.minZ + 0.001D);
        BlockPos blockpos1 = new BlockPos(axisalignedbb.maxX - 0.001D, axisalignedbb.maxY - 0.001D, axisalignedbb.maxZ - 0.001D);
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        if (livingEntity.level.hasChunksAt(blockpos, blockpos1)) {
            for(int i = blockpos.getX(); i <= blockpos1.getX(); ++i) {
                for(int j = blockpos.getY(); j <= blockpos1.getY(); ++j) {
                    for(int k = blockpos.getZ(); k <= blockpos1.getZ(); ++k) {
                        blockpos$mutable.set(i, j, k);
                        BlockState blockstate = livingEntity.level.getBlockState(blockpos$mutable);
                        if (blockstate.getBlock() instanceof WebBlock){
                            livingEntity.makeStuckInBlock(blockstate, Vector3d.ZERO);
                        }
                    }
                }
            }
        }
    }

    public static void BushMovement(LivingEntity livingEntity){
        AxisAlignedBB axisalignedbb = livingEntity.getBoundingBox();
        BlockPos blockpos = new BlockPos(axisalignedbb.minX + 0.001D, axisalignedbb.minY + 0.001D, axisalignedbb.minZ + 0.001D);
        BlockPos blockpos1 = new BlockPos(axisalignedbb.maxX - 0.001D, axisalignedbb.maxY - 0.001D, axisalignedbb.maxZ - 0.001D);
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        if (livingEntity.level.hasChunksAt(blockpos, blockpos1)) {
            for(int i = blockpos.getX(); i <= blockpos1.getX(); ++i) {
                for(int j = blockpos.getY(); j <= blockpos1.getY(); ++j) {
                    for(int k = blockpos.getZ(); k <= blockpos1.getZ(); ++k) {
                        blockpos$mutable.set(i, j, k);
                        BlockState blockstate = livingEntity.level.getBlockState(blockpos$mutable);
                        if (blockstate.getBlock() instanceof SweetBerryBushBlock){
                            livingEntity.makeStuckInBlock(blockstate, Vector3d.ZERO);
                        }
                    }
                }
            }
        }
    }

    public static void ClimbAnyWall(LivingEntity livingEntity){
        Vector3d movement = livingEntity.getDeltaMovement();
        if (livingEntity instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) livingEntity;
            if (!player.abilities.flying && player.horizontalCollision){
                movement = new Vector3d(movement.x, 0.2D, movement.z);
            }
            player.setDeltaMovement(movement);
        } else {
            if (livingEntity.horizontalCollision){
                movement = new Vector3d(movement.x, 0.2D, movement.z);
            }
            livingEntity.setDeltaMovement(movement);
        }
    }

    public static boolean hasChunksAt(LivingEntity livingEntity){
        World world = livingEntity.level;
        BlockPos.Mutable blockpos$mutable = livingEntity.blockPosition().mutable().move(0, 0, 0);
        return world.hasChunksAt(blockpos$mutable.getX() - 10, blockpos$mutable.getY() - 10, blockpos$mutable.getZ() - 10, blockpos$mutable.getX() + 10, blockpos$mutable.getY() + 10, blockpos$mutable.getZ() + 10);
    }

    /**
     * Code based of @BobMowzies Sunstrike positioning.
     */
    public static double moveDownToGround(Entity entity) {
        RayTraceResult rayTrace = rayTrace(entity);
        if (rayTrace.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult hitResult = (BlockRayTraceResult) rayTrace;
            if (hitResult.getDirection() == Direction.UP) {
                BlockState hitBlock = entity.level.getBlockState(hitResult.getBlockPos());
                if (hitBlock.getBlock() instanceof SlabBlock && hitBlock.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.BOTTOM) {
                    return hitResult.getBlockPos().getY() + 1.0625F - 0.5F;
                } else {
                    return hitResult.getBlockPos().getY() + 1.0625F;
                }
            }
        }
        return entity.getY();
    }

    private static RayTraceResult rayTrace(Entity entity) {
        Vector3d startPos = new Vector3d(entity.getX(), entity.getY(), entity.getZ());
        Vector3d endPos = new Vector3d(entity.getX(), 0, entity.getZ());
        return entity.level.clip(new RayTraceContext(startPos, endPos, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entity));
    }

    public static double spawnY(LivingEntity livingEntity, BlockPos blockPos) {
        BlockPos blockpos = blockPos;
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = livingEntity.level.getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(livingEntity.level, blockpos1, Direction.UP)) {
                if (!livingEntity.level.isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = livingEntity.level.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(livingEntity.level, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= MathHelper.floor(livingEntity.getY()) - 1);

        if (flag) {
            if (!(blockpos.getY() + d0 > livingEntity.getY() + 5)) {
                return blockpos.getY() + d0;
            } else {
                return livingEntity.getY();
            }
        } else {
            return livingEntity.getY();
        }
    }

    public static double spawnWaterY(LivingEntity livingEntity, BlockPos blockPos) {
        BlockPos blockpos = blockPos;
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            FluidState fluidState = livingEntity.level.getFluidState(blockpos);
            BlockState blockstate = livingEntity.level.getBlockState(blockpos1);
            if (fluidState.is(FluidTags.WATER)) {
                if (!livingEntity.level.isWaterAt(blockpos)) {
                    BlockState blockstate1 = livingEntity.level.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(livingEntity.level, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            } else if (blockstate.isFaceSturdy(livingEntity.level, blockpos1, Direction.UP)) {
                if (!livingEntity.level.isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = livingEntity.level.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(livingEntity.level, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= MathHelper.floor(livingEntity.getY()) - 1);

        if (flag) {
            if (!(blockpos.getY() + d0 > livingEntity.getY() + 5)) {
                return blockpos.getY() + d0;
            } else {
                return livingEntity.getY();
            }
        } else {
            return livingEntity.getY();
        }
    }

    public static BlockPos fangSpawnPosition(Entity entity) {
        BlockPos blockpos = entity.blockPosition();
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = entity.level.getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(entity.level, blockpos1, Direction.UP)) {
                if (!entity.level.isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = entity.level.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(entity.level, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= MathHelper.floor(blockpos.getY()) - 1);

        if (flag) {
            return new BlockPos(blockpos.getX(), blockpos.getY() + d0, blockpos.getZ());
        }
        return blockpos;
    }

    public static boolean isEmptyBlock(IBlockReader pLevel, BlockPos pPos, BlockState pBlockState, FluidState pFluidState, EntityType<?> pEntityType, boolean pWater) {
        if (pWater){
            if (pBlockState.isCollisionShapeFullBlock(pLevel, pPos)) {
                return false;
            } else {
                return !pEntityType.isBlockDangerous(pBlockState) || pFluidState.isEmpty();
            }
        } else {
            if (pBlockState.isCollisionShapeFullBlock(pLevel, pPos)) {
                return false;
            } else if (!pFluidState.isEmpty()) {
                return false;
            } else {
                return !pEntityType.isBlockDangerous(pBlockState);
            }
        }
    }

    public static BlockPos SummonRadius(LivingEntity livingEntity, World world){
        BlockPos.Mutable blockpos$mutable = livingEntity.blockPosition().mutable().move(0, 0, 0);
        blockpos$mutable.setX(blockpos$mutable.getX() + world.random.nextInt(5) - world.random.nextInt(5));
        blockpos$mutable.setY((int) BlockFinder.moveDownToGround(livingEntity));
        blockpos$mutable.setZ(blockpos$mutable.getZ() + world.random.nextInt(5) - world.random.nextInt(5));
        if (hasChunksAt(livingEntity)
                && isEmptyBlock(world, blockpos$mutable, world.getBlockState(blockpos$mutable), world.getFluidState(blockpos$mutable), ModEntityType.ZOMBIE_MINION.get(), false)){
            return blockpos$mutable;
        } else {
            return livingEntity.blockPosition().mutable().move(0, (int) BlockFinder.moveDownToGround(livingEntity), 0);
        }
    }

    public static BlockPos SummonWaterRadius(LivingEntity livingEntity, World world){
        BlockPos.Mutable blockpos$mutable = livingEntity.blockPosition().mutable().move(0, 0, 0);
        blockpos$mutable.setX(blockpos$mutable.getX() + world.random.nextInt(5) - world.random.nextInt(5));
        blockpos$mutable.setY((int) BlockFinder.spawnWaterY(livingEntity, livingEntity.blockPosition()));
        blockpos$mutable.setZ(blockpos$mutable.getZ() + world.random.nextInt(5) - world.random.nextInt(5));
        if (hasChunksAt(livingEntity)
                && isEmptyBlock(world, blockpos$mutable, world.getBlockState(blockpos$mutable), world.getFluidState(blockpos$mutable), ModEntityType.ZOMBIE_MINION.get(), true)){
            return blockpos$mutable;
        } else {
            return livingEntity.blockPosition().mutable().move(0, (int) BlockFinder.spawnWaterY(livingEntity, livingEntity.blockPosition()), 0);
        }
    }

    public static boolean isInRain(World pLevel, BlockPos pPos){
        if (pLevel.getBiome(pPos).getPrecipitation() != Biome.RainType.NONE){
            return pLevel.isRaining() && pLevel.canSeeSky(pPos);
        } else {
            return false;
        }
    }

    public static boolean isDeadBlock(World pLevel, BlockPos pPos){
        BlockState blockState = pLevel.getBlockState(pPos.below());
        if (blockState.getFluidState().isEmpty()) {
            return blockState.getBlock() instanceof IDeadBlock;
        }
        return false;
    }

    public static boolean isScytheBreak(BlockState blockState){
        return (blockState.is(BlockTags.CROPS) || blockState.getBlock() instanceof BushBlock) && !(blockState.getBlock() instanceof StemBlock);
    }

    public static boolean biomeIsInOverworld(ResourceLocation resourceLocation){
        RegistryKey<Biome> biomeRegistryKey = RegistryKey.create(ForgeRegistries.Keys.BIOMES, resourceLocation);
        return BiomeDictionary.hasType(biomeRegistryKey, BiomeDictionary.Type.OVERWORLD);
    }

    public static boolean biomeIsInVanillaDim(ResourceLocation resourceLocation){
        RegistryKey<Biome> biomeRegistryKey = RegistryKey.create(ForgeRegistries.Keys.BIOMES, resourceLocation);
        return biomeIsInOverworld(resourceLocation) || BiomeDictionary.hasType(biomeRegistryKey, BiomeDictionary.Type.NETHER)
                || BiomeDictionary.hasType(biomeRegistryKey, BiomeDictionary.Type.END);
    }

    public static boolean createBarrel(IServerWorld pLevel, MutableBoundingBox pBounds, Random pRandom, BlockPos pPos, ResourceLocation pResourceLocation, @Nullable BlockState pState) {
        if (pBounds.isInside(pPos) && !pLevel.getBlockState(pPos).is(Blocks.BARREL)) {
            pLevel.setBlock(pPos, pState != null ? pState: Blocks.BARREL.defaultBlockState(), 2);
            TileEntity tileentity = pLevel.getBlockEntity(pPos);
            if (tileentity instanceof BarrelTileEntity) {
                ((BarrelTileEntity)tileentity).setLootTable(pResourceLocation, pRandom.nextLong());
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean createPithos(IServerWorld pLevel, MutableBoundingBox pBounds, Random pRandom, BlockPos pPos, ResourceLocation pResourceLocation, @Nullable BlockState pState) {
        if (pBounds.isInside(pPos) && !pLevel.getBlockState(pPos).is(ModBlocks.PITHOS_BLOCK.get())) {
            pLevel.setBlock(pPos, pState != null ? pState: ModBlocks.PITHOS_BLOCK.get().defaultBlockState(), 2);
            TileEntity tileentity = pLevel.getBlockEntity(pPos);
            if (tileentity instanceof PithosTileEntity) {
                ((PithosTileEntity)tileentity).setLootTable(pResourceLocation, pRandom.nextLong());
            }

            return true;
        } else {
            return false;
        }
    }

    public static BlockPos findBlockRadius(BlockPos oPos, int pX, int pY, int pZ){
        BlockPos blockPos = oPos;
        for (int j1 = -pX; j1 < pX; ++j1) {
            for (int k1 = -pY; k1 <= pY; ++k1) {
                for (int l1 = -pZ; l1 < pZ; ++l1) {
                    blockPos = oPos.offset(j1, k1, l1);
                }
            }
        }
        return blockPos;
    }

    public static BlockState findBlock(World world, BlockPos oPos, int pX, int pY, int pZ){
        return world.getBlockState(findBlockRadius(oPos, pX, pY, pZ));
    }

    /**
     * Ripped from @TelepathicGrunt's RepurposedStructure codes.
     */
    public static BlockPos getHighestLand(ChunkGenerator chunkGenerator, MutableBoundingBox boundingBox, boolean canBeOnLiquid) {
        BlockPos.Mutable mutable = new BlockPos.Mutable().set(
                boundingBox.getCenter().getX(),
                chunkGenerator.getGenDepth() - 20,
                boundingBox.getCenter().getZ());

        IBlockReader blockView = chunkGenerator.getBaseColumn(mutable.getX(), mutable.getZ());
        BlockState currentBlockstate;
        while (mutable.getY() > chunkGenerator.getSeaLevel()) {
            currentBlockstate = blockView.getBlockState(mutable);
            if (!currentBlockstate.canOcclude()) {
                mutable.move(Direction.DOWN);
                continue;
            }
            else if (blockView.getBlockState(mutable.offset(0, 3, 0)).getMaterial() == Material.AIR && (canBeOnLiquid ? !currentBlockstate.isAir() : currentBlockstate.canOcclude())) {
                return mutable;
            }
            mutable.move(Direction.DOWN);
        }

        return mutable;
    }

    /**
     * Ripped from @izofar's Bygone-Nether codes.
     */
    private static final Predicate<Block> isAir = (block) -> block == Blocks.AIR || block == Blocks.CAVE_AIR;

    public static boolean verticalSpace(ChunkGenerator chunkGenerator, int x, int z, int min, int max, int height) {
        IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(x, z);
        BlockPos.Mutable currentPos = new BlockPos.Mutable(x, max, z);

        int height_tracked = 0;
        while(currentPos.getY() >= min && height_tracked < height){
            if(isAir.test(columnOfBlocks.getBlockState(currentPos).getBlock())) {
                height_tracked++;
            } else {
                height_tracked = 0;
            }
            currentPos.move(Direction.DOWN);
        }
        return height_tracked == height;
    }

    public static boolean isLavaLake(ChunkGenerator chunkGenerator, int x, int z) {
        IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(x, z);
        BlockPos.Mutable currentPos = new BlockPos.Mutable(x, 31, z);

        boolean isLake = true;

        if(columnOfBlocks.getBlockState(currentPos).getBlock() != Blocks.LAVA) {
            isLake = false;
        } else while(currentPos.getY() < 70) {
            currentPos.move(Direction.UP);
            isLake = isLake && (isAir.test(columnOfBlocks.getBlockState(currentPos).getBlock()));
        }
        return isLake;
    }

    public static boolean isBuried(ChunkGenerator chunkGenerator, int x, int z, int min, int max) {
        IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(x, z);
        BlockPos.Mutable currentPos = new BlockPos.Mutable(x, min, z);

        boolean found = false;
        while(currentPos.getY() < max){
            if (isAir.test(columnOfBlocks.getBlockState(currentPos.above()).getBlock()) && !isAir.test(columnOfBlocks.getBlockState(currentPos).getBlock()))
                found = true;
            currentPos.move(Direction.UP);
        }
        return !found;
    }

    public static boolean findStructure(ServerWorld serverWorld, LivingEntity livingEntity, Structure<?> structure){
        StructureStart<?> structureStart = serverWorld.structureFeatureManager().getStructureAt(livingEntity.blockPosition(), true, structure);
        return structureStart.getBoundingBox().isInside(livingEntity.blockPosition());
    }

}
