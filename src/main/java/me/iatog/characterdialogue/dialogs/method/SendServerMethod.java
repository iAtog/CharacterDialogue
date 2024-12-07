package me.iatog.characterdialogue.dialogs.method;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.dialogs.MethodConfiguration;
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
	public void execute(Player player, MethodConfiguration configuration, DialogSession session, SingleUseConsumer<CompletedType> completed) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		String server = configuration.getArgument();
		String channel = configuration.getString("channel", "BungeeCord");

		if(server.isEmpty()) {
			completed.accept(CompletedType.DESTROY);
			player.sendMessage(TextUtils.colorize("&cNo server found"));
			return;
		}

		out.writeUTF("Connect");
		out.writeUTF(server);
		
		player.sendPluginMessage(provider, channel, out.toByteArray());
		completed.accept(CompletedType.DESTROY);
	}
}
