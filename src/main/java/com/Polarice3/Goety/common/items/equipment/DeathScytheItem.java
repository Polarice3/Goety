package com.Polarice3.Goety.common.items.equipment;

import com.Polarice3.Goety.common.entities.projectiles.ScytheProjectileEntity;
import com.Polarice3.Goety.common.items.ModItemTiers;
import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.network.packets.client.CScytheStrikePacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class DeathScytheItem extends DarkScytheItem{
    public DeathScytheItem() {
        super(ModItemTiers.DEATH);
        MinecraftForge.EVENT_BUS.addListener(this::EmptyStrike);
        MinecraftForge.EVENT_BUS.addListener(this::AttackStrike);
    }

    private void EmptyStrike(PlayerInteractEvent.LeftClickEmpty event){
        if (event.getItemStack().getItem() == this){
            ModNetwork.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CScytheStrikePacket());
        }
    }

    private void AttackStrike(AttackEntityEvent event){
        if (event.getPlayer().getMainHandItem().getItem() == this){
            strike(event.getPlayer().level, event.getPlayer());
        }
    }

    public void strike(World pLevel, PlayerEntity pPlayer){
        if (pPlayer.getAttackStrengthScale(0.5F) > 0.9F) {
            pLevel.playSound((PlayerEntity)null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundCategory.NEUTRAL, 2.0F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            if (!pLevel.isClientSide) {
                Vector3d vector3d = pPlayer.getViewVector(1.0F);
                ScytheProjectileEntity scytheProjectile = new ScytheProjectileEntity(pPlayer.getMainHandItem(),
                        pLevel,
                        pPlayer.getX() + vector3d.x / 2,
                        pPlayer.getEyeY() - 0.2,
                        pPlayer.getZ() + vector3d.z / 2,
                        vector3d.x,
                        vector3d.y,
                        vector3d.z);
                scytheProjectile.setOwnerId(pPlayer.getUUID());
                scytheProjectile.setDamage(this.getInitialDamage());
                scytheProjectile.setTotallife(300);
                pLevel.addFreshEntity(scytheProjectile);
            }
        }
    }
}
