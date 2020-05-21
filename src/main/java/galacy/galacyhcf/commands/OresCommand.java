package galacy.galacyhcf.commands;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.models.RedisPlayer;

public class OresCommand extends VanillaCommand {
    public OresCommand(String name) {
        super(name, "See your ores-mining stats!", "/ores");
        setAliases(new String[]{"ore"});
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof GPlayer) {
            RedisPlayer data = ((GPlayer) sender).redisData();
            sender.sendMessage(TextFormat.BLUE + "Lapis: " + TextFormat.GRAY + data.lapis);
            sender.sendMessage(TextFormat.AQUA + "Diamonds: " + TextFormat.GRAY + data.diamonds);
            sender.sendMessage(TextFormat.WHITE + "Iron: " + TextFormat.GRAY + data.iron);
            sender.sendMessage(TextFormat.GOLD + "Gold: " + TextFormat.GRAY + data.gold);
            sender.sendMessage(TextFormat.RED + "Redstone: " + TextFormat.GRAY + data.redstone);
        }
        return false;
    }
}