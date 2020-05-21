package galacy.galacyhcf.commands;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.models.RedisPlayer;
import galacy.galacyhcf.utils.Utils;

public class StatsCommand extends VanillaCommand {
    public StatsCommand(String name) {
        super(name, "See your stats!", "/stats");
        setAliases(new String[]{"stat", "kills", "deaths"});
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof GPlayer) {
            RedisPlayer data = ((GPlayer) sender).redisData();
            sender.sendMessage(Utils.prefix + TextFormat.YELLOW + "You have killed " + TextFormat.RED + data.kills + TextFormat.YELLOW + " people and died " + TextFormat.RED + data.deaths + TextFormat.YELLOW + " time.");
        }
        return false;
    }
}
