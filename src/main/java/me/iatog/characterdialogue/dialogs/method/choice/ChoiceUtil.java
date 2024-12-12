package me.iatog.characterdialogue.dialogs.method.choice;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogChoice;
import me.iatog.characterdialogue.dialogs.MethodContext;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import static me.iatog.characterdialogue.util.TextUtils.colorize;

public class ChoiceUtil {

    public static BaseComponent[] getSelectText(int index) {
        YamlDocument file = CharacterDialoguePlugin.getInstance().getFileFactory().getLanguage();
        String text = file.getString("select-choice", "&aClick here to select #%str%").replace("%str%", index + "");
        return new BaseComponent[] { new TextComponent(colorize(text)) };
    }

    public static DialogChoice getByClassName(Class<? extends DialogChoice> clazz) {
        for (DialogChoice target : CharacterDialoguePlugin.getInstance().getCache().getChoices().values()) {
            if (target.getClass() == clazz) {
                return target;
            }
        }

        return null;
    }

    public static boolean isContextValid(MethodContext context) {
        CharacterDialoguePlugin main = CharacterDialoguePlugin.getInstance();
        YamlDocument choicesFile = main.getFileFactory().getChoicesFile();
        String choice = context.getConfiguration().getArgument();


        if (main.getCache().getChoiceSessions().containsKey(context.getPlayer().getUniqueId())) {
            context.getSession().sendDebugMessage("Player already in a choice session.", "ChoiceMethod");
            return false;
        }

        if(choice.isEmpty()) {
            context.getSession().sendDebugMessage("No choice specified, cancelling.", "ChoiceMethod");
            return false;
        }

        if (!choicesFile.contains("choices." + choice)) {
            String msg = "The choice \"" + choice + "\" doesn't exists.";
            main.getLogger().warning(msg);
            context.getSession().sendDebugMessage(msg, "ChoiceMethod");
            return false;
        }

        return true;
    }
}
