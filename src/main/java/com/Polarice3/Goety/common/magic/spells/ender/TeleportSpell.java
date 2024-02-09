package com.Polarice3.Goety.common.magic.spells.ender;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spells;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class TeleportSpell extends Spells {

    @Override
    public int defaultSoulCost() {
        return SpellConfig.TeleportCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return SpellConfig.TeleportDuration.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.ENDERMAN_TELEPORT;
    }

    @Override
    public int defaultSpellCooldown() {
        return SpellConfig.TeleportCoolDown.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.ENDER;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.RANGE.get());
        return list;
    }

    @Override
    public void SpellResult(ServerWorld worldIn, LivingEntity entityLiving, boolean staff) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            int enchantment = 0;
            if (WandUtil.enchantedFocus(player)) {
                enchantment = WandUtil.getLevels(ModEnchantments.RANGE.get(), player);
            }
            Vector3d vec3 = findTeleportLocation(worldIn, player, 32 + enchantment);
            BlockPos blockPos = new BlockPos(vec3);
            enderTeleportEvent(entityLiving, worldIn, blockPos);
            worldIn.broadcastEntityEvent(player, (byte) 46);
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), this.getSoundSource(), 2.0F, 1.0F);
    }

    /**
     * Teleport Location code based of @iron43's codes: <a href="https://github.com/iron431/Irons-Spells-n-Spellbooks/blob/1.19.2/src/main/java/io/redspace/ironsspellbooks/spells/ender/TeleportSpell.java">...</a>
     */
    public static Vector3d findTeleportLocation(ServerWorld level, LivingEntity livingEntity, float maxDistance) {
        BlockRayTraceResult blockHitResult = MobUtil.rayTrace(livingEntity, maxDistance, false);
        BlockPos pos = blockHitResult.getBlockPos();

        Vector3d bbOffset = livingEntity.getForward().normalize().multiply(livingEntity.getBbWidth() / 3.0D, 0.0D, livingEntity.getBbHeight() / 3.0D);
        Vector3d bbImpact = blockHitResult.getLocation().subtract(bbOffset);

        int ledgeY = (int) level.clip(new RayTraceContext(Vector3d.atBottomCenterOf(pos).add(0.0D, 3.0D, 0.0D), Vector3d.atBottomCenterOf(pos), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, null)).getLocation().y;

        Vector3d correctedPos = new Vector3d(pos.getX(), ledgeY, pos.getZ());

        boolean isAir = level.getBlockState(new BlockPos(correctedPos)).isAir();
        boolean los = level.clip(new RayTraceContext(bbImpact, bbImpact.add(0.0D, ledgeY - pos.getY(), 0.0D), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, livingEntity)).getType() == RayTraceResult.Type.MISS;

        if (isAir && los && Math.abs(ledgeY - pos.getY()) <= 3) {
            return correctedPos.add(0.5D, 0.076D, 0.5D);
        } else {
            return level.clip(new RayTraceContext(bbImpact, bbImpact.add(0.0D, -livingEntity.getEyeHeight(), 0.0D), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, livingEntity)).getLocation().add(0.0D, 0.076D, 0.0D);
        }

    }

    public static void enderTeleportEvent(LivingEntity player, World world, BlockPos target) {
        player.teleportTo(target.getX(), target.getY(), target.getZ());
        player.fallDistance = 0.0F;
    }

}
