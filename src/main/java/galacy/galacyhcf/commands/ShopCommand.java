package galacy.galacyhcf.commands;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.models.Shop;
import galacy.galacyhcf.utils.Utils;

public class ShopCommand extends VanillaCommand {
    public ShopCommand(String name) {
        super(name, "Not for you!", "/shop <item_id> <sell|buy> <amount> <price> [name]");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof GPlayer && sender.isOp()) {
            if (args.length < 4)
                sender.sendMessage(Utils.prefix + TextFormat.RED + "Wrong usage. Usage: /shop <item_id> <sell|buy> <amount> <price> [name]");
            else {
                int itemId = Integer.parseInt(args[0]);
                int type = args[1].equals("buy") ? Shop.buy : Shop.sell;
                int amount = Integer.parseInt(args[2]);
                int price = Integer.parseInt(args[3]);
                String name = args.length == 5 ? args[4] : Item.get(itemId).getName();
                ((GPlayer) sender).shop = new Shop(name, itemId, type, amount, price, null);
                sender.sendMessage(Utils.prefix + TextFormat.YELLOW + "Click on a sign now to create the shop.");
            }
        }
        return false;
    }
}