package com.Polarice3.Goety.common.entities.hostile.cultists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class JuggerEntity extends AbstractCultistEntity{

    protected JuggerEntity(EntityType<? extends AbstractCultistEntity> type, World worldIn) {
        super(type, worldIn);
        this.maxUpStep = 1.0F;
        this.xpReward = 15;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0F, false));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.RAVAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.RAVAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.RAVAGER_DEATH;
    }

    public boolean canBeLeashed(PlayerEntity player) {
        return false;
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.RAVAGER_CELEBRATE;
    }

    public void aiStep() {
        super.aiStep();
        if (this.isAlive()) {

            if (this.horizontalCollision && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                boolean flag = false;
                AxisAlignedBB axisalignedbb = this.getBoundingBox().inflate(0.2D);

                for(BlockPos blockpos : BlockPos.betweenClosed(MathHelper.floor(axisalignedbb.minX), MathHelper.floor(axisalignedbb.minY), MathHelper.floor(axisalignedbb.minZ), MathHelper.floor(axisalignedbb.maxX), MathHelper.floor(axisalignedbb.maxY), MathHelper.floor(axisalignedbb.maxZ))) {
                    BlockState blockstate = this.level.getBlockState(blockpos);
                    Block block = blockstate.getBlock();
                    if (block instanceof LeavesBlock || blockstate.getMaterial() == Material.WOOD) {
                        flag = this.level.destroyBlock(blockpos, true, this) || flag;
                    }
                }

                if (!flag && this.onGround) {
                    this.jumpFromGround();
                }
            }

        }
    }

    protected PathNavigator createNavigation(World pLevel) {
        return new JuggerEntity.Navigator(this, pLevel);
    }

    static class Navigator extends GroundPathNavigator {
        public Navigator(MobEntity p_i50754_1_, World p_i50754_2_) {
            super(p_i50754_1_, p_i50754_2_);
        }

        protected PathFinder createPathFinder(int p_179679_1_) {
            this.nodeEvaluator = new JuggerEntity.Processor();
            return new PathFinder(this.nodeEvaluator, p_179679_1_);
        }
    }

    static class Processor extends WalkNodeProcessor {
        private Processor() {
        }

        /**
         * Returns the exact path node type according to abilities and settings of the entity
         */
        protected PathNodeType evaluateBlockPathType(IBlockReader pLevel, boolean pCanOpenDoors, boolean pCanEnterDoors, BlockPos pPos, PathNodeType pNodeType) {
            return pNodeType == PathNodeType.LEAVES ? PathNodeType.OPEN : super.evaluateBlockPathType(pLevel, pCanOpenDoors, pCanEnterDoors, pPos, pNodeType);
        }
    }

}
