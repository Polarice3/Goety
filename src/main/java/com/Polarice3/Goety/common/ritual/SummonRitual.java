package com.Polarice3.Goety.common.ritual;

import com.Polarice3.Goety.client.inventory.crafting.RitualRecipe;
import com.Polarice3.Goety.common.entities.hostile.ShadeEntity;
import com.Polarice3.Goety.common.tileentities.DarkAltarTileEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SummonRitual extends Ritual {
    private final boolean tame;

    public SummonRitual(RitualRecipe recipe, boolean tame) {
        super(recipe);
        this.tame = tame;
    }

    public void spawnEntity(Entity entity, World world) {
        for (ServerPlayerEntity player : world.getEntitiesOfClass(ServerPlayerEntity.class,
                entity.getBoundingBox().inflate(50))) {
            CriteriaTriggers.SUMMONED_ENTITY.trigger(player, entity);
        }
        world.addFreshEntity(entity);
    }

    @Override
    public void finish(World world, BlockPos blockPos, DarkAltarTileEntity tileEntity,
                       PlayerEntity castingPlayer, ItemStack activationItem) {
        super.finish(world, blockPos, tileEntity, castingPlayer, activationItem);

        activationItem.shrink(1);

        ((ServerWorld) world).sendParticles(ParticleTypes.LARGE_SMOKE, blockPos.getX() + 0.5,
                blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 1, 0, 0, 0, 0);

        EntityType<?> entityType = this.recipe.getEntityToSummon();
        if (entityType != null) {
            Entity entity = this.createSummonedEntity(entityType, world, blockPos, tileEntity, castingPlayer);
            if (entity instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) entity;
                this.prepareLivingEntityForSpawn(living, world, blockPos, tileEntity, castingPlayer, this.tame);

                this.initSummoned(living, world, blockPos, tileEntity, castingPlayer);

                this.spawnEntity(living, world);

            } else {
                entity.absMoveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                        world.random.nextInt(360), 0);
                this.initSummoned(entity, world, blockPos, tileEntity, castingPlayer);
                this.spawnEntity(entity, world);
            }
        }
    }

    public Entity createSummonedEntity(EntityType<?> entityType, World world, BlockPos goldenBowlPosition, DarkAltarTileEntity tileEntity,
                                       PlayerEntity castingPlayer) {
        return entityType.create(world);
    }

    public void initSummoned(LivingEntity living, World world, BlockPos goldenBowlPosition, DarkAltarTileEntity tileEntity,
                             PlayerEntity castingPlayer) {
        if (living instanceof ShadeEntity){
            ShadeEntity shadeEntity = (ShadeEntity) living;
            shadeEntity.setLimitedLife(12000);
        }
    }

    public void initSummoned(Entity entity, World world, BlockPos goldenBowlPosition, DarkAltarTileEntity tileEntity,
                             PlayerEntity castingPlayer) {
    }
}
/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */