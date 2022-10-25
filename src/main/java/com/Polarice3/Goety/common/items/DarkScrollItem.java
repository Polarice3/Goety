package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.bosses.VizierEntity;
import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class DarkScrollItem extends Item {
    public DarkScrollItem() {
        super(new Properties().tab(Goety.TAB).stacksTo(1));
    }

    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        super.finishUsingItem(stack, worldIn, entityLiving);
        boolean flag = false;
        if (worldIn instanceof ServerWorld){
            BlockPos blockpos = ((ServerWorld)worldIn).getChunkSource().getGenerator().findNearestMapFeature((ServerWorld)worldIn, Structure.WOODLAND_MANSION, entityLiving.blockPosition(), 100, false);
            if (blockpos != null){
                if (entityLiving.distanceToSqr(blockpos.getX(), worldIn.getHeight(Heightmap.Type.WORLD_SURFACE, blockpos.getX(), blockpos.getZ()), blockpos.getZ()) <= 4096 && !worldIn.canSeeSky(entityLiving.blockPosition())){
                    worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.RAID_HORN, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    VizierEntity vizier = ModEntityType.VIZIER.get().create(worldIn);
                    if (vizier != null) {
                        vizier.setPos(entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ());
                        vizier.finalizeSpawn((IServerWorld) worldIn, worldIn.getCurrentDifficultyAt(entityLiving.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        vizier.makeInvulnerable();
                        worldIn.addFreshEntity(vizier);
                        if (!(entityLiving instanceof PlayerEntity && ((PlayerEntity) entityLiving).isCreative())) {
                            stack.setCount(0);
                        }
                    }
                } else {
                    flag = true;
                }
            } else {
                flag = true;
            }
        } else {
            flag = true;
        }

        if (flag){
            if (entityLiving instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) entityLiving;
                player.displayClientMessage(new TranslationTextComponent("info.goety.items.dark_scroll.failure"), true);
            }
            worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        }

        return stack;
    }

    public int getUseDuration(ItemStack stack) {
        return 25;
    }

    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        playerIn.startUsingItem(handIn);
        if (!worldIn.isClientSide){
            ServerWorld serverWorld = (ServerWorld) worldIn;
            serverWorld.sendParticles(ParticleTypes.ANGRY_VILLAGER, playerIn.getX(), playerIn.getY(), playerIn.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
        }
        return ActionResult.consume(itemstack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("info.goety.item.dark_scroll"));
    }
}
