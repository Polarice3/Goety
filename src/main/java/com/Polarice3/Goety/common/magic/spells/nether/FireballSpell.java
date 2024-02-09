package com.Polarice3.Goety.common.magic.spells.nether;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.projectiles.ModFireballEntity;
import com.Polarice3.Goety.common.magic.Spells;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Learned you could use this method for better projectile accuracy from codes by @Yunus1903
 */
public class FireballSpell extends Spells {

    @Override
    public int defaultSoulCost() {
        return SpellConfig.FireballCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return SpellConfig.FireballDuration.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.BLAZE_SHOOT;
    }

    @Override
    public int defaultSpellCooldown() {
        return SpellConfig.FireballCoolDown.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.NETHER;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.BURNING.get());
        return list;
    }

    @Override
    public void SpellResult(ServerWorld worldIn, LivingEntity entityLiving, boolean staff) {
        Vector3d vector3d = entityLiving.getViewVector( 1.0F);
        ModFireballEntity smallFireballEntity = new ModFireballEntity(worldIn,
                entityLiving.getX() + vector3d.x / 2,
                entityLiving.getEyeY() - 0.2,
                entityLiving.getZ() + vector3d.z / 2,
                vector3d.x,
                vector3d.y,
                vector3d.z);
        smallFireballEntity.setOwner(entityLiving);
        worldIn.addFreshEntity(smallFireballEntity);
        if (staff) {
            for (int i = 0; i < 2; ++i) {
                ModFireballEntity smallFireballEntity2 = new ModFireballEntity(worldIn,
                        entityLiving.getX() + vector3d.x / 2 + worldIn.random.nextGaussian(),
                        entityLiving.getEyeY() - 0.2,
                        entityLiving.getZ() + vector3d.z / 2 + worldIn.random.nextGaussian(),
                        vector3d.x,
                        vector3d.y,
                        vector3d.z);
                smallFireballEntity2.setOwner(entityLiving);
                worldIn.addFreshEntity(smallFireballEntity2);
            }
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), this.getSoundSource(), 1.0F, 1.0F);
    }
}
