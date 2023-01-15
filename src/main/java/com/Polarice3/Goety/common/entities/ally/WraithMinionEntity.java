package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.common.entities.neutral.AbstractWraithEntity;
import com.Polarice3.Goety.init.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WraithMinionEntity extends AbstractWraithEntity {
    private static final DataParameter<Boolean> DATA_INTERESTED_ID = EntityDataManager.defineId(WraithMinionEntity.class, DataSerializers.BOOLEAN);
    private float interestTime;

    public WraithMinionEntity(EntityType<? extends SummonedEntity> p_i48553_1_, World p_i48553_2_) {
        super(p_i48553_1_, p_i48553_2_);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(8, new WanderGoal(this, 1.0D, 10));
    }

    public void tick() {
        if (this.isAlive()) {
            if (this.isInterested()) {
                --this.interestTime;
            }
            if (this.interestTime <= 0){
                this.setIsInterested(false);
            }
        }
        super.tick();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_INTERESTED_ID, false);
    }

    public void setIsInterested(boolean pBeg) {
        this.entityData.set(DATA_INTERESTED_ID, pBeg);
    }

    public boolean isInterested() {
        return this.entityData.get(DATA_INTERESTED_ID);
    }

    public ActionResultType mobInteract(PlayerEntity pPlayer, Hand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
            if (!this.isInterested()) {
                if (itemstack.isEmpty() || itemstack == ItemStack.EMPTY) {
                    this.setIsInterested(true);
                    this.interestTime = 40;
                    this.level.broadcastEntityEvent(this, (byte) 102);
                    this.playSound(ModSounds.WRAITH_AMBIENT.get(), 1.0F, 2.0F);
                    this.heal(1.0F);
                    return ActionResultType.sidedSuccess(this.level.isClientSide);
                }
            }
        }
        return ActionResultType.PASS;
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte pId) {
        super.handleEntityEvent(pId);
        if (pId == 102){
            this.setIsInterested(true);
            this.interestTime = 40;
            this.playSound(ModSounds.WRAITH_AMBIENT.get(), 1.0F, 2.0F);
            this.addParticlesAroundSelf(ParticleTypes.HEART);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void addParticlesAroundSelf(IParticleData pParticleData) {
        for(int i = 0; i < 5; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(pParticleData, this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), d0, d1, d2);
        }

    }

}
