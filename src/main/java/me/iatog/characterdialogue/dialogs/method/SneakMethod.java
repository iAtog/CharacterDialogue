package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.events.DialogueFinishEvent;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.dialogs.MethodConfiguration;
import me.iatog.characterdialogue.enums.CompletedType;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.util.SingleUseConsumer;
import me.iatog.characterdialogue.util.TextUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SneakMethod extends DialogMethod<CharacterDialoguePlugin> implements Listener {

    private final Map<UUID, SingleUseConsumer<CompletedType>> waitingPlayers;

    public SneakMethod(CharacterDialoguePlugin main) {
        super("waitsneak", main);
        this.waitingPlayers = new HashMap<>();

        setupRunnable();
    }

    @Override
    public void execute(Player player, MethodConfiguration configuration, DialogSession session, SingleUseConsumer<CompletedType> completed) {
        boolean actionBar = getProvider().getFileFactory().getConfig().getBoolean("use-actionbar", true);
        waitingPlayers.put(player.getUniqueId(), completed);
        //completed.accept(CompletedType.PAUSE);

        if (!actionBar) {
            player.sendMessage(TextUtils.colorize("&7"));
            player.sendMessage(TextUtils.colorize("&c[ Sneak to continue ]"));
            player.sendMessage(TextUtils.colorize("&7"));
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Map<UUID, DialogSession> sessions = provider.getCache().getDialogSessions();
        UUID uid = event.getPlayer().getUniqueId();

        if (sessions.containsKey(uid) && waitingPlayers.containsKey(uid)) {
            //DialogSession session = sessions.get(uid);
            waitingPlayers.get(uid).accept(CompletedType.CONTINUE);

            //session.start(current + 1);
            waitingPlayers.remove(uid);
        }
    }

    @EventHandler
    public void onFinish(DialogueFinishEvent event) {
        if (event.getPlayer() == null) return;

        waitingPlayers.remove(event.getPlayer().getUniqueId());
    }

    private void setupRunnable() {
        Bukkit.getScheduler().runTaskTimer(getProvider(), new Runnable() {
            private String colorCode = "c";

            @Override
            public void run() {
                for (UUID uuid : waitingPlayers.keySet()) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null || !player.isOnline()) {
                        waitingPlayers.remove(uuid);
                        return;
                    }

                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(TextUtils.colorize("&" + colorCode + "[ Sneak to continue ]")));

                    if (colorCode.equals("f")) {
                        colorCode = "c";
                    } else {
                        colorCode = "f";
                    }
                }
            }
        }, 20, 20);
    }
}
