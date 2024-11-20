package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.util.TextUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveMethod extends DialogMethod<CharacterDialoguePlugin> {

    public GiveMethod() {
        super("give");
    }

    @Override
    public void execute(Player player, String arg, DialogSession session) {
        // DESIGN = GIVE: GOLDEN_APPLE,1
        Material material;
        int amount = 1;
        try {
            if(arg.contains(",")) {
                String[] parts = arg.split(",");
                material = Material.valueOf(parts[0].toUpperCase());
                amount = Integer.parseInt(parts[1]);
            } else {
                material = Material.valueOf(arg.toUpperCase());
            }
        } catch(NumberFormatException|EnumConstantNotPresentException|IndexOutOfBoundsException e) {
            player.sendMessage(TextUtils.colorize("&c&lFatal error ocurred."));
            session.destroy();
            return;
        }

        player.getInventory().addItem(new ItemStack(material, amount));
    }
}
