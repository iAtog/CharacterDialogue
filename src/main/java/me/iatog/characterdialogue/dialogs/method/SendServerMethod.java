package me.iatog.characterdialogue.dialogs.method;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.dialogs.MethodConfiguration;
import me.iatog.characterdialogue.dialogs.MethodContext;
import me.iatog.characterdialogue.util.TextUtils;

public class SendServerMethod extends DialogMethod<CharacterDialoguePlugin> {
	// send_server{channel="BungeeCord"}: lobby2
	public SendServerMethod(CharacterDialoguePlugin main) {
		super("send_server", main);
	}

	@Override
	public void execute(MethodContext context) {
		MethodConfiguration configuration = context.getConfiguration();
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		String server = configuration.getArgument();
		String channel = configuration.getString("channel", "BungeeCord");

		if(server.isEmpty()) {
			this.destroy(context);
			context.getPlayer().sendMessage(TextUtils.colorize("&cNo server found"));
			return;
		}

		out.writeUTF("Connect");
		out.writeUTF(server);
		this.destroy(context);
		context.getPlayer().sendPluginMessage(getProvider(), channel, out.toByteArray());
	}
}
