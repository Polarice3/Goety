package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.client.render.GhostFireTextures;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.neutral.AbstractWraithEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SEntityTeleportPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class GhostFireEntity extends GroundProjectileEntity {
    private static final DataParameter<Integer> DATA_TYPE_ID = EntityDataManager.defineId(GhostFireEntity.class, DataSerializers.INT);

    public GhostFireEntity(EntityType<? extends Entity> p_i50170_1_, World p_i50170_2_) {
        super(p_i50170_1_, p_i50170_2_);
        this.yRot = 0.0F;
        this.lifeTicks = 80;
    }

    public GhostFireEntity(World world, double pPosX, double pPosY, double pPosZ, LivingEntity owner) {
        this(ModEntityType.GHOST_FIRE.get(), world);
        this.setOwner(owner);
        this.setPos(pPosX, pPosY, pPosZ);
    }

    public GhostFireEntity(World world, BlockPos blockPos, LivingEntity owner) {
        this(ModEntityType.GHOST_FIRE.get(), world);
        this.setOwner(owner);
        this.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public ResourceLocation getResourceLocation() {
        return GhostFireTextures.TEXTURES.getOrDefault(this.getAnimation(), GhostFireTextures.TEXTURES.get(0));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TYPE_ID, 0);
    }

    public int getAnimation() {
        return this.entityData.get(DATA_TYPE_ID);
    }

    public void setAnimation(int pType) {
        this.entityData.set(DATA_TYPE_ID, pType);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setAnimation(pCompound.getInt("Animation"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Animation", this.getAnimation());
    }

    public float getBrightness() {
        return 1.0F;
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (this.sentTrapEvent) {
                if (this.animationTicks > 9){
                    --this.animationTicks;
                }
                if (this.getAnimation() < 44){
                    this.setAnimation(this.getAnimation() + 1);
                } else {
                    this.setAnimation(13);
                }
                --this.lifeTicks;
                if (this.lifeTicks <= 70) {
                    for(int i = 0; i < 3; ++i) {
                        this.level.addParticle(ParticleTypes.SMOKE, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                    }
                    if (this.level.random.nextInt(24) == 0) {
                        this.level.playLocalSound((double)this.blockPosition().getX() + 0.5D, (double)this.blockPosition().getY() + 0.5D, (double)this.blockPosition().getZ() + 0.5D, SoundEvents.FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + this.level.random.nextFloat(), this.level.random.nextFloat() * 0.7F + 0.3F, false);
                    }
                }
            }
        } else {
            if (!this.isNoGravity()) {
                this.moveDownToGround();
            }

            if (!this.sentTrapEvent) {
                this.level.broadcastEntityEvent(this, (byte)4);
                this.sentTrapEvent = true;
            }
            if (this.tickCount == 8) {
                if (!this.playSound) {
                    this.level.broadcastEntityEvent(this, (byte) 5);
                    this.playSound = true;
                }
            }

            if (this.tickCount >= 12){
                for(LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox())) {
                    if (this.getOwner() != null && this.getOwner() instanceof IMob){
                        if (!(livingentity instanceof IMob)){
                            this.dealDamageTo(livingentity);
                        }
                    } else {
                        this.dealDamageTo(livingentity);
                    }
                }
            }

            if (this.tickCount > 0) {
                if (--this.lifeTicks < 0) {
                    this.remove();
                }
            }

            if (this.getOwner() != null){
                if (this.getOwner() instanceof MobEntity){
                    if (this.getOwner().hurtTime > 0 && this.lifeTicks > 75 && !this.getOwner().isDeadOrDying()){
                        this.lifeTicks = 12;
                    }
                }
            }

            if (this.isInLava() || this.isInWaterOrBubble() || (MobUtil.isInRain(this) && this.level.random.nextFloat() < 0.2F)){
                this.level.broadcastEntityEvent(this, (byte)6);
                this.remove();
            }
        }

    }

    public void moveDownToGround() {
        RayTraceResult rayTrace = rayTrace(this);
        if (rayTrace.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult hitResult = (BlockRayTraceResult) rayTrace;
            if (hitResult.getDirection() == Direction.UP) {
                BlockState hitBlock = this.level.getBlockState(hitResult.getBlockPos());
                if (hitBlock.getBlock() instanceof SlabBlock && hitBlock.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.BOTTOM) {
                    this.setPos(getX(), hitResult.getBlockPos().getY() + 1.0625F - 0.5f, getZ());
                } else {
                    this.setPos(getX(), hitResult.getBlockPos().getY() + 1.0625F, getZ());
                }
                if (this.level instanceof ServerWorld) {
                    ((ServerWorld) this.level).getChunkSource().broadcastAndSend(this, new SEntityTeleportPacket(this));
                }
            }
        }
    }

    private RayTraceResult rayTrace(GhostFireEntity entity) {
        Vector3d startPos = new Vector3d(entity.getX(), entity.getY(), entity.getZ());
        Vector3d endPos = new Vector3d(entity.getX(), 0, entity.getZ());
        return entity.level.clip(new RayTraceContext(startPos, endPos, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
    }

    private void dealDamageTo(LivingEntity target) {
        LivingEntity livingentity = this.getOwner();
        int burning = 0;
        float damage = 4.0F;
        if (this.tickCount >= 14){
            damage = 2.0F;
        }
        if (target.isAlive() && !target.isInvulnerable() && target != livingentity) {
            if (livingentity == null) {
                target.hurt(DamageSource.IN_FIRE, damage);
            } else {
                if (target.isAlliedTo(livingentity)){
                    return;
                }
                if (livingentity.isAlliedTo(target)) {
                    return;
                }
                if (livingentity instanceof PlayerEntity){
                    PlayerEntity player = (PlayerEntity) livingentity;
                    if (WandUtil.enchantedFocus(player)) {
                        damage += WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                        burning += WandUtil.getLevels(ModEnchantments.BURNING.get(), player);
                    }
                } else {
                    if (livingentity.getAttribute(Attributes.ATTACK_DAMAGE) != null){
                        damage = (float) livingentity.getAttributeValue(Attributes.ATTACK_DAMAGE);
                        if (this.tickCount >= 14){
                            damage /= 2.0F;
                        }
                    }
                    if (livingentity instanceof AbstractWraithEntity){
                        AbstractWraithEntity wraith = (AbstractWraithEntity) livingentity;
                        burning = wraith.getBurningLevel();
                    }
                }
                if (target.hurt(ModDamageSource.magicFire(this, livingentity), damage)){
                    target.setSecondsOnFire(5 * burning);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte pId) {
        super.handleEntityEvent(pId);
        if (pId == 5) {
            if (!this.isSilent()) {
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), ModSounds.WRAITH_FIRE.get(), this.getSoundSource(), 1.0F, 1.0F, false);
            }
        }
        if (pId == 6){
            if (!this.isSilent()) {
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.FIRE_EXTINGUISH, this.getSoundSource(), 1.0F, 1.0F, false);
            }
        }

    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
