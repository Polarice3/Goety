package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.projectiles.FangEntity;
import com.Polarice3.Goety.common.entities.projectiles.GhostFireEntity;
import com.Polarice3.Goety.common.items.magic.SoulWand;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

import java.util.Map;

public class WandUtil {
    private static boolean isMatchingItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof SoulWand;
    }

    public static ItemStack findWand(PlayerEntity playerEntity) {
        ItemStack foundStack = ItemStack.EMPTY;
        if (isMatchingItem(playerEntity.getMainHandItem())){
            foundStack = playerEntity.getMainHandItem();
        } else if (isMatchingItem(playerEntity.getOffhandItem())){
            foundStack = playerEntity.getOffhandItem();
        }

        return foundStack;
    }

    public static ItemStack findFocus(PlayerEntity playerEntity){
        ItemStack foundStack = ItemStack.EMPTY;
        if (!findWand(playerEntity).isEmpty()){
            if (!SoulWand.getFocus(findWand(playerEntity)).isEmpty()) {
                foundStack = SoulWand.getFocus(findWand(playerEntity));
            }
        }

        return foundStack;
    }

    public static boolean enchantedFocus(PlayerEntity playerEntity){
        return !findFocus(playerEntity).isEmpty() && findFocus(playerEntity).isEnchanted();
    }

    public static int getLevels(Enchantment enchantment, PlayerEntity playerEntity){
        if (enchantedFocus(playerEntity)) {
            return EnchantmentHelper.getItemEnchantmentLevel(enchantment, findFocus(playerEntity));
        } else {
            return 0;
        }
    }

    public static Map<Enchantment, Integer> getFocusEnchantments(PlayerEntity playerEntity){
        if (!findFocus(playerEntity).isEmpty()){
            ItemStack focus = findFocus(playerEntity);
            if (focus.isEnchanted()){
                return SoulWand.getFocusEnchantments(focus);
            }
        }

        return null;
    }

    public static void spawnFangs(LivingEntity livingEntity, double pPosX, double pPosZ, double PPPosY, double pOPosY, float pYRot, int pWarmUp) {
        BlockPos blockpos = new BlockPos(pPosX, pOPosY, pPosZ);
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
        } while(blockpos.getY() >= MathHelper.floor(PPPosY) - 1);

        if (flag) {
            FangEntity fangEntity = new FangEntity(livingEntity.level, pPosX, (double)blockpos.getY() + d0, pPosZ, pYRot, pWarmUp, livingEntity);
            if (livingEntity instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) livingEntity;
                if (WandUtil.enchantedFocus(player)){
                    if (WandUtil.getLevels(ModEnchantments.ABSORB.get(), player) != 0){
                        fangEntity.setAbsorbing(true);
                    }
                }
            }
            livingEntity.level.addFreshEntity(fangEntity);
        }

    }

    public static void spawnGhostFires(World world, BlockPos pPos, LivingEntity livingEntity){
        BlockPos blockPos2 = pPos.west();
        BlockPos blockPos3 = pPos.east();
        GhostFireEntity ghostFire1 = new GhostFireEntity(world, pPos, livingEntity);
        world.addFreshEntity(ghostFire1);
        GhostFireEntity ghostFire2 = new GhostFireEntity(world, blockPos2, livingEntity);
        world.addFreshEntity(ghostFire2);
        GhostFireEntity ghostFire3 = new GhostFireEntity(world, blockPos3, livingEntity);
        world.addFreshEntity(ghostFire3);
        GhostFireEntity ghostFire4 = new GhostFireEntity(world, pPos.north(), livingEntity);
        world.addFreshEntity(ghostFire4);
        GhostFireEntity ghostFire5 = new GhostFireEntity(world, pPos.south(), livingEntity);
        world.addFreshEntity(ghostFire5);
        GhostFireEntity ghostFire6 = new GhostFireEntity(world, blockPos2.north(), livingEntity);
        world.addFreshEntity(ghostFire6);
        GhostFireEntity ghostFire7 = new GhostFireEntity(world, blockPos2.south(), livingEntity);
        world.addFreshEntity(ghostFire7);
        GhostFireEntity ghostFire8 = new GhostFireEntity(world, blockPos3.north(), livingEntity);
        world.addFreshEntity(ghostFire8);
        GhostFireEntity ghostFire9 = new GhostFireEntity(world, blockPos3.south(), livingEntity);
        world.addFreshEntity(ghostFire9);
    }

    public static void spawnCrossGhostFires(World world, BlockPos pPos, LivingEntity livingEntity){
        BlockPos blockPos2 = pPos.west();
        BlockPos blockPos3 = pPos.east();
        BlockPos blockPos4 = pPos.north();
        BlockPos blockPos5 = pPos.south();
        GhostFireEntity ghostFire1 = new GhostFireEntity(world, pPos, livingEntity);
        world.addFreshEntity(ghostFire1);
        GhostFireEntity ghostFire2 = new GhostFireEntity(world, blockPos2, livingEntity);
        world.addFreshEntity(ghostFire2);
        GhostFireEntity ghostFire3 = new GhostFireEntity(world, blockPos3, livingEntity);
        world.addFreshEntity(ghostFire3);
        GhostFireEntity ghostFire4 = new GhostFireEntity(world, blockPos4, livingEntity);
        world.addFreshEntity(ghostFire4);
        GhostFireEntity ghostFire5 = new GhostFireEntity(world, blockPos5, livingEntity);
        world.addFreshEntity(ghostFire5);
        GhostFireEntity ghostFire6 = new GhostFireEntity(world, blockPos4.north(), livingEntity);
        world.addFreshEntity(ghostFire6);
        GhostFireEntity ghostFire7 = new GhostFireEntity(world, blockPos5.south(), livingEntity);
        world.addFreshEntity(ghostFire7);
        GhostFireEntity ghostFire8 = new GhostFireEntity(world, blockPos2.west(), livingEntity);
        world.addFreshEntity(ghostFire8);
        GhostFireEntity ghostFire9 = new GhostFireEntity(world, blockPos3.east(), livingEntity);
        world.addFreshEntity(ghostFire9);
    }

}
