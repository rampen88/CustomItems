package me.rampen88.customitems.commands.subcommands;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.crafting.ItemHandler;
import me.rampen88.customitems.crafting.SimpleItem;
import me.rampen88.customitems.util.MiscUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand extends SubCommand{

	public GiveCommand(CustomItems plugin){
		super(plugin, "custom.items.give", "give", "get");
	}

	@Override
	protected void execute(CommandSender sender, String[] args){
		if(args[0].equals("give")){
			giveCommand(sender, args);
		}else if(sender instanceof Player){
			getCommand((Player) sender, args);
		}else{
			sender.sendMessage(util.getMessage("NoPermission"));
		}
	}

	private void getCommand(Player target, String[] args){
		if(args.length < 3){
			target.sendMessage(util.getMessage("Commands.Get.Usage"));
			return;
		}
		giveItem(target, target, args[1], args[2], false, util, plugin.getItemHandler());
	}

	private void giveCommand(CommandSender sender, String[] args){
		if(args.length < 4){
			sender.sendMessage(util.getMessage("Commands.Give.Usage"));
			return;
		}

		Player player = Bukkit.getPlayer(args[1]);
		if(player == null){
			sender.sendMessage(util.getMessage("Commands.Give.NotOnline").replace("%player%", args[1]));
			return;
		}
		giveItem(sender, player, args[2], args[3], true, util, plugin.getItemHandler());
	}

	public static void giveItem(CommandSender sender, Player target, String itemName, String amountString, boolean sendMessageToSender, MiscUtil util, ItemHandler itemHandler){
		SimpleItem item = itemHandler.getItemByName(itemName);
		if(item == null){
			sender.sendMessage(util.getMessage("Commands.ItemError"));
			return;
		}

		Integer amount = util.parseInt(amountString);
		if(amount == null){
			sender.sendMessage(util.getMessage("Commands.NotANumber"));
			return;
		}

		ItemStack itemStack = item.getItem();
		itemStack.setAmount(amount);

		target.getInventory().addItem(itemStack);
		target.sendMessage(util.getMessage("Commands.Received").replace("%amount%", amount.toString()).replace("%item%", item.getName()));
		if(sendMessageToSender)
			sender.sendMessage(util.getMessage("Commands.Give.Success").replace("%amount%", amount.toString()).replace("%item%", item.getName()).replace("%player%", target.getName()));
	}

}
