package com.Polarice3.Goety.common.magic.spells;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.items.magic.RecallFocus;
import com.Polarice3.Goety.common.magic.Spells;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

public class RecallSpell extends Spells {
    @Override
    public int SoulCost() {
        return SpellConfig.RecallCost.get();
    }

    @Override
    public int CastDuration() {
        return SpellConfig.RecallDuration.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.PORTAL_TRIGGER;
    }

    @Override
    public SpellType getSpellType(){
        return SpellType.ENDER;
    }

    @Override
    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
            if (RecallFocus.isValid(worldIn, WandUtil.findFocus(player))) {
                RecallFocus.recall(player, WandUtil.findFocus(player));
            } else if (!RecallFocus.hasRecall(WandUtil.findFocus(player))){
                SEHelper.increaseSouls(player, this.SoulCost());
                player.displayClientMessage(new TranslationTextComponent("info.goety.focus.noPos"), true);
                worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            } else {
                SEHelper.increaseSouls(player, this.SoulCost());
                player.displayClientMessage(new TranslationTextComponent("info.goety.focus.PosInvalid"), true);
                worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        this.WandResult(worldIn, entityLiving);
    }
}
