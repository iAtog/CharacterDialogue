package me.iatog.characterdialogue.dialogs.method.choice;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.iatog.characterdialogue.misc.Choice;
import me.iatog.characterdialogue.util.TextUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ChoiceGUI {

    private boolean destroy = true;

    public void buildGUI(ChoiceData data) {
        String model = data.getConfigFile().getString("choice.text-model", "&c{I}. &7{S}");
        Gui gui = Gui.gui().title(Component.text("Select one"))
                .rows(5)
                .disableAllInteractions()
                .type(GuiType.CHEST)
                .create();

        gui.getFiller().fillBorder(ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).asGuiItem());

        data.getChoiceSession().getChoices().forEach((index, choice) -> {
            GuiItem choiceItem = createChoiceItem(index, choice, model);
            gui.addItem(choiceItem);
        });


        gui.setCloseGuiAction(ac -> {
            if(destroy) {
                ChoiceUtil.removeTaskIfPresent(ac.getPlayer().getUniqueId());
                data.getChoiceSession().destroy();
                data.getDialogSession().destroy();
            }
        });

        gui.open(data.getPlayer());
    }

    public GuiItem createChoiceItem(int index, Choice choice, String model) {
        /*String parsedModel = TextUtils.colorize(model.replace("{I}",
                String.valueOf(index)).replace("{S}",
                choice.getMessage()));*/

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
        switch (index) {
            case 1 -> {return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDJkNGE2OTkzN2UwYmVhZGMzODQyNmMwOTk0YjUwZDk1MDQwNmZkOGRhOWYzMWM1ODJkNDZmM2I5YmZjNGM1YiJ9fX0=";}
            case 2 -> {return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzBhNmM3YTBkNjU4YmI5MGUyN2I1OTM0ZjYyYTVlMTVjYzljMTFjODdhZTE0NjRhNGU3OWVhNjY1MjNiYTM2MSJ9fX0=";}
            case 3 -> {return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTYxYjMxYTg3Yjc4MjYyYzYzZTk0NzE0ZTU2MjRhMmFiNTk1MGY3NWRlZTMyY2MzMDI2YTVmYTc4MjM0NjhkZSJ9fX0=";}
            case 4 -> {return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2FkZmQzYzk5OTY3ZDMyNzQ5MDJlY2I2ZTk4NjU4YWNmZGIzOTE4NzE3YjJlOTAzN2Y2MWMzYjRlMDllMmExIn19fQ==";}
            case 5 -> {return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJiYWYwMTkwOTIyMWI5YWJlOTQ1YWZlN2RmZGI3MmYzMTczMzExZTU2MjAxOTRkZDI3MDExYTZkNTU0ZmZjOCJ9fX0=";}
            default -> {return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVkMjAzMzBkYTU5YzIwN2Q3ODM1MjgzOGU5MWE0OGVhMWU0MmI0NWE5ODkzMjI2MTQ0YjI1MWZlOWI5ZDUzNSJ9fX0=";}
        }
    }
}
