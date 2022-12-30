package com.Polarice3.Goety.common.entities.hostile.cultists;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.bosses.ApostleEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;

public abstract class SpellcastingCultistEntity extends AbstractCultistEntity{
    private static final DataParameter<Byte> SPELL = EntityDataManager.defineId(SpellcastingCultistEntity.class, DataSerializers.BYTE);
    protected int spellTicks;
    private SpellType activeSpell = SpellType.NONE;

    protected SpellcastingCultistEntity(EntityType<? extends SpellcastingCultistEntity> type, World p_i48551_2_) {
        super(type, p_i48551_2_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPELL, (byte)0);
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.spellTicks = compound.getInt("SpellTicks");
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("SpellTicks", this.spellTicks);
    }

    public boolean isSpellcasting() {
        if (this.level.isClientSide) {
            return this.entityData.get(SPELL) > 0;
        } else {
            return this.spellTicks > 0;
        }
    }

    public void setSpellType(SpellType spellType) {
        this.activeSpell = spellType;
        this.entityData.set(SPELL, (byte)spellType.id);
    }

    protected SpellType getSpellType() {
        return !this.level.isClientSide ? this.activeSpell : SpellType.getFromId(this.entityData.get(SPELL));
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.spellTicks > 0) {
            --this.spellTicks;
        }

    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide && this.isSpellcasting()) {
            SpellType SpellcastingCultistEntity$spelltype = this.getSpellType();
            double d0 = SpellcastingCultistEntity$spelltype.particleSpeed[0];
            double d1 = SpellcastingCultistEntity$spelltype.particleSpeed[1];
            double d2 = SpellcastingCultistEntity$spelltype.particleSpeed[2];
            if (this.getEntity() instanceof ApostleEntity){
                float f = this.yBodyRot * ((float)Math.PI / 180F) + MathHelper.cos((float)this.tickCount * 0.6662F) * 0.25F;
                float f1 = MathHelper.cos(f);
                float f2 = MathHelper.sin(f);
                if (this.getMainArm() == HandSide.RIGHT){
                    this.level.addParticle(ModParticleTypes.CULT_SPELL.get(), this.getX() + (double)f1 * 0.6D, this.getY() + 1.8D, this.getZ() + (double)f2 * 0.6D, d0, d1, d2);
                } else {
                    this.level.addParticle(ModParticleTypes.CULT_SPELL.get(), this.getX() - (double)f1 * 0.6D, this.getY() + 1.8D, this.getZ() - (double)f2 * 0.6D, d0, d1, d2);
                }
            } else {
                for (int i = 0; i < this.level.random.nextInt(35) + 10; ++i) {
                    double d = this.level.random.nextGaussian() * 0.2D;
                    this.level.addParticle(ModParticleTypes.CULT_SPELL.get(), this.getX(), this.getEyeY(), this.getZ(), d0, d1, d2);
                }
            }
        }

    }

    protected int getSpellTicks() {
        return this.spellTicks;
    }

    protected abstract SoundEvent getCastingSoundEvent ();

    public class CastingASpellGoal extends Goal {
        public CastingASpellGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            return SpellcastingCultistEntity.this.getSpellTicks() > 0;
        }

        public void start() {
            super.start();
            SpellcastingCultistEntity.this.navigation.stop();
        }

        public void stop() {
            super.stop();
            SpellcastingCultistEntity.this.setSpellType(SpellType.NONE);
        }

        public void tick() {
            if (SpellcastingCultistEntity.this.getTarget() != null) {
                SpellcastingCultistEntity.this.getLookControl().setLookAt(SpellcastingCultistEntity.this.getTarget(), (float) SpellcastingCultistEntity.this.getMaxHeadYRot(), (float) SpellcastingCultistEntity.this.getMaxHeadXRot());
            }

        }
    }

    public enum SpellType {
        NONE(0, 0.0D, 0.0D, 0.0D),
        FIRE(1, 1.0D, 0.6D, 0.0D),
        ZOMBIE(2, 0.1D, 0.1D, 0.8D),
        ROAR(3, 0.8D, 0.3D, 0.8D),
        TORNADO(4, 1.0D, 0.1D, 0.1D),
        SKELETON(5, 0.5D, 0.5D, 0.5D),
        SACRIFICE(6, 0.1D, 0.1D, 0.1D);

        private final int id;
        private final double[] particleSpeed;

        SpellType(int idIn, double xParticleSpeed, double yParticleSpeed, double zParticleSpeed) {
            this.id = idIn;
            this.particleSpeed = new double[]{xParticleSpeed, yParticleSpeed, zParticleSpeed};
        }

        public static SpellType getFromId(int idIn) {
            for(SpellType SpellcastingCultistEntity$spelltype : values()) {
                if (idIn == SpellcastingCultistEntity$spelltype.id) {
                    return SpellcastingCultistEntity$spelltype;
                }
            }

            return NONE;
        }
    }

    public abstract class UseSpellGoal extends Goal {
        protected int spellWarmup;
        protected int spellCooldown;

        protected UseSpellGoal() {
        }

        public boolean canUse() {
            LivingEntity livingentity = SpellcastingCultistEntity.this.getTarget();
            if (livingentity != null && livingentity.isAlive()) {
                if (SpellcastingCultistEntity.this.isSpellcasting()) {
                    return false;
                } else {
                    return SpellcastingCultistEntity.this.tickCount >= this.spellCooldown;
                }
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = SpellcastingCultistEntity.this.getTarget();
            return livingentity != null && livingentity.isAlive() && this.spellWarmup > 0;
        }

        public void start() {
            this.spellWarmup = this.getCastWarmupTime();
            SpellcastingCultistEntity.this.spellTicks = this.getCastingTime();
            this.spellCooldown = SpellcastingCultistEntity.this.tickCount + this.getCastingInterval();
            SoundEvent soundevent = this.getSpellPrepareSound();
            if (soundevent != null) {
                SpellcastingCultistEntity.this.playSound(soundevent, 1.0F, 1.0F);
            }

            SpellcastingCultistEntity.this.setSpellType(this.getSpellType());
        }

        public void tick() {
            --this.spellWarmup;
            if (this.spellWarmup == 0) {
                this.castSpell();
                SpellcastingCultistEntity.this.setSpellType(SpellType.NONE);
                SpellcastingCultistEntity.this.playSound(SpellcastingCultistEntity.this.getCastingSoundEvent (), 1.0F, 1.0F);
            }

        }

        protected abstract void castSpell();

        protected int getCastWarmupTime() {
            return 20;
        }

        protected abstract int getCastingTime();

        protected abstract int getCastingInterval();

        @Nullable
        protected abstract SoundEvent getSpellPrepareSound();

        protected abstract SpellType getSpellType();
    }
}
