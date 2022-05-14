package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.infamy.IInfamy;
import com.Polarice3.Goety.utils.InfamyHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

import javax.annotation.Nonnull;
import java.util.Locale;

public class ScryingMirrorItem extends ItemBase{

    @Nonnull
    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand){
        World world = player.getEntity().level;
        if (target instanceof ChickenEntity){
            IInfamy infamy = InfamyHelper.getCapability(player);
            int i = infamy.getInfamy();
            player.displayClientMessage(new TranslationTextComponent("info.goety.infamy.amount", i), true);
            return ActionResultType.SUCCESS;
        }
        if (target instanceof CatEntity){
            if (!world.isClientSide){
                ServerWorld serverWorld = (ServerWorld) player.getEntity().level;
                if (serverWorld.isVillage(target.blockPosition())){
                    if (player.getOffhandItem().getItem() == Items.MAP){
                        player.getOffhandItem().shrink(1);
                        BlockPos blockpos = serverWorld.findNearestMapFeature(Structure.WOODLAND_MANSION, player.blockPosition(), 100, true);
                        assert blockpos != null;
                        ItemStack itemstack = FilledMapItem.create(serverWorld, blockpos.getX(), blockpos.getZ(), (byte)2, true, true);
                        FilledMapItem.renderBiomePreviewMap(serverWorld, itemstack);
                        MapData.addTargetDecoration(itemstack, blockpos, "+", MapDecoration.Type.MANSION);
                        itemstack.setHoverName(new TranslationTextComponent("filled_map." + Structure.WOODLAND_MANSION.getFeatureName().toLowerCase(Locale.ROOT)));
                        player.addItem(itemstack);
                        IInfamy infamy = InfamyHelper.getCapability(player);
                        infamy.increaseInfamy(MainConfig.ScryingInfamy.get());
                        InfamyHelper.sendInfamyUpdatePacket(player);
                    } else {
                        IInfamy infamy = InfamyHelper.getCapability(player);
                        int i = infamy.getInfamy();
                        player.displayClientMessage(new TranslationTextComponent("info.goety.infamy.amount", i), true);
                    }
                } else {
                    IInfamy infamy = InfamyHelper.getCapability(player);
                    int i = infamy.getInfamy();
                    player.displayClientMessage(new TranslationTextComponent("info.goety.infamy.amount", i), true);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return super.interactLivingEntity(stack, player, target, hand);

    }
}
