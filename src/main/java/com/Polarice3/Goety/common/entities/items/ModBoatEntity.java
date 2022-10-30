package com.Polarice3.Goety.common.entities.items;

import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ModBoatEntity extends BoatEntity {
    private static final DataParameter<Integer> DATA_ID_TYPE = EntityDataManager.defineId(ModBoatEntity.class, DataSerializers.INT);

    public ModBoatEntity(EntityType<? extends BoatEntity> p_i50129_1_, World p_i50129_2_) {
        super(p_i50129_1_, p_i50129_2_);
        this.blocksBuilding = true;
    }

    public ModBoatEntity(World p_i1705_1_, double p_i1705_2_, double p_i1705_4_, double p_i1705_6_) {
        this(ModEntityType.MOD_BOAT.get(), p_i1705_1_);
        this.setPos(p_i1705_2_, p_i1705_4_, p_i1705_6_);
        this.setDeltaMovement(Vector3d.ZERO);
        this.xo = p_i1705_2_;
        this.yo = p_i1705_4_;
        this.zo = p_i1705_6_;
    }

    public Item getDropItem() {
        return ModItems.HAUNTED_BOAT.get();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE, ModBoatEntity.Type.HAUNTED.ordinal());
    }

    public void setType(ModBoatEntity.Type pBoatType) {
        this.entityData.set(DATA_ID_TYPE, pBoatType.ordinal());
    }

    public ModBoatEntity.Type getModBoatType() {
        return ModBoatEntity.Type.byId(this.entityData.get(DATA_ID_TYPE));
    }

    public static enum Type {
        HAUNTED(ModBlocks.HAUNTED_PLANKS.get(), "haunted"),
        GLOOM(ModBlocks.GLOOM_PLANKS.get(), "gloom"),
        MURK(ModBlocks.MURK_PLANKS.get(), "murk");

        private final String name;
        private final Block planks;

        private Type(Block p_i48146_3_, String p_i48146_4_) {
            this.name = p_i48146_4_;
            this.planks = p_i48146_3_;
        }

        public String getName() {
            return this.name;
        }

        public Block getPlanks() {
            return this.planks;
        }

        public String toString() {
            return this.name;
        }

        /**
         * Get a boat type by it's enum ordinal
         */
        public static ModBoatEntity.Type byId(int pId) {
            ModBoatEntity.Type[] aboatentity$type = values();
            if (pId < 0 || pId >= aboatentity$type.length) {
                pId = 0;
            }

            return aboatentity$type[pId];
        }

        public static ModBoatEntity.Type byName(String pName) {
            ModBoatEntity.Type[] aboatentity$type = values();

            for(int i = 0; i < aboatentity$type.length; ++i) {
                if (aboatentity$type[i].getName().equals(pName)) {
                    return aboatentity$type[i];
                }
            }

            return aboatentity$type[0];
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
