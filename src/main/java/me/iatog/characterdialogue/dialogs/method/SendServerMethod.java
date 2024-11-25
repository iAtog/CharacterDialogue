package me.iatog.characterdialogue.dialogs.method;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.enums.CompletedType;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.util.SingleUseConsumer;
import me.iatog.characterdialogue.util.TextUtils;
import org.bukkit.entity.Player;

public class SendServerMethod extends DialogMethod<CharacterDialoguePlugin> {
	
	public SendServerMethod(CharacterDialoguePlugin main) {
		super("send_server", main);
	}

	@Override
	public void execute(Player player, String arg, DialogSession session, SingleUseConsumer<CompletedType> completed) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		String[] args = arg.split(",");
		
		if(args.length == 0) {
			completed.accept(CompletedType.DESTROY);
			player.sendMessage(TextUtils.colorize("No server found"));
			return;
		}
		
		String server = args[0];
		String channel = args.length > 1 ? args[1] : "BungeeCord";
		
		out.writeUTF("Connect");
		out.writeUTF(server);
		
		player.sendPluginMessage(provider, channel, out.toByteArray());
		completed.accept(CompletedType.DESTROY);
	}
}
