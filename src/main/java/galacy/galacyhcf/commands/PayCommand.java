package galacy.galacyhcf.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.utils.Utils;

public class PayCommand extends VanillaCommand {
    public PayCommand(String name) {
        super(name, "pay your friends!", "/pay");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof GPlayer) {
            if (args.length == 2) {
                Player player = sender.getServer().getPlayer(args[0]);
                if (player != null) {
                    if (player instanceof GPlayer) {
                        int amount = Integer.parseInt(args[1]);
                        if (amount < 1)
                            sender.sendMessage(Utils.prefix + TextFormat.RED + "Your payment can't be this small, dude seriously!");
                        else {
                            if (((GPlayer) sender).balance < amount)
                                sender.sendMessage(Utils.prefix + TextFormat.RED + "You don't have that much money in your balance.");
                            else {
                                ((GPlayer) sender).updateBalance(((GPlayer) sender).balance - amount);
                                ((GPlayer) player).updateBalance(((GPlayer) player).balance + amount);
                                player.sendMessage(Utils.prefix + TextFormat.GREEN + sender.getName() + " just paid you " + TextFormat.YELLOW + "$" + amount);
                                sender.sendMessage(Utils.prefix + TextFormat.GREEN + "You just sent " + TextFormat.YELLOW + "$" + amount + TextFormat.GREEN + " to " + player.getName());
                            }
                        }
                    }
                } else sender.sendMessage(Utils.prefix + TextFormat.RED + "This player doesn't seem to be online!");
            } else sender.sendMessage(Utils.prefix + TextFormat.RED + "Wrong usage. /pay <player> <amount>.");
        }
        return false;
    }
}
