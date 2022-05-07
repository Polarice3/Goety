package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

public class NetherBookRawItem extends Item {

    public NetherBookRawItem(){
        super(new Properties()
                .tab(Goety.TAB)
                .rarity(Rarity.UNCOMMON)
                .setNoRepair()
                .stacksTo(1)
                .fireResistant()
        );
    }

    @Nonnull
    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand){
        World world = player.getEntity().level;
        if (target instanceof VillagerEntity){
            VillagerEntity villager = (VillagerEntity) target;
            if (villager.getVillagerData().getProfession() == VillagerProfession.LIBRARIAN){
                ItemEntity itemEntity = new ItemEntity(EntityType.ITEM, world);
                itemEntity.setItem(new ItemStack(ModItems.NETHER_BOOK_TRANSLATED.get()));
                itemEntity.setPos(villager.getX(), villager.getY(), villager.getZ());
                world.addFreshEntity(itemEntity);
                if (!world.isClientSide){
                    ServerWorld serverWorld = (ServerWorld) world;
                    WitchEntity witchentity = EntityType.WITCH.create(serverWorld);
                    if (witchentity != null) {
                        witchentity.moveTo(villager.getX(), villager.getY(), villager.getZ(), villager.yRot, villager.xRot);
                        witchentity.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(witchentity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData) null, (CompoundNBT) null);
                        witchentity.setNoAi(villager.isNoAi());
                        witchentity.level.playSound(null, witchentity.blockPosition(), SoundEvents.WITCH_CELEBRATE, SoundCategory.HOSTILE, 1.5F, 2.0F);
                        if (villager.hasCustomName()) {
                            witchentity.setCustomName(villager.getCustomName());
                            witchentity.setCustomNameVisible(villager.isCustomNameVisible());
                        }
                        for(int i = 0; i < serverWorld.random.nextInt(35) + 10; ++i) {
                            double d = serverWorld.random.nextGaussian() * 0.2D;
                            new ParticleUtil(ParticleTypes.SMOKE, witchentity.getX(), witchentity.getEyeY(), witchentity.getZ(), d, d, d);
                        }

                        witchentity.setPersistenceRequired();
                        net.minecraftforge.event.ForgeEventFactory.onLivingConvert(villager, witchentity);
                        serverWorld.addFreshEntityWithPassengers(witchentity);
                        villager.remove();
                    }
                }
                stack.shrink(1);
                return ActionResultType.SUCCESS;
            }
        }
        return super.interactLivingEntity(stack, player, target, hand);

    }
}
