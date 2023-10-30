package darkflow.birthofmine.commands;

import darkflow.birthofmine.PlayerBirth;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.sql.SQLException;

public class sendGift implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            String birthName=args[0];
            try {
                Player tmpPlayer=Bukkit.getPlayer(birthName);
                if(tmpPlayer==null){
                    sender.sendMessage("§c[警告] "+"玩家"+birthName+"不在线或不存在哦！");
                    return true;
                }
                if(PlayerBirth.getInstance().todayIsBirthday(Bukkit.getPlayer(birthName))){
                    Inventory storageGUI = Bukkit.createInventory(null, 27, "礼物盒To"+birthName); // 创建一个27格的仓库页面
                    ((Player) sender).getPlayer().openInventory(storageGUI);
                    return true;
                }
                else{
                    sender.sendMessage("§c[警告] "+"今天不是"+birthName+"的生日哦！");
                    return true;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return false;
    }
}
