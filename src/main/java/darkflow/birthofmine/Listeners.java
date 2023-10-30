package darkflow.birthofmine;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Listeners implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws SQLException {
        List<String> UserList=new PlayerBirth().getTodayPlayer();
        Player currentPlayer=event.getPlayer();
        currentPlayer.sendTitle("欢迎回来，"+currentPlayer.getName()+"！","今天有人过生日吗",100,100,100);
        List<String> nameList =new ArrayList<>();
        for (String uuid:UserList){
            if (currentPlayer.getUniqueId().toString().equals(uuid)){
                currentPlayer.sendTitle("今天是你的生日哦！","快去找管理员领取生日礼物吧！",100,100,100);
            }
            Player player= Bukkit.getPlayer(UUID.fromString(uuid));
            if(player==null){
                player= (Player) Bukkit.getOfflinePlayer(UUID.fromString(uuid));
            }
            if (player!=null){
                nameList.add(player.getName());
            }
        }
        if(nameList.size()!=0){
            event.getPlayer().sendMessage("今天是"+nameList.toString()+"的生日哦！");
        }

    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) throws SQLException {
        System.out.println("玩家"+event.getPlayer().getName()+"退出了游戏。");
    }
}
