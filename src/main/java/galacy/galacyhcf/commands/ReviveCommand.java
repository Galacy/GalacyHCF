package galacy.galacyhcf.commands;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.models.RedisPlayer;
import galacy.galacyhcf.utils.Utils;

public class ReviveCommand extends VanillaCommand {
    public ReviveCommand(String name) {
        super(name, "Revive your friends", "/revive <player>");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof GPlayer) {
            if (args.length == 0)
                sender.sendMessage(Utils.prefix + TextFormat.RED + "You have to specify the player you want to revive like this: /revive <player>.");
            else {
                RedisPlayer player = GalacyHCF.redis.getPlayerByName(args[0]);
                if (player == null)
                    sender.sendMessage(Utils.prefix + TextFormat.RED + "This player does not exist.");
                else {
                    RedisPlayer senderData = ((GPlayer) sender).redisData();
                    if ((System.currentTimeMillis() / 1000) < player.deathban) {
                        player.updateDeathban((int) (System.currentTimeMillis() / 1000));
                        senderData.updateLives(senderData.lives - 1);
                        sender.sendMessage(Utils.prefix + TextFormat.GREEN + "Player successfully revived.");
                    } else sender.sendMessage(Utils.prefix + TextFormat.RED + "This player is not deathbanned.");
                }
            }
        }
        return false;
    }
}