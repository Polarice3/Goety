package com.Polarice3.Goety.utils;

import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;

public class Vector3dUtil {
    public static Vector3d north(Vector3d vector3d){
        return relative(vector3d, Direction.NORTH);
    }

    public static Vector3d north(Vector3d vector3d, int distance){
        return relative(vector3d, Direction.NORTH, distance);
    }

    public static Vector3d south(Vector3d vector3d){
        return relative(vector3d, Direction.SOUTH);
    }

    public static Vector3d south(Vector3d vector3d, int distance){
        return relative(vector3d, Direction.SOUTH, distance);
    }

    public static Vector3d west(Vector3d vector3d){
        return relative(vector3d, Direction.WEST);
    }

    public static Vector3d west(Vector3d vector3d, int distance){
        return relative(vector3d, Direction.WEST, distance);
    }

    public static Vector3d east(Vector3d vector3d){
        return relative(vector3d, Direction.EAST);
    }

    public static Vector3d east(Vector3d vector3d, int distance){
        return relative(vector3d, Direction.EAST, distance);
    }

    public static Vector3d relative(Vector3d vector3d, Direction p_177972_1_) {
        return new Vector3d(vector3d.x() + p_177972_1_.getStepX(), vector3d.y() + p_177972_1_.getStepY(), vector3d.z() + p_177972_1_.getStepZ());
    }

    public static Vector3d relative(Vector3d vector3d, Direction pDirection, int pDistance) {
        return pDistance == 0 ? vector3d : new Vector3d(vector3d.x() + pDirection.getStepX() * pDistance, vector3d.y() + pDirection.getStepY() * pDistance, vector3d.z() + pDirection.getStepZ() * pDistance);
    }
}
