package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.infamy.IInfamy;
import com.Polarice3.Goety.utils.InfamyHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public abstract class Spells {

    public Spells(){
    }

    public abstract int SoulCost();

    public abstract int CastDuration();

    public abstract SoundEvent CastingSound();

    public abstract void WandResult(World worldIn, LivingEntity entityLiving);

    public abstract void StaffResult(World worldIn, LivingEntity entityLiving);

    public void IncreaseInfamy(int random, PlayerEntity player){
        if (MainConfig.InfamySpell.get()){
            if (random != 0) {
                int random2 = player.level.random.nextInt(random);
                if (random2 == 0) {
                    IInfamy infamy1 = InfamyHelper.getCapability(player);
                    infamy1.increaseInfamy(MainConfig.InfamySpellGive.get());
                }
            }
        }
    }

}
