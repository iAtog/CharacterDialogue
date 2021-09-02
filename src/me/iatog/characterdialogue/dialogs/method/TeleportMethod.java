package me.iatog.characterdialogue.dialogs.method;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.DialogSession;

public class TeleportMethod extends DialogMethod {

	public TeleportMethod() {
		super("teleport");
	}

	@Override
	public void execute(Player player, String arg, DialogSession session) {
		// TELEPORT: world,x,y,z,yaw,pitch
		String[] part = arg.split(",");
		Location location = player.getLocation();
		
		World world = part[0].equals("current") ? player.getWorld() : Bukkit.getWorld(part[0]);
		double x = Double.valueOf(part[1]);
		double y = Double.valueOf(part[2]);
		double z = Double.valueOf(part[3]);
		float yaw = part[4].equals("current") ? location.getYaw() : Float.valueOf(part[4]);
		float pitch = part[5].equals("current") ? location.getPitch() : Float.valueOf(part[5]);
		
		Location toTeleport = new Location(world, x, y, z, yaw, pitch);
		player.teleport(toTeleport);
	}

}
