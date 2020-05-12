package galacy.galacyhcf.managers;

import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.commands.FactionCommand;
import galacy.galacyhcf.commands.PingCommand;

public class CommandManager {
    public static void register() {
        GalacyHCF.instance.getServer().getCommandMap().register("GalacyHCF", new FactionCommand("faction"));
        GalacyHCF.instance.getServer().getCommandMap().register("GalacyHCF", new PingCommand("ping"));
    }
}
