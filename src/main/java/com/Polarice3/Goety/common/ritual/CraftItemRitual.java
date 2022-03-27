package com.Polarice3.Goety.common.ritual;

import com.Polarice3.Goety.client.inventory.crafting.RitualRecipe;
import com.Polarice3.Goety.common.tileentities.DarkAltarTileEntity;
import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CraftItemRitual extends Ritual{

    public CraftItemRitual(RitualRecipe recipe) {
        super(recipe);
    }

    @Override
    public void finish(World world, BlockPos darkAltarPos, DarkAltarTileEntity tileEntity,
                       PlayerEntity castingPlayer, ItemStack activationItem) {
        super.finish(world, darkAltarPos, tileEntity, castingPlayer, activationItem);

        activationItem.shrink(1);

        new ParticleUtil(ParticleTypes.LARGE_SMOKE, darkAltarPos.getX() + 0.5,
                darkAltarPos.getY() + 0.5, darkAltarPos.getZ() + 0.5, 0, 0, 0);

        ItemStack result = this.recipe.getResultItem().copy();
        this.dropResult(world, darkAltarPos, tileEntity, castingPlayer, result);
    }
}
