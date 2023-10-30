package darkflow.birthofmine.commands;

import darkflow.birthofmine.PlayerBirth;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static darkflow.birthofmine.EasyToUseFuncs.itemStackArrayFromBase64;

public class getGift implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            try {
                if(PlayerBirth.getInstance().todayIsBirthday((Player) sender)){
                    Inventory storageGUI = Bukkit.createInventory(null, 27, "礼物盒"); // 创建一个27格的仓库页面
                    String base64Item = PlayerBirth.getInstance().getGiftWithUUID(((Player) sender).getUniqueId().toString());
                    if (base64Item == null || base64Item.equals("")) {
                        sender.sendMessage("§c[警告] "+"你还没有礼物哦！");
                        return true;
                    }
                    ItemStack[] birthdayItemStacks =itemStackArrayFromBase64(base64Item);
                    for (ItemStack item:birthdayItemStacks) {
                        if(item==null){
                            continue;
                        }
                        storageGUI.addItem(item);
                    }
                    // 打开仓库页面给玩家
                    ((Player) sender).getPlayer().openInventory(storageGUI);
                    return true;
                }
                else{
                    sender.sendMessage("§c[警告] "+"今天不是你的生日哦！");
                    return true;
                }

            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
