package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.events.DialogueFinishEvent;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.DialogSession;
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

    private final Map<UUID, Integer> waitingPlayers;

    public SneakMethod(CharacterDialoguePlugin main) {
        super("waitsneak");
        this.provider = main;
        this.waitingPlayers = new HashMap<>();

        setupRunnable();
    }

    @Override
    public void execute(Player player, String arg, DialogSession session) {
        waitingPlayers.put(player.getUniqueId(), session.getCurrentIndex());
        session.cancel();
        //player.sendMessage("§c[ Sneak to continue ]");
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Map<UUID, DialogSession> sessions = provider.getCache().getDialogSessions();
        UUID uid = event.getPlayer().getUniqueId();

        if(sessions.containsKey(uid) && waitingPlayers.containsKey(uid)) {
            DialogSession session = sessions.get(uid);
            int current = waitingPlayers.get(uid);

            session.start(current + 1);
            waitingPlayers.remove(uid);
        }
    }

    @EventHandler
    public void onFinish(DialogueFinishEvent event) {
        waitingPlayers.remove(event.getPlayer().getUniqueId());
    }

    private void setupRunnable() {
        Bukkit.getScheduler().runTaskTimer(getProvider(), () -> {
            for(UUID uuid : waitingPlayers.keySet()) {
                Player player = Bukkit.getPlayer(uuid);
                assert player != null;
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§c[ Sneak to continue ]"));
            }
        }, 40, 40);
    }
}
