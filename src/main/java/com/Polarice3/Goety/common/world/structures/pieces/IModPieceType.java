package com.Polarice3.Goety.common.world.structures.pieces;

import com.Polarice3.Goety.utils.ConstantPaths;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

public class IModPieceType {
    public static IStructurePieceType SALVAGED_FORT_PIECE = registerStructurePiece(ConstantPaths.getSalvagedFort(), SalvagedFortPiece::new);
    public static IStructurePieceType DECREPIT_FORT_PIECE = registerStructurePiece(ConstantPaths.getDecrepitFort(), DecrepitFortPiece::new);
    public static IStructurePieceType DARK_MANOR_PIECE = registerStructurePiece(ConstantPaths.getDarkManor(), DarkManorPiece::new);
    public static IStructurePieceType PORTAL_OUTPOST_PIECE = registerStructurePiece(ConstantPaths.getPortalOutpost(), PortalOutpostPiece::new);
    public static IStructurePieceType RUINED_RITUAL_PIECE = registerStructurePiece(ConstantPaths.getRuinedRitual(), RuinedRitualPiece::new);

    private static <T extends IStructurePieceType> T registerStructurePiece(ResourceLocation key, T type) {
        return Registry.register(Registry.STRUCTURE_PIECE, key, type);
    }
}
