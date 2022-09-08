package com.Polarice3.Goety.data;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.IFinishedBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModBlockStateProvider implements IDataProvider {

    public ModBlockStateProvider() {
    }

    public void run(DirectoryCache pCache) {
        Map<Block, IFinishedBlockState> map = Maps.newHashMap();
        Consumer<IFinishedBlockState> consumer = (p_240085_1_) -> {
            Block block = p_240085_1_.getBlock();
            IFinishedBlockState ifinishedblockstate = map.put(block, p_240085_1_);
            if (ifinishedblockstate != null) {
                throw new IllegalStateException("Duplicate blockstate definition for " + block);
            }
        };
        Map<ResourceLocation, Supplier<JsonElement>> map1 = Maps.newHashMap();
        Set<Item> set = Sets.newHashSet();
        BiConsumer<ResourceLocation, Supplier<JsonElement>> biconsumer = (p_240086_1_, p_240086_2_) -> {
            Supplier<JsonElement> supplier = map1.put(p_240086_1_, p_240086_2_);
            if (supplier != null) {
                throw new IllegalStateException("Duplicate model definition for " + p_240086_1_);
            }
        };
        Consumer<Item> consumer1 = set::add;
        (new ModBlockModelProvider(consumer, biconsumer, consumer1)).run();
    }

    /**
     * Gets a name for this provider, to use in logging.
     */
    public String getName() {
        return "Block State Definitions";
    }
}
