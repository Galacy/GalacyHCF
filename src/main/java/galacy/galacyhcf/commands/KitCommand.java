package galacy.galacyhcf.commands;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.utils.Utils;

import java.util.Date;

public class KitCommand extends VanillaCommand {
    public KitCommand(String name) {
        super(name, "See your ores-mining stats!", "/kit");
        setAliases(new String[]{"kits"});
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof GPlayer) {
            FormWindowSimple window = new FormWindowSimple(TextFormat.BOLD + "" + TextFormat.GOLD + "Daily Kits", TextFormat.GRAY + "You can unlock kits by purchasing ranks from the store.");
            window.addButton(new ElementButton(TextFormat.BOLD + "" + TextFormat.DARK_PURPLE + "Galacy Kit " + TextFormat.RESET +
                    (((GPlayer) sender).rank >= GPlayer.GALACY ?
                            (((GPlayer) sender).redisData().galacyKit > System.currentTimeMillis() ?
                                    TextFormat.YELLOW + "Cooldown " + Utils.timerHour.format(new Date((((GPlayer) sender).redisData().galacyKit) - System.currentTimeMillis())) : TextFormat.GREEN + "(AVAILABLE)") :
                            TextFormat.RED + "(LOCKED)")));
            window.addButton(new ElementButton(TextFormat.BOLD + "" + TextFormat.AQUA + "Diamond Kit " + TextFormat.RESET +
                    (((GPlayer) sender).rank >= GPlayer.SOLAR ?
                            (((GPlayer) sender).redisData().diamondKit > System.currentTimeMillis() ?
                                    TextFormat.YELLOW + "Cooldown " + Utils.timerHour.format(new Date((((GPlayer) sender).redisData().diamondKit) - System.currentTimeMillis())) : TextFormat.GREEN + "(AVAILABLE)") :
                            TextFormat.RED + "(LOCKED)")));
            window.addButton(new ElementButton(TextFormat.BOLD + "" + TextFormat.WHITE + "Rogue Kit " + TextFormat.RESET +
                    (((GPlayer) sender).rank >= GPlayer.NEBULA ?
                            (((GPlayer) sender).redisData().rogueKit > System.currentTimeMillis() ?
                                    TextFormat.YELLOW + "Cooldown " + Utils.timerHour.format(new Date((((GPlayer) sender).redisData().rogueKit) - System.currentTimeMillis())) : TextFormat.GREEN + "(AVAILABLE)") :
                            TextFormat.RED + "(LOCKED)")));
            window.addButton(new ElementButton(TextFormat.BOLD + "" + TextFormat.BLUE + "Archer Kit " + TextFormat.RESET +
                    (((GPlayer) sender).rank >= GPlayer.NOVA ?
                            (((GPlayer) sender).redisData().archerKit > System.currentTimeMillis() ?
                                    TextFormat.YELLOW + "Cooldown " + Utils.timerHour.format(new Date((((GPlayer) sender).redisData().archerKit) - System.currentTimeMillis())) : TextFormat.GREEN + "(AVAILABLE)") :
                            TextFormat.RED + "(LOCKED)")));
            window.addButton(new ElementButton(TextFormat.BOLD + "" + TextFormat.GOLD + "Bard Kit " + TextFormat.RESET +
                    (((GPlayer) sender).rank >= GPlayer.STAR ?
                            (((GPlayer) sender).redisData().bardKit > System.currentTimeMillis() ?
                                    TextFormat.YELLOW + "Cooldown " + Utils.timerHour.format(new Date((((GPlayer) sender).redisData().bardKit) - System.currentTimeMillis())) : TextFormat.GREEN + "(AVAILABLE)") :
                            TextFormat.RED + "(LOCKED)")));
            window.addButton(new ElementButton(TextFormat.BOLD + "" + TextFormat.GREEN + "Miner Kit " + TextFormat.RESET +
                    (((GPlayer) sender).rank >= GPlayer.STAR ?
                            (((GPlayer) sender).redisData().minerKit > System.currentTimeMillis() ?
                                    TextFormat.YELLOW + "Cooldown " + Utils.timerHour.format(new Date((((GPlayer) sender).redisData().minerKit) - System.currentTimeMillis())) : TextFormat.GREEN + "(AVAILABLE)") :
                            TextFormat.RED + "(LOCKED)")));

            ((GPlayer) sender).showFormWindow(window);
        }
        return false;
    }
}