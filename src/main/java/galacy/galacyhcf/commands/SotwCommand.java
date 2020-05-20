package galacy.galacyhcf.commands;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.tasks.SotwTask;
import galacy.galacyhcf.utils.Utils;

public class SotwCommand extends VanillaCommand {
    public SotwCommand(String name) {
        super(name, "this isn't for you, look away!", "/sotw <on|off>");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof GPlayer && sender.isOp()) {
            if (args.length == 0)
                sender.sendMessage(Utils.prefix + TextFormat.RED + "Usage: /sotw <on|off>.");
            else {
                if (args[0].equals("on") || args[0].equals("enable")) {
                    if (GalacyHCF.sotwTask != null) {
                        sender.sendMessage(Utils.prefix + TextFormat.RED + "SoTW already started!");
                    } else {
                        GalacyHCF.sotwTask = new SotwTask(GalacyHCF.instance);
                        sender.getServer().getScheduler().scheduleRepeatingTask(GalacyHCF.sotwTask, 20);
                        sender.sendMessage(Utils.prefix + TextFormat.GREEN + "SoTW started!");
                    }
                } else if (args[0].equals("off") || args[0].equals("disable")) {
                    if (GalacyHCF.sotwTask != null) {
                        GalacyHCF.sotwTask.started = false;
                        GalacyHCF.sotwTask.cancel();
                        sender.sendMessage(Utils.prefix + TextFormat.GREEN + "SoTW stopped!");
                    } else sender.sendMessage(Utils.prefix + TextFormat.RED + "SoTW already stopped!");
                }
            }
        }
        return false;
    }
}