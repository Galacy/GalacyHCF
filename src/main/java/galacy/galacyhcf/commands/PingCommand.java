package galacy.galacyhcf.commands;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.models.GPlayer;

public class PingCommand extends VanillaCommand {
    public PingCommand(String name) {
        super(name, "Ping Command", "/ping");
        setAliases(new String[]{"ms", "latency", "internet"});
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof GPlayer) {
            TextFormat color = ((GPlayer) sender).getPing() < 50 ? TextFormat.GREEN : ((GPlayer) sender).getPing() < 200 ? TextFormat.YELLOW : TextFormat.RED;
            sender.sendMessage(TextFormat.GREEN + "Your ping is: " + color + ((GPlayer) sender).getPing() + "ms.");
        }
        return false;
    }
}
