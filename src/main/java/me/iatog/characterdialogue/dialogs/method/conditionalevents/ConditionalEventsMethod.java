package me.iatog.characterdialogue.dialogs.method.conditionalevents;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.dialogs.MethodConfiguration;
import me.iatog.characterdialogue.dialogs.MethodContext;
import me.iatog.characterdialogue.enums.CaptureMode;
import me.iatog.characterdialogue.enums.CompletedType;
import me.iatog.characterdialogue.session.DialogSession;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConditionalEventsMethod extends DialogMethod<CharacterDialoguePlugin> {

    public static final Map<UUID, EventData> cache = new HashMap<>();

    // It will wait for a ConditionalEvents event to be fulfilled
    // if not fulfilled the dialog ends, and if fulfilled the dialog continues

    // conditional_events{timeout=200, capture=action}: action_name
    // conditional_events{timeout=200, capture=event}: event_name
    // conditional_events{timeout=200, capture=both}: action_name|event_name
    public ConditionalEventsMethod(CharacterDialoguePlugin provider) {
        super("conditional_events", provider);
        addPluginDependency("ConditionalEvents", () -> {
            Bukkit.getPluginManager().registerEvents(new ConditionalEventListener(provider), provider);
            handleTimeouts();
        });
    }

    @Override
    public void execute(MethodContext context) {
        MethodConfiguration configuration = context.getConfiguration();
        DialogSession session = context.getSession();
        String capture = configuration.getString("capture", "event");
        int timeoutInMillis = (configuration.getInteger("timeout", 60) * 1000);
        String argument = configuration.getArgument();
        CaptureMode mode;

        try {
            mode = CaptureMode.valueOf(capture.toUpperCase());
        } catch(Exception exception) {
            String msg = "Invalid capture mode provided '" +capture + "' in conditional events method";
            context.getSession().sendDebugMessage(msg, "ConditionalEventMethod");
            getProvider().getLogger().severe(msg);
            this.next(context);
            return;
        }

        if(argument.isEmpty()) {
            getProvider().getLogger().warning("No ConditionalEvents event provided.");
            this.next(context);
            return;
        }

        if(mode == CaptureMode.BOTH && !argument.contains("|")) {
            String msg = "When using capture=both you need to specify a valid argument: <eventName>|<action>";
            session.sendDebugMessage(msg, "ConditionalEventsMethod");
            getProvider().getLogger().severe(msg);
            this.destroy(context);
            return;
        }


        long exp = (System.currentTimeMillis() + timeoutInMillis);
        boolean pause = configuration.getBoolean("pause", true);

        EventData data = new EventData(mode, argument, session, exp, context.getConsumer(), pause);
        cache.put(context.getPlayer().getUniqueId(), data);
        if(pause) {
            this.pause(context);
        }
        session.sendDebugMessage("Waiting for conditional_events event...", "ConditionalEventsMethod");
    }

    private void handleTimeouts() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(getProvider(), () -> {

            for(EventData data : cache.values()) {
                long expiration = data.expTime();

                if(System.currentTimeMillis() >= expiration) {
                    data.getSession().getPlayer().sendMessage("Timeout completed");
                    if(!data.consumer().executed()) {
                        data.consumer().accept(CompletedType.DESTROY);
                    } else {
                        data.getSession().destroy();
                    }

                    cache.remove(data.getSession().getPlayer().getUniqueId());
                }
            }/*
            cache.forEach((uuid, data) -> {

            });*/
        }, 10, 20);
    }
}
