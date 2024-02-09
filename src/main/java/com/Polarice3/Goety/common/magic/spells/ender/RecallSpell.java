package com.Polarice3.Goety.common.magic.spells.ender;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.items.magic.RecallFocus;
import com.Polarice3.Goety.common.magic.Spells;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

public class RecallSpell extends Spells {
    @Override
    public int defaultSoulCost() {
        return SpellConfig.RecallCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return SpellConfig.RecallDuration.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return SpellConfig.RecallCoolDown.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return null;
    }

    @Override
    public SoundEvent loopSound(LivingEntity entityLiving) {
        return SoundEvents.PORTAL_TRIGGER;
    }

    public boolean conditionsMet(ServerWorld worldIn, LivingEntity entityLiving){
        if (entityLiving instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
            if (RecallFocus.isValid(worldIn, WandUtil.findFocus(player))) {
                return true;
            } else if (!RecallFocus.hasRecall(WandUtil.findFocus(player))){
                player.displayClientMessage(new TranslationTextComponent("info.goety.focus.noPos"), true);
            } else {
                player.displayClientMessage(new TranslationTextComponent("info.goety.focus.PosInvalid"), true);
            }
        }
        return false;
    }

    public void SpellResult(ServerWorld worldIn, LivingEntity entityLiving, boolean staff){
        if (RecallFocus.isValid(worldIn, WandUtil.findFocus(entityLiving))) {
            RecallFocus.recall(entityLiving, WandUtil.findFocus(entityLiving));
        }
    }
}
