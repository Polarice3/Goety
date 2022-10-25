package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.tileentities.SoulFangTotemTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeBlock;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SoulFangTotemBlock extends ContainerBlock implements IForgeBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public SoulFangTotemBlock() {
        super(Properties.of(Material.STONE)
                .strength(3.0F, 9.0F)
                .sound(SoundType.STONE)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.FALSE));
    }

    public void setEnchantments(ItemStack itemStack, TileEntity tileEntity){
        SoulFangTotemTileEntity soulFangTotemTile = (SoulFangTotemTileEntity) tileEntity;
        Map<Enchantment, Integer> enchantments = soulFangTotemTile.getEnchantments();
        for(Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            Integer integer = entry.getValue();
            if (integer < 0){
                enchantments.remove(enchantment);
            } else {
                enchantments.put(enchantment, integer);
            }
        }
        EnchantmentHelper.setEnchantments(enchantments, itemStack);
    }

    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        ItemStack itemStack = new ItemStack(this);
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof SoulFangTotemTileEntity) {
            this.setEnchantments(itemStack, tileEntity);
        }
        return itemStack;
    }

    public void playerDestroy(World pLevel, PlayerEntity pPlayer, BlockPos pPos, BlockState pState, @Nullable TileEntity pTe, ItemStack pStack) {
        ItemStack itemStack = new ItemStack(this);
        if (pTe instanceof SoulFangTotemTileEntity) {
            this.setEnchantments(itemStack, pTe);
        }
        popResource(pLevel, pPos, itemStack);
        super.playerDestroy(pLevel, pPlayer, pPos, pState, pTe, pStack);
    }

    public void setPlacedBy(World pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        TileEntity tileentity = pLevel.getBlockEntity(pPos);
        if (pPlacer instanceof PlayerEntity){
            if (tileentity instanceof SoulFangTotemTileEntity){
                SoulFangTotemTileEntity soulFangTotemTile = (SoulFangTotemTileEntity) tileentity;
                soulFangTotemTile.setOwnerId(pPlacer.getUUID());
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(pStack);
                for(Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    Enchantment enchantment = entry.getKey();
                    if (!this.asItem().canApplyAtEnchantingTable(pStack, enchantment)){
                        enchantments.remove(enchantment);
                    }
                }
                soulFangTotemTile.getEnchantments().putAll(enchantments);
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Collections.emptyList();
    }

    public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new SoulFangTotemTileEntity();
    }
}
