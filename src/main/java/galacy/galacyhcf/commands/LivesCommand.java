package galacy.galacyhcf.commands;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.models.RedisPlayer;
import galacy.galacyhcf.utils.Utils;

public class LivesCommand extends VanillaCommand {
    public LivesCommand(String name) {
        super(name, "Lives Command", "/lives");
        setAliases(new String[]{"life", "live"});
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        if (sender instanceof GPlayer) {
            RedisPlayer data = GalacyHCF.redis.getPlayer(((GPlayer) sender).xuid);
            sender.sendMessage(Utils.prefix + TextFormat.GREEN + "You have " + TextFormat.YELLOW + data.lives + TextFormat.GREEN + " life.");
        }
        return false;
    }
}
