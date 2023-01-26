package com.Polarice3.Goety.common.spells.cantrips;

import com.Polarice3.Goety.utils.ServerParticleUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.vector.Vector3d;

public class MagnetCantrip {

    public void callItems(PlayerEntity pPlayer) {
        if (pPlayer != null && !pPlayer.isSpectator()) {
            for (Entity entity : pPlayer.level.getEntitiesOfClass(Entity.class, pPlayer.getBoundingBox().inflate(16.0D))){
                if (entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity) {
                    Vector3d vector3d = new Vector3d(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
                    Vector3d vector3d1 = new Vector3d(entity.getX(), entity.getY(), entity.getZ());
                    Vector3d vector3d2 = vector3d.subtract(vector3d1);
                    if (vector3d2.lengthSqr() > 1) {
                        vector3d2.normalize();
                    }
                    double speed = 0.2D;
                    Vector3d motion = new Vector3d(vector3d2.x * speed, vector3d2.y * speed, vector3d2.z * speed);
                    entity.setDeltaMovement(motion);
                    if (!pPlayer.level.isClientSide){
                        ServerParticleUtil.smokeParticles(ParticleTypes.WITCH, entity.getX(), entity.getY(), entity.getZ(), pPlayer.level);
                    }
                }
            }
        }
    }
}
