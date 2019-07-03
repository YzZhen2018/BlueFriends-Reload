package cn.bluesadi.bluefriends.util.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SerializedLocation {
    private String world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    SerializedLocation(){}

    SerializedLocation(Location loc){
        world = loc.getWorld().getName();
        x = loc.getX();
        y = loc.getY();
        z = loc.getZ();
        yaw = loc.getYaw();
        pitch = loc.getPitch();
    }

    Location buildLocation(){
        return new Location(Bukkit.getWorld(world),x,y,z,yaw,pitch);
    }
}
