package com.Polarice3.Goety.client.inventory.container;

import com.Polarice3.Goety.Goety;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainerType {
    public static DeferredRegister<ContainerType<?>> CONTAINER_TYPE = DeferredRegister.create(ForgeRegistries.CONTAINERS, Goety.MOD_ID);

    public static final RegistryObject<ContainerType<SoulItemContainer>> WAND = CONTAINER_TYPE.register("wand",
        () -> IForgeContainerType.create(SoulItemContainer::createContainerClientSide));

    public static final RegistryObject<ContainerType<FocusBagContainer>> FOCUSBAG = CONTAINER_TYPE.register("focusbag",
            () -> IForgeContainerType.create(FocusBagContainer::createContainerClientSide));
}
