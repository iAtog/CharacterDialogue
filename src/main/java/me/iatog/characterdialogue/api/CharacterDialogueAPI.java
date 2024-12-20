package me.iatog.characterdialogue.api;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.adapter.AdaptedNPC;
import me.iatog.characterdialogue.api.dialog.Dialogue;
import me.iatog.characterdialogue.enums.ClickType;
import me.iatog.characterdialogue.enums.CompletedType;
import me.iatog.characterdialogue.libraries.ApiImplementation;
import me.iatog.characterdialogue.libraries.HologramLibrary;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.util.SingleUseConsumer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public interface CharacterDialogueAPI {

    public static CharacterDialogueAPI get() {
        return new ApiImplementation(CharacterDialoguePlugin.getInstance());
    }

    Dialogue getDialogue(String name);

    void reloadHolograms();

    void loadHologram(int npcId);

    boolean readDialogBy(Player player, String dialog) throws IOException;

    boolean wasReadedBy(Player player, String dialog);

    boolean readDialogBy(Player player, Dialogue dialog);

    boolean wasReadedBy(Player player, Dialogue dialog);

    void runDialogue(Player player, Dialogue dialogue, boolean debugMode, AdaptedNPC npc);

    void runDialogue(Player player, String dialogueName, boolean debugMode, AdaptedNPC npc);

    void runDialogueExpression(Player player, String dialog);

    void runDialogueExpression(Player player, String dialog, String npcName);

    void runDialogueExpression(Player player, String dialog, String npcName, SingleUseConsumer<CompletedType> onComplete, DialogSession session, AdaptedNPC npc);

    void runDialogueExpressions(Player player, List<String> lines, ClickType type, AdaptedNPC npc, String displayName, String dialogueName);

    void runDialogueExpressions(Player player, List<String> lines, String displayName, String dialogueName);

    Dialogue getNPCDialogue(int id);

    Map<String, Dialogue> getDialogues();

    String getNPCDialogueName(int id);

    int getBukkitVersion();

    boolean enableMovement(Player player);

    boolean disableMovement(Player player);

    boolean canEnableMovement(Player player);

    Pattern getLineRegex();

    HologramLibrary getHologramLibrary();
}
