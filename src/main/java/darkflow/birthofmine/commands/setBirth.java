package darkflow.birthofmine.commands;

import darkflow.birthofmine.PlayerBirth;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class setBirth implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            String BirthString = args[0];
            //判断birthstring是否为合法日期字符串，使用正则
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                format.parse(BirthString);
            } catch (ParseException e) {
                return false;
            }
            try {
                new PlayerBirth().setBirth(((Player) sender).getUniqueId().toString(),BirthString);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}

