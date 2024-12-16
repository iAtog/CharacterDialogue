package me.iatog.characterdialogue.dialogs.method.choice;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.misc.Choice;
import me.iatog.characterdialogue.util.TextUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ChoiceGUI {

    private final String empty = TextUtils.colorize("&7");
    private boolean destroy = true;

    private final CharacterDialoguePlugin main;

    public ChoiceGUI(CharacterDialoguePlugin main) {
        this.main = main;
    }

    public void buildGUI(ChoiceData data) {
        String model = data.getConfigFile().getString("choice.text-model", "&c{I}. &7{S}");
        Gui gui = Gui.gui().title(Component.text("Select one"))
              .rows(5)
              .disableAllInteractions()
              .type(GuiType.CHEST)
              .create();

        gui.getFiller().fillBorder(
              ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE)
                    .name(Component.text(empty)).asGuiItem());

        data.getChoiceSession().getChoices().forEach((index, choice) -> {
            GuiItem choiceItem = createChoiceItem(index, choice, model);
            gui.addItem(choiceItem);
        });


        gui.setCloseGuiAction(ac -> {
            if (destroy) {
                ChoiceUtil.removeTaskIfPresent(ac.getPlayer().getUniqueId());
                data.getChoiceSession().destroy();
                data.getDialogSession().destroy();
            }
        });

        gui.open(data.getPlayer());
    }

    public GuiItem createChoiceItem(int index, Choice choice, String model) {
        return ItemBuilder
              .skull(new ItemStack(Material.PLAYER_HEAD, index))
              .texture(getTexture(index))
              .amount(index)
              .name(Component.text(TextUtils.colorize(choice.getMessage())))
              .lore(Arrays.asList(Component.text(TextUtils.colorize("&7")), Component.text(TextUtils.colorize("&a&lClick here to select #" + index))))
              .asGuiItem(action -> {
                  destroy = false;
                  Player player = (Player) action.getWhoClicked();
                  player.closeInventory();
                  ChoiceUtil.runChoice(player, index);

              });
    }

    public String getTexture(int index) {
        YamlDocument config = main.getFileFactory().getConfig();
        return config.getString("choice.number-heads." + index, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVkMjAzMzBkYTU5YzIwN2Q3ODM1MjgzOGU5MWE0OGVhMWU0MmI0NWE5ODkzMjI2MTQ0YjI1MWZlOWI5ZDUzNSJ9fX0=");
    }
}
