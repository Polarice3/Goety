package com.Polarice3.Goety.client.render.tileentities;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.blocks.ModChestBlock;
import com.Polarice3.Goety.common.blocks.ModTrappedChestBlock;
import com.Polarice3.Goety.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Chest Rendering based of codes from @MehVahdJukaar
 */
public class ModChestTileEntityRenderer<T extends TileEntity & IChestLid> extends ChestTileEntityRenderer<T> {

    private static final String path = "entity/chest/";
    private static Map<Block, ChestResources> RESOURCEMAP = new HashMap<>();

    public ModChestTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    public static void stitchChests(TextureStitchEvent.Pre event, Block chest) {
        ResourceLocation atlas = event.getMap().location();
        if (chest instanceof ModChestBlock) {
            if (chest instanceof ModTrappedChestBlock) {
                resourcePacker(event, atlas, chest, "trapped", "trapped_left", "trapped_right");
            } else {
                resourcePacker(event, atlas, chest, "normal", "normal_left", "normal_right");
            }
        }
    }

    private static void resourcePacker(TextureStitchEvent.Pre event, ResourceLocation atlas, Block chest, String middleLocation, String leftLocation, String rightLocation) {
        String deepPath;
        try {
            deepPath = path + ChestToString.getEnumFromChest(chest).getString();
        } catch (Exception e) {
            Goety.LOGGER.debug("Chest without EnumValue: " + chest);
            return;
        }
        ResourceLocation normal = new ResourceLocation(Goety.MOD_ID, deepPath + "/" + middleLocation);
        ResourceLocation left = new ResourceLocation(Goety.MOD_ID, deepPath + "/" + leftLocation);
        ResourceLocation right = new ResourceLocation(Goety.MOD_ID, deepPath + "/" + rightLocation);
        RESOURCEMAP.put(chest, new ChestResources(new RenderMaterial(atlas, normal), new RenderMaterial(atlas, left), new RenderMaterial(atlas, right)));
        addSprites(event, normal, left, right);
    }

    private static void addSprites(TextureStitchEvent.Pre event, ResourceLocation normal, ResourceLocation left, ResourceLocation right) {
        event.addSprite(normal);
        event.addSprite(left);
        event.addSprite(right);
    }

    @Override
    protected RenderMaterial getMaterial(T tileEntity, ChestType chestType) {
        ChestResources resources = RESOURCEMAP.get(tileEntity.getBlockState().getBlock());
        if (resources == null) {
            return null;
        }
        switch (chestType) {
            case SINGLE:
            default:
                return resources.getMain();
            case LEFT:
                return resources.getLeft();
            case RIGHT:
                return resources.getRight();
        }
    }

    private static class ChestResources {
        private final RenderMaterial main;
        private final RenderMaterial left;
        private final RenderMaterial right;

        public ChestResources(RenderMaterial main, RenderMaterial left, RenderMaterial right) {
            this.main = main;
            this.left = left;
            this.right = right;
        }

        public RenderMaterial getLeft() {
            return left;
        }

        public RenderMaterial getMain() {
            return main;
        }

        public RenderMaterial getRight() {
            return right;
        }
    }

    public enum ChestToString {

        HAUNTED(ModBlocks.HAUNTED_CHEST.get(), ModBlocks.TRAPPED_HAUNTED_CHEST.get(), "haunted"),
        GLOOM(ModBlocks.GLOOM_CHEST.get(), ModBlocks.TRAPPED_GLOOM_CHEST.get(), "gloom"),
        MURK(ModBlocks.MURK_CHEST.get(), ModBlocks.TRAPPED_MURK_CHEST.get(), "murk");

        private final Block chest;
        private final Block trappedChest;
        private final String name;

        ChestToString(Block chest, Block trappedChest, String name) {
            this.chest = chest;
            this.trappedChest = trappedChest;
            this.name = name;
        }

        public static ChestToString getEnumFromChest(Block chest) {
            for (ChestToString index : ChestToString.values()) {
                if (index.getChest() == chest || index.getTrappedChest() == chest) {
                    return index;
                }
            }
            return null;
        }

        public Block getChest() {
            return chest;
        }

        public Block getTrappedChest() {
            return trappedChest;
        }

        public String getString() {
            return name;
        }

    }
}
