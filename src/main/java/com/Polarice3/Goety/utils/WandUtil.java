package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.projectiles.BlightFireEntity;
import com.Polarice3.Goety.common.entities.projectiles.FangEntity;
import com.Polarice3.Goety.common.entities.projectiles.GhostFireEntity;
import com.Polarice3.Goety.common.entities.utilities.SummonCircleEntity;
import com.Polarice3.Goety.common.items.magic.SoulWand;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
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

    public static void spawnGhostFires(World world, Vector3d pPos, LivingEntity livingEntity){
        Vector3d vector3d = Vector3dUtil.west(pPos);
        Vector3d vector3d1 = Vector3dUtil.east(pPos);
        GhostFireEntity ghostFire1 = new GhostFireEntity(world, pPos, livingEntity);
        world.addFreshEntity(ghostFire1);
        GhostFireEntity ghostFire2 = new GhostFireEntity(world, vector3d, livingEntity);
        world.addFreshEntity(ghostFire2);
        GhostFireEntity ghostFire3 = new GhostFireEntity(world, vector3d1, livingEntity);
        world.addFreshEntity(ghostFire3);
        GhostFireEntity ghostFire4 = new GhostFireEntity(world, Vector3dUtil.north(pPos), livingEntity);
        world.addFreshEntity(ghostFire4);
        GhostFireEntity ghostFire5 = new GhostFireEntity(world, Vector3dUtil.south(pPos), livingEntity);
        world.addFreshEntity(ghostFire5);
        GhostFireEntity ghostFire6 = new GhostFireEntity(world, Vector3dUtil.north(vector3d), livingEntity);
        world.addFreshEntity(ghostFire6);
        GhostFireEntity ghostFire7 = new GhostFireEntity(world, Vector3dUtil.south(vector3d), livingEntity);
        world.addFreshEntity(ghostFire7);
        GhostFireEntity ghostFire8 = new GhostFireEntity(world, Vector3dUtil.north(vector3d1), livingEntity);
        world.addFreshEntity(ghostFire8);
        GhostFireEntity ghostFire9 = new GhostFireEntity(world, Vector3dUtil.south(vector3d1), livingEntity);
        world.addFreshEntity(ghostFire9);
    }

    public static void spawnCrossGhostFires(World world, Vector3d pPos, LivingEntity livingEntity){
        GhostFireEntity ghostFire1 = new GhostFireEntity(world, pPos, livingEntity);
        world.addFreshEntity(ghostFire1);
        GhostFireEntity ghostFire2 = new GhostFireEntity(world, Vector3dUtil.west(pPos), livingEntity);
        world.addFreshEntity(ghostFire2);
        GhostFireEntity ghostFire3 = new GhostFireEntity(world, Vector3dUtil.east(pPos), livingEntity);
        world.addFreshEntity(ghostFire3);
        GhostFireEntity ghostFire4 = new GhostFireEntity(world, Vector3dUtil.north(pPos), livingEntity);
        world.addFreshEntity(ghostFire4);
        GhostFireEntity ghostFire5 = new GhostFireEntity(world, Vector3dUtil.south(pPos), livingEntity);
        world.addFreshEntity(ghostFire5);
    }

    public static void spawnBlightFires(World world, Vector3d pPos, LivingEntity livingEntity){
        Vector3d north = Vector3dUtil.north(pPos);
        Vector3d south = Vector3dUtil.south(pPos);
        Vector3d west = Vector3dUtil.west(pPos);
        Vector3d east = Vector3dUtil.east(pPos);
        BlightFireEntity ghostFire1 = new BlightFireEntity(world, pPos, livingEntity);
        world.addFreshEntity(ghostFire1);
        BlightFireEntity ghostFire2 = new BlightFireEntity(world, west, livingEntity);
        world.addFreshEntity(ghostFire2);
        BlightFireEntity ghostFire3 = new BlightFireEntity(world, east, livingEntity);
        world.addFreshEntity(ghostFire3);
        BlightFireEntity ghostFire4 = new BlightFireEntity(world, Vector3dUtil.north(pPos), livingEntity);
        world.addFreshEntity(ghostFire4);
        BlightFireEntity ghostFire5 = new BlightFireEntity(world, Vector3dUtil.south(pPos), livingEntity);
        world.addFreshEntity(ghostFire5);
        BlightFireEntity ghostFire6 = new BlightFireEntity(world, Vector3dUtil.north(west), livingEntity);
        world.addFreshEntity(ghostFire6);
        BlightFireEntity ghostFire7 = new BlightFireEntity(world, Vector3dUtil.south(west), livingEntity);
        world.addFreshEntity(ghostFire7);
        BlightFireEntity ghostFire8 = new BlightFireEntity(world, Vector3dUtil.north(east), livingEntity);
        world.addFreshEntity(ghostFire8);
        BlightFireEntity ghostFire9 = new BlightFireEntity(world, Vector3dUtil.south(east), livingEntity);
        world.addFreshEntity(ghostFire9);
        BlightFireEntity ghostFire10 = new BlightFireEntity(world, Vector3dUtil.north(north), livingEntity);
        world.addFreshEntity(ghostFire10);
        BlightFireEntity ghostFire11 = new BlightFireEntity(world, Vector3dUtil.south(south), livingEntity);
        world.addFreshEntity(ghostFire11);
        BlightFireEntity ghostFire12 = new BlightFireEntity(world, Vector3dUtil.west(west), livingEntity);
        world.addFreshEntity(ghostFire12);
        BlightFireEntity ghostFire13 = new BlightFireEntity(world, Vector3dUtil.east(east), livingEntity);
        world.addFreshEntity(ghostFire13);
    }

    public static void spawnXBlightFires(World world, Vector3d pPos, LivingEntity livingEntity){
        Vector3d west = Vector3dUtil.west(pPos);
        Vector3d east = Vector3dUtil.east(pPos);
        BlightFireEntity ghostFire1 = new BlightFireEntity(world, pPos, livingEntity);
        world.addFreshEntity(ghostFire1);
        BlightFireEntity ghostFire6 = new BlightFireEntity(world, Vector3dUtil.north(west), livingEntity);
        world.addFreshEntity(ghostFire6);
        BlightFireEntity ghostFire7 = new BlightFireEntity(world, Vector3dUtil.south(west), livingEntity);
        world.addFreshEntity(ghostFire7);
        BlightFireEntity ghostFire8 = new BlightFireEntity(world, Vector3dUtil.north(east), livingEntity);
        world.addFreshEntity(ghostFire8);
        BlightFireEntity ghostFire9 = new BlightFireEntity(world, Vector3dUtil.south(east), livingEntity);
        world.addFreshEntity(ghostFire9);
        BlightFireEntity ghostFire10 = new BlightFireEntity(world, Vector3dUtil.north(Vector3dUtil.west(Vector3dUtil.north(west))), livingEntity);
        world.addFreshEntity(ghostFire10);
        BlightFireEntity ghostFire11 = new BlightFireEntity(world, Vector3dUtil.south(Vector3dUtil.west(Vector3dUtil.south(west))), livingEntity);
        world.addFreshEntity(ghostFire11);
        BlightFireEntity ghostFire12 = new BlightFireEntity(world, Vector3dUtil.north(Vector3dUtil.east(Vector3dUtil.north(east))), livingEntity);
        world.addFreshEntity(ghostFire12);
        BlightFireEntity ghostFire13 = new BlightFireEntity(world, Vector3dUtil.south(Vector3dUtil.east(Vector3dUtil.south(east))), livingEntity);
        world.addFreshEntity(ghostFire13);
    }

    public static void summoningCircles(World world, LivingEntity pOwner, Vector3d pPos, Entity summon1, Entity summon2, Entity summon3, Entity summon4){
        Vector3d north = Vector3dUtil.north(pPos, 2);
        Vector3d south = Vector3dUtil.south(pPos, 2);
        Vector3d west = Vector3dUtil.west(pPos, 2);
        Vector3d east = Vector3dUtil.east(pPos, 2);
        SummonCircleEntity circle1 = new SummonCircleEntity(world, north, summon1, true, pOwner);
        world.addFreshEntity(circle1);
        SummonCircleEntity circle2 = new SummonCircleEntity(world, south, summon2, true, pOwner);
        world.addFreshEntity(circle2);
        SummonCircleEntity circle3 = new SummonCircleEntity(world, west, summon3, true, pOwner);
        world.addFreshEntity(circle3);
        SummonCircleEntity circle4 = new SummonCircleEntity(world, east, summon4, true, pOwner);
        world.addFreshEntity(circle4);
    }

}
