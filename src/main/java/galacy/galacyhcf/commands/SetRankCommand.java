package galacy.galacyhcf.commands;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.providers.SQLStatements;
import galacy.galacyhcf.utils.Utils;

import java.sql.SQLException;

public class SetRankCommand extends VanillaCommand {
    public SetRankCommand(String name) {
        super(name, "Set a player's rank.", "/setrank <rank> <xuid>");
        setAliases(new String[]{"rank"});
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender.isOp()) {
            if (args.length != 2) {
                sender.sendMessage(Utils.prefix + TextFormat.RED + "Wrong usage, not enough arguments.");

                return false;
            }
            String currentTime = Utils.dateFormat.format(new java.util.Date());
            int rank;
            try {
                rank = Integer.parseInt(args[0]);
            } catch (NumberFormatException exception) {
                sender.sendMessage(Utils.prefix + TextFormat.RED + "Bad rank number.");

                return false;
            }

            try {
                GalacyHCF.mysql.exec(SQLStatements.updatePlayerRank.
                        replace("$updated_at", currentTime).
                        replace("$xuid", args[1]).
                        replace("$rank", String.valueOf(rank)));
            } catch (SQLException e) {
                sender.sendMessage(Utils.prefix + TextFormat.RED + "An error occurred.");
            }
        } else sender.sendMessage(Utils.prefix + TextFormat.RED + "You do not have access to this command.");
        return false;
    }
}
