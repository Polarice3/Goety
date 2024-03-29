package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModTags;
import com.Polarice3.Goety.utils.EffectsUtil;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class HauntedBushBlock extends BushBlock implements net.minecraftforge.common.IForgeShearable{
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

    public HauntedBushBlock() {
        super(Properties.of(Material.REPLACEABLE_PLANT)
                .noCollission()
                .instabreak()
                .sound(SoundType.GRASS)
        );
    }

    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return SHAPE;
    }

    public void entityInside(BlockState pState, World pLevel, BlockPos pPos, Entity pEntity) {
        if (pEntity instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) pEntity;
            if (pEntity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) livingEntity;
                if (MobUtil.playerValidity(player, true)) {
                    corruption(livingEntity);
                }
            } else {
                corruption(livingEntity);
            }
        }
    }

    public void corruption(LivingEntity livingEntity){
        if (!RobeArmorFinder.FindNecroBootsofWander(livingEntity)) {
            if (livingEntity.hasEffect(ModEffects.DESICCATE.get())) {
                EffectsUtil.resetDuration(livingEntity, ModEffects.DESICCATE.get(), 400);
            } else {
                livingEntity.addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 400));
            }
        }
    }

    public PathNodeType getAiPathNodeType(BlockState state, IBlockReader world, BlockPos pos, @Nullable MobEntity entity) {
        return entity != null && entity.getMobType() == CreatureAttribute.UNDEAD ? null : PathNodeType.DAMAGE_OTHER;
    }

    protected boolean mayPlaceOn(BlockState pState, IBlockReader pLevel, BlockPos pPos) {
        return pState.is(Blocks.GRASS_BLOCK)
                || pState.is(Blocks.DIRT)
                || pState.is(Blocks.COARSE_DIRT)
                || pState.is(Blocks.PODZOL)
                || pState.is(Blocks.FARMLAND)
                || pState.is(BlockTags.SAND)
                || pState.is(ModTags.Blocks.DEAD_SANDS)
                || pState.is(ModTags.Blocks.TERRACOTTAS);
    }
}
