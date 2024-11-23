package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.placeholders.Placeholders;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.util.TextUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class TalkMethod extends DialogMethod<CharacterDialoguePlugin> implements Listener {

    private static final String empty = TextUtils.colorize("&7");
    private static final String[] emptyMessages = TalkType.getEmptyList();

    // List of players to wait sneak
    private final List<UUID> players;
    /**
     * DESIGN:
     * talk: <type>|<message>
     * talk: message|Lorem ipsum
     * talk: full_chat|Lorem ipsum
     * simple
     */
    public TalkMethod(CharacterDialoguePlugin main) {
        super("talk", main);
        this.players = new ArrayList<>();
    }

    @Override
    public void execute(Player player, String arg, DialogSession session) {
        String[] args = arg.split("\\|", 2);

        TalkType type;
        String message = "";
        Sound sound = Sound.BLOCK_STONE_BUTTON_CLICK_OFF;
        long tickSpeed = 2L;

        float yaw = 0.5f;
        float pitch = 0.5f;
        boolean skippeable = true;

        try {
            String[] typeArgs = args[0].split("[(),]");

            type = TalkType.valueOf(typeArgs[0].toUpperCase());

            if (typeArgs.length > 1) {
                String[] soundSplit = typeArgs[1].split("/");
                if(soundSplit.length > 1) {
                    sound = Sound.valueOf(soundSplit[0]);
                    yaw = Float.parseFloat(soundSplit[1]);
                    pitch = Float.parseFloat(soundSplit[2]);
                } else {
                    sound = Sound.valueOf(typeArgs[1]);
                }
            }
            if (typeArgs.length > 2) {
                tickSpeed = Long.parseLong(typeArgs[2]);
            }
            if (typeArgs.length > 3) {
                skippeable = Boolean.parseBoolean(typeArgs[3]);
            }
            /*type = TalkType.valueOf(args[0].toUpperCase());*/
            message = args[1];
        } catch(IndexOutOfBoundsException|NullPointerException ex) {
            getProvider().getLogger().severe("The line L" + session.getCurrentIndex() + " in " + session.getDialogue().getName() + " is not valid. (parse error)");
            session.destroy();
            return;
        }

        if(message.isEmpty()) {
            getProvider().getLogger().severe("The line L" + session.getCurrentIndex() + " in " + session.getDialogue().getName() + " is not valid. (empty message)");
            session.destroy();
            return;
        }

        session.pause();
        this.players.add(player.getUniqueId());

        animateMessage(player, message, type, session, tickSpeed, sound, yaw, pitch, skippeable);
    }

    public void animateMessage(Player player, String message, TalkType type, DialogSession session, long tickSpeed,
                               Sound sound,  float yaw, float pitch, boolean skippable) {
        UUID uuid = player.getUniqueId();
        String npcName = session.getDialogue().getDisplayName();
        String translatedMessage = Placeholders.translate(player, message);

        new BukkitRunnable() {
            int index = 0;

            @Override
            public void run() {
                try {
                    if(!players.contains(uuid) && skippable) {
                        type.execute(player, translatedMessage, npcName);
                        player.playSound(player.getLocation(), sound, 1f, 2f);
                        this.cancel();
                        players.remove(uuid);
                        Bukkit.getScheduler().runTaskLater(getProvider(), session::startNext, 10);
                        return;
                    }

                    if (index < message.length()) {
                        String writingMessage = translatedMessage.substring(0, index + 1);
                        index++;

                        player.playSound(player.getLocation(), sound, 0.5f, 0.5f);
                        type.execute(player, writingMessage, npcName);
                    } else {
                        players.remove(uuid);
                        this.cancel();
                        Bukkit.getScheduler().runTaskLater(getProvider(), session::startNext, 10);
                    }
                } catch(NullPointerException ex) {
                    getProvider().getLogger().severe("Null exception in talkMethod [" + message + "]");
                    ex.printStackTrace();
                    session.destroy();
                    this.cancel();
                }
            }
        }.runTaskTimer(getProvider(), 1L, tickSpeed);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        players.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if(!players.contains(player.getUniqueId())) {
            return;
        }

        players.remove(player.getUniqueId());
    }

    public enum TalkType {
        ACTION_BAR((player, text, npcName) -> {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("&7[&c" + npcName + "&7] &f" + text));
        }),
        MESSAGE((player, text, npcName) -> {
            String npc = TextUtils.colorize("&8[&b" + npcName + "&8] &7");

            player.sendMessage(emptyMessages);
            player.sendMessage(npc + text);
        }),
        FULL_CHAT((player, text, npcName) -> {
            String line = "&7&m                                                                                ";
            String colorizedText = TextUtils.colorize("&8[&b" + npcName + "&8] &7" + text);
            List<String> wrapped = TextUtils.wrapText(colorizedText, 55);

            player.sendMessage(emptyMessages);
            player.sendMessage(TextUtils.colorize(line));
            player.sendMessage(empty);

            for(String wrap : wrapped) {
                TextUtils.sendCenteredMessage(player, "&7" + wrap);
            }

            player.sendMessage(empty);
            player.sendMessage(TextUtils.colorize(line));
        });

        private final TriConsumer<Player, String, String> consumer;

        TalkType(TriConsumer<Player, String, String> consumer) {
            this.consumer = consumer;

        }

        public void execute(Player target, String message, String npcName) {
            consumer.accept(target, message, npcName);
        }

        public static String[] getEmptyList() {
            List<String> list = new ArrayList<>();

            for(int i = 0; i < 40;i++) {
                list.add(ChatColor.translateAlternateColorCodes('&', "&7"));
            }

            return list.stream().toArray(String[]::new);
        }
    }


}
