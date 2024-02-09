package com.Polarice3.Goety.common.ritual;

import com.Polarice3.Goety.client.inventory.crafting.RitualRecipe;
import com.Polarice3.Goety.common.blocks.tiles.DarkAltarTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class CraftItemRitual extends Ritual{

    public CraftItemRitual(RitualRecipe recipe) {
        super(recipe);
    }

    @Override
    public void finish(World world, BlockPos darkAltarPos, DarkAltarTileEntity tileEntity,
                       PlayerEntity castingPlayer, ItemStack activationItem) {
        super.finish(world, darkAltarPos, tileEntity, castingPlayer, activationItem);

        activationItem.shrink(1);

        for(int i = 0; i < 20; ++i) {
            double d0 = (double)darkAltarPos.getX() + world.random.nextDouble();
            double d1 = (double)darkAltarPos.getY() + world.random.nextDouble();
            double d2 = (double)darkAltarPos.getZ() + world.random.nextDouble();
            world.addParticle(ParticleTypes.POOF, d0, d1, d2, 0, 0, 0);
        }

        ItemStack result = this.recipe.getResultItem().copy();
        result.onCraftedBy(world, castingPlayer, 1);
        IItemHandler handler = tileEntity.itemStackHandler.orElseThrow(RuntimeException::new);
        handler.insertItem(0, result.split(1), false);
    }
}
