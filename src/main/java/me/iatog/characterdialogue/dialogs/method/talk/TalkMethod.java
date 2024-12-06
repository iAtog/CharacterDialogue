package me.iatog.characterdialogue.dialogs.method.talk;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.dialogs.MethodConfiguration;
import me.iatog.characterdialogue.enums.CompletedType;
import me.iatog.characterdialogue.placeholders.Placeholders;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.util.SingleUseConsumer;
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
import java.util.function.Consumer;

public class TalkMethod extends DialogMethod<CharacterDialoguePlugin> implements Listener {

    // List of players to wait sneak
    private final List<UUID> players;
    /**
     * DESIGN:
     * talk: <type>|<message>
     * talk: message|Lorem ipsum
     * talk: full_chat|Lorem ipsum
     *
     * NEW
     * talk{type='action_bar',sound='BLOCK_SCULK_VEIN_BREAK',volume=0.5,pitch=0.5,tickSpeed=2,skip=false}: Hello
     */
    public TalkMethod(CharacterDialoguePlugin main) {
        super("talk", main);
        this.players = new ArrayList<>();
    }

    @Override
    public void execute(Player player, MethodConfiguration configuration, DialogSession session,
                        SingleUseConsumer<CompletedType> completed) {
        this.players.add(player.getUniqueId());

        animateMessage(player, session, configuration, completed);
    }

    public void animateMessage(Player player,
                               DialogSession session,
                               MethodConfiguration configuration,
                               Consumer<CompletedType> completed) {
        String message = configuration.getArgument();
        UUID uuid = player.getUniqueId();
        final String npcName = session.getDialogue().getDisplayName();
        final String translatedMessage = Placeholders.translate(player, message);

        float volume = configuration.getFloat("volume", 0.5f);
        float pitch = configuration.getFloat("pitch", 0.5f);
        boolean skip = configuration.getBoolean("skip", false);

        int ticks = configuration.getInteger("tickSpeed", 2);

        TalkType type;
        Sound sound;

        try {
            type = TalkType.valueOf(configuration.getString("type", "ACTION_BAR").toUpperCase());
            sound = Sound.valueOf(configuration.getString("sound", "BLOCK_STONE_BUTTON_CLICK_OFF").toUpperCase());
        } catch(EnumConstantNotPresentException ex) {
            getProvider().getLogger().severe("The line L" + session.getCurrentIndex() + " in " + session.getDialogue().getName() + " is not valid. (parse error)");
            session.sendDebugMessage("Error parsing data: " + ex.getMessage(), "TalkMethod:60");
            completed.accept(CompletedType.DESTROY);
            return;
        }

        if(message.isEmpty()) {
            getProvider().getLogger().severe("The line L" + session.getCurrentIndex() + " in " + session.getDialogue().getName() + " is not valid. (empty message)");
            session.sendDebugMessage("Error parsing data: message is empty", "TalkMethod:80");
            completed.accept(CompletedType.DESTROY);
            return;
        }

        new BukkitRunnable() {
            int index = 0;
            boolean finished = false;

            @Override
            public void run() {
                try {
                    if ((!players.contains(uuid) && skip && !isCancelled()) && !finished) {
                        stopAnimation();
                        return;
                    }

                    if (index < message.length()) {
                        animateText();
                    } else {
                        cancel();

                        if(!finished) {
                            finishAnimation();
                        }
                    }
                } catch (NullPointerException ex) {
                    handleException(ex);
                }
            }

            private void stopAnimation() {
                this.cancel();
                this.finished = true;
                type.execute(player, translatedMessage, npcName);
                player.playSound(player.getLocation(), sound, volume, pitch);
                session.sendDebugMessage("Finished talk because: " + !players.contains(uuid) + " | " + skip + " | " + !isCancelled(), "TalkMethod:134");
                players.remove(uuid);
                completed.accept(CompletedType.CONTINUE);
                //session.startNext();
            }

            private void animateText() {
                String writingMessage = translatedMessage.substring(0, index + 1);
                index++;
                player.playSound(player.getLocation(),
                        sound,
                        volume,
                        pitch);
                type.execute(player, writingMessage, npcName);
            }

            private void finishAnimation() {
                if (!this.finished) {
                    completed.accept(CompletedType.CONTINUE);
                    session.sendDebugMessage("Starting next... (else) (finishAnimation)", "TalkMethod:149");
                }
                this.finished = true;
                session.sendDebugMessage("Finished because message " + index + " < " + message.length() +
                        " (cancelled: " + isCancelled() + ")", "TalkMethod:152");
                players.remove(uuid);
                this.cancel();
            }

            private void handleException(Exception ex) {
                getProvider().getLogger().severe("exception in talkMethod [" + message + "]");
                ex.printStackTrace();
                completed.accept(CompletedType.DESTROY);
                this.cancel();
                this.finished = true;
            }

        }.runTaskTimer(getProvider(), 10L, ticks);
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
}
