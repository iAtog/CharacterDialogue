package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.enums.CompletedType;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.util.SingleUseConsumer;
import me.iatog.characterdialogue.util.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TeleportMethod extends DialogMethod<CharacterDialoguePlugin> {

	public TeleportMethod() {
		super("teleport");
	}

	@Override
	public void execute(Player player, String arg, DialogSession session, SingleUseConsumer<CompletedType> completed) {
		// TELEPORT: world,x,y,z,yaw,pitch
		String[] part = arg.split(",");
		Location location = player.getLocation();
		String currentText = "current";

		try {
			World world = part[0].equals(currentText) ? player.getWorld() : Bukkit.getWorld(part[0]);
			double x = part[1].equals(currentText) ? location.getX() : Double.parseDouble(part[1]);
			double y = part[2].equals(currentText) ? location.getY() : Double.parseDouble(part[2]);
			double z = part[3].equals(currentText) ? location.getZ() : Double.parseDouble(part[3]);
			float yaw = location.getYaw();
			float pitch = location.getPitch();

			if (part.length > 4) {
				yaw = Float.parseFloat(part[4]);
				pitch = Float.parseFloat(part[5]);
			}

			Location toTeleport = new Location(world, x, y, z, yaw, pitch);
			player.teleport(toTeleport);
			completed.accept(CompletedType.CONTINUE);
		} catch(Exception ex) {
			player.sendMessage(TextUtils.colorize("&cInvalid coordinates provided."));
			completed.accept(CompletedType.DESTROY);
		}
	}

}
