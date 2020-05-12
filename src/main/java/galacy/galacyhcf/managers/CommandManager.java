package galacy.galacyhcf.commands;

import galacy.galacyhcf.GalacyHCF;

public class CommandManager {
    public static void register() {
        GalacyHCF.instance.getServer().getCommandMap().register("GalacyHCF", new FactionCommand("faction"));
        GalacyHCF.instance.getServer().getCommandMap().register("GalacyHCF", new PingCommand("ping"));
    }
}
