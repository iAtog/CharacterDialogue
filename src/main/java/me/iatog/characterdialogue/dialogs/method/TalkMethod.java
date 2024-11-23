package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.placeholders.Placeholders;
import me.iatog.characterdialogue.session.DialogSession;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TalkMethod extends DialogMethod<CharacterDialoguePlugin> implements Listener {

    /**
     * DESIGN:
     * talk: <type>|<message>
     * talk: message|Lorem ipsum
     * talk: full_chat|Lorem ipsum
     * simple
     * @param main e
     */

    public TalkMethod(CharacterDialoguePlugin main) {
        super("talk", main);
    }

    @Override
    public void execute(Player player, String arg, DialogSession session) {
        String[] args = arg.split("\\|", 2);

        TalkType type;
        String message;

        try {
            type = TalkType.valueOf(args[0].toUpperCase());
            message = args[1];
        } catch(IndexOutOfBoundsException|NullPointerException ex) {
            getProvider().getLogger().severe("The line L" + session.getCurrentIndex() + " in " + session.getDialogue().getName() + " is not valid.");
            session.destroy();
            return;
        }

        session.pause();

        animateMessage(player, message, type, session);
    }

    public void animateMessage(Player player, String message, TalkType type, DialogSession session) {
        new BukkitRunnable() {
            int index = 0;

            @Override
            public void run() {
                if (index < message.length()) {
                    String writingMessage = message.substring(0, index + 1);
                    String npcName = session.getDialogue().getDisplayName();

                    index++;

                    String text = Placeholders.translate(player, "&7[&c" + npcName + "&7] &f" + writingMessage);
                    player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 0.5f, 0.5f);
                    type.execute(player, text);
                } else {
                    this.cancel();
                    Bukkit.getScheduler().runTaskLater(getProvider(), session::startNext, 20);
                }
            }
        }.runTaskTimer(getProvider(), 2L, 2L);
    }

    public enum TalkType {
        ACTION_BAR((player, text) -> {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(text));
        }),
        MESSAGE((player, text) -> {

        }),
        FULL_CHAT((player, text) -> {

        });

        private final BiConsumer<Player, String> consumer;

        TalkType(BiConsumer<Player, String> consumer) {
            this.consumer = consumer;
        }

        public void execute(Player target, String message) {
            consumer.accept(target, message);
        }
    }
}
