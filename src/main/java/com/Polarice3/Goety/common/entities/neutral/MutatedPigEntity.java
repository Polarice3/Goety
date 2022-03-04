package com.Polarice3.Goety.common.entities.neutral;

import com.Polarice3.Goety.init.ModRegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class MutatedPigEntity extends MutatedEntity {
    private static final Ingredient TEMPTATION_ITEMS = Ingredient.of(Items.CARROT, Items.POTATO, Items.BEETROOT);

    public MutatedPigEntity(EntityType<? extends MutatedPigEntity> p_i50250_1_, World p_i50250_2_) {
        super(p_i50250_1_, p_i50250_2_);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, false, TEMPTATION_ITEMS));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIG_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.PIG_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PIG_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.PIG_STEP, 0.15F, 1.0F);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        int random = this.level.random.nextInt(16);
        if (pSource.getEntity() != null && pSource.getEntity() instanceof LivingEntity) {
            if (random == 0) {
                ((LivingEntity) pSource.getEntity()).addEffect(new EffectInstance(Effects.LEVITATION, 200));
            }
        }
        return super.hurt(pSource, pAmount);
    }

    public void thunderHit(ServerWorld world, LightningBoltEntity lightning) {
        if (world.getDifficulty() != Difficulty.PEACEFUL && net.minecraftforge.event.ForgeEventFactory.canLivingConvert(this, EntityType.CREEPER, (timer) -> {})) {
            CreeperEntity zombifiedpiglinentity = EntityType.CREEPER.create(world);
            zombifiedpiglinentity.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
            zombifiedpiglinentity.setNoAi(this.isNoAi());
            if (this.hasCustomName()) {
                zombifiedpiglinentity.setCustomName(this.getCustomName());
                zombifiedpiglinentity.setCustomNameVisible(this.isCustomNameVisible());
            }

            zombifiedpiglinentity.setPersistenceRequired();
            net.minecraftforge.event.ForgeEventFactory.onLivingConvert(this, zombifiedpiglinentity);
            level.addFreshEntity(zombifiedpiglinentity);
            this.remove();
        } else {
            super.thunderHit(world, lightning);
        }

    }

    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropCustomDeathLoot(source, looting, recentlyHitIn);
        int random = this.level.random.nextInt(4);
        if (random == 1 || looting > 2) {
            this.spawnAtLocation(ModRegistryHandler.MUTATED_PORKCHOP_UNCOOKED.get());
        } else {
            for (int i = 0; i < 4 + this.level.random.nextInt(8); ++i) {
                this.spawnAtLocation(Items.ROTTEN_FLESH);
            }
        }

    }

}
