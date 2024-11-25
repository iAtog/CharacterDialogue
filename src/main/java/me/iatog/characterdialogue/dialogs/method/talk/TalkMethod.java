package me.iatog.characterdialogue.dialogs.method.talk;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
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
     * simple
     */
    public TalkMethod(CharacterDialoguePlugin main) {
        super("talk", main);
        this.players = new ArrayList<>();
    }

    @Override
    public void execute(Player player, String arg, DialogSession session, SingleUseConsumer<CompletedType> completed) {
        String[] args = arg.split("\\|", 2);
        TalkConfiguration configuration = new TalkConfiguration(session);
        //session.sendDebugMessage("Session paused", "TalkMethod:52");

        try {
            String[] typeArgs = args[0].split("[(),]");
            configuration.setType(TalkType.valueOf(typeArgs[0].toUpperCase()));

            if (typeArgs.length > 1) {
                String[] soundSplit = typeArgs[1].split("/");
                if(soundSplit.length > 1) {
                    configuration.setSound(Sound.valueOf(soundSplit[0]));
                    configuration.setVolume(Float.parseFloat(soundSplit[1]));
                    configuration.setPitch(Float.parseFloat(soundSplit[2]));
                } else {
                    configuration.setSound(Sound.valueOf(typeArgs[1]));
                }
            }
            if (typeArgs.length > 2) {
                configuration.setTickSpeed(Long.parseLong(typeArgs[2]));
            }
            if (typeArgs.length > 3) {
                configuration.setSkippable(Boolean.parseBoolean(typeArgs[3]));
            }

            configuration.setMessage(args[1]);
        } catch(IndexOutOfBoundsException|NullPointerException ex) {
            getProvider().getLogger().severe("The line L" + session.getCurrentIndex() + " in " + session.getDialogue().getName() + " is not valid. (parse error)");
            session.sendDebugMessage("Error parsing data: " + ex.getMessage(), "TalkMethod:73");
            completed.accept(CompletedType.DESTROY);
            return;
        }

        if(configuration.getMessage().isEmpty()) {
            getProvider().getLogger().severe("The line L" + session.getCurrentIndex() + " in " + session.getDialogue().getName() + " is not valid. (empty message)");
            session.sendDebugMessage("Error parsing data: message is empty", "TalkMethod:80");
            completed.accept(CompletedType.DESTROY);
            return;
        }

        this.players.add(player.getUniqueId());

        animateMessage(player, configuration, completed);
    }

    public void animateMessage(Player player, TalkConfiguration configuration, Consumer<CompletedType> completed) {
        UUID uuid = player.getUniqueId();
        final DialogSession session = configuration.getSession();
        final String npcName = session.getDialogue().getDisplayName();
        final String translatedMessage = Placeholders.translate(player, configuration.getMessage());

        new BukkitRunnable() {
            int index = 0;
            boolean finished = false;

            @Override
            public void run() {
                try {
                    if ((!players.contains(uuid) && configuration.isSkippable() && !isCancelled()) && !finished) {
                        stopAnimation();
                        return;
                    }

                    if (index < configuration.getMessage().length()) {
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
                configuration.getType().execute(player, translatedMessage, npcName);
                player.playSound(player.getLocation(), configuration.getSound(), 1f, 2f);
                session.sendDebugMessage("Finished talk because: " + !players.contains(uuid) + " | " + configuration.isSkippable() + " | " + !isCancelled(), "TalkMethod:134");
                players.remove(uuid);
                completed.accept(CompletedType.CONTINUE);
                //session.startNext();
            }

            private void animateText() {
                String writingMessage = translatedMessage.substring(0, index + 1);
                index++;
                player.playSound(player.getLocation(),
                        configuration.getSound(),
                        configuration.getVolume(),
                        configuration.getPitch());
                configuration.getType().execute(player, writingMessage, npcName);
            }

            private void finishAnimation() {
                if (!this.finished) {
                    completed.accept(CompletedType.CONTINUE);
                    session.sendDebugMessage("Starting next... (else) (finishAnimation)", "TalkMethod:149");
                }
                this.finished = true;
                session.sendDebugMessage("Finished because message " + index + " < " + configuration.getMessage().length() +
                        " (cancelled: " + isCancelled() + ")", "TalkMethod:152");
                players.remove(uuid);
                this.cancel();
            }

            private void handleException(Exception ex) {
                getProvider().getLogger().severe("exception in talkMethod [" + configuration.getMessage() + "]");
                ex.printStackTrace();
                completed.accept(CompletedType.DESTROY);
                this.cancel();
                this.finished = true;
            }

        }.runTaskTimer(getProvider(), 10L, configuration.getTickSpeed());
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