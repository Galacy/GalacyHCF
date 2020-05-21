package galacy.galacyhcf.commands;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.utils.Utils;

public class BalanceCommand extends VanillaCommand {
    public BalanceCommand(String name) {
        super(name, "Balance Command", "/balance");
        setAliases(new String[]{"mymoney", "mybalance", "money", "cash"});
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof GPlayer) {
            sender.sendMessage(Utils.prefix + TextFormat.YELLOW + "Your balance is: " + TextFormat.GREEN + "$" + ((GPlayer) sender).balance);
        }
        return false;
    }
}