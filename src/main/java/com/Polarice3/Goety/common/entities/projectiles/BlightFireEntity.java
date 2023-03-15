package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.client.render.BlightFireTextures;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.neutral.AbstractWraithEntity;
import com.Polarice3.Goety.common.entities.neutral.IOwned;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.EffectsUtil;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;

public class BlightFireEntity extends GhostFireEntity{
    public BlightFireEntity(EntityType<? extends Entity> p_i50170_1_, World p_i50170_2_) {
        super(p_i50170_1_, p_i50170_2_);
    }

    public BlightFireEntity(World world, Vector3d vector3d, @Nullable LivingEntity owner) {
        this(ModEntityType.BLIGHT_FIRE.get(), world);
        this.setOwner(owner);
        this.setPos(vector3d.x(), vector3d.y(), vector3d.z());
    }

    public ResourceLocation getResourceLocation() {
        return BlightFireTextures.TEXTURES.getOrDefault(this.getAnimation(), BlightFireTextures.TEXTURES.get(0));
    }

    public void dealDamageTo(LivingEntity target) {
        LivingEntity owner = this.getOwner();
        int burning = 0;
        float damage = 4.0F;
        if (this.tickCount >= 14){
            damage = 2.0F;
        }
        if (target.isAlive() && !target.isInvulnerable() && target != owner) {
            if (target.fireImmune()){
                return;
            }
            if (owner == null) {
                if (target.hurt(DamageSource.IN_FIRE, damage)) {
                    if (target.hasEffect(ModEffects.DESICCATE.get())) {
                        int d2 = Objects.requireNonNull(target.getEffect(ModEffects.DESICCATE.get())).getDuration();
                        EffectsUtil.resetDuration(target, ModEffects.DESICCATE.get(), Math.max(d2, 200));
                    } else {
                        target.addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 200));
                    }
                }
            } else {
                if (owner instanceof MobEntity) {
                    MobEntity mob = (MobEntity) owner;
                    if (mob instanceof IMob && target instanceof IMob) {
                        if (mob.getTarget() != target) {
                            return;
                        }
                    }
                    if (mob instanceof IOwned){
                        IOwned owned = (IOwned) mob;
                        if (owned.getTrueOwner() != null){
                            if (target.isAlliedTo(owned.getTrueOwner()) || owned.getTrueOwner().isAlliedTo(target) || target == owned.getTrueOwner()){
                                return;
                            }
                        }
                    }
                } else {
                    if (target.isAlliedTo(owner)){
                        return;
                    }
                    if (owner.isAlliedTo(target)) {
                        return;
                    }
                }
                if (owner instanceof PlayerEntity){
                    PlayerEntity player = (PlayerEntity) owner;
                    if (WandUtil.enchantedFocus(player)) {
                        damage += WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                        burning += WandUtil.getLevels(ModEnchantments.BURNING.get(), player);
                    }
                } else {
                    if (owner.getAttribute(Attributes.ATTACK_DAMAGE) != null){
                        damage = (float) owner.getAttributeValue(Attributes.ATTACK_DAMAGE);
                        if (this.tickCount >= 14){
                            damage /= 2.0F;
                        }
                    }
                    if (owner instanceof AbstractWraithEntity){
                        AbstractWraithEntity wraith = (AbstractWraithEntity) owner;
                        burning = wraith.getBurningLevel();
                    }
                }
                if (target.hurt(ModDamageSource.magicFire(this, owner), damage)){
                    target.setSecondsOnFire(5 * burning);
                    if (target.hasEffect(ModEffects.DESICCATE.get())){
                        int d2 = Objects.requireNonNull(target.getEffect(ModEffects.DESICCATE.get())).getDuration();
                        EffectsUtil.resetDuration(target, ModEffects.DESICCATE.get(), Math.max(d2, 200));
                    } else {
                        target.addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 200));
                    }
                    owner.heal(damage);
                    if (owner instanceof PlayerEntity) {
                        if (this.isSoulEating()) {
                            SEHelper.increaseSouls((PlayerEntity) owner, 1);
                        }
                    }
                }
            }
        }
    }
}
