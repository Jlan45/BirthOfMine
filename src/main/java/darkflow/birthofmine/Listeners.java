package darkflow.birthofmine;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static darkflow.birthofmine.EasyToUseFuncs.*;

public class Listeners implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws SQLException {
        List<String> UserList=PlayerBirth.getInstance().getTodayPlayer();
        Player currentPlayer=event.getPlayer();
        currentPlayer.sendTitle("欢迎回来，"+currentPlayer.getName()+"！","今天有人过生日吗",100,100,100);
        List<String> nameList =new ArrayList<>();
        for (String name:UserList){
            if (currentPlayer.getName().equals(name)){
                currentPlayer.sendTitle("今天是你的生日哦！","快使用getgift命令来收取你的礼物吧！",100,100,100);
                setPlayerFirework(currentPlayer);
            }
            nameList.add(name);
        }
        if(nameList.size()!=0){
            event.getPlayer().sendMessage("今天是"+nameList.toString()+"的生日哦！");
        }

    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        System.out.println("玩家"+event.getPlayer().getName()+"退出了游戏。");
    }
    @EventHandler
    public void onSendGiftClosed(InventoryCloseEvent event) throws IOException, SQLException {
        if(event.getView().getTitle().startsWith("礼物盒To")){
            //先从数据库中查询是否有之前存下的礼物，base64
            ArrayList<ItemStack> itemStacks=new ArrayList<>();
            String base64ItemStacks=PlayerBirth.getInstance().getGiftWithName(event.getView().getTitle().replace("礼物盒To",""));
            if (base64ItemStacks!=null && !base64ItemStacks.equals("")){
                ItemStack[] birthdayItemStacks = itemStackArrayFromBase64(base64ItemStacks);
                itemStacks.addAll(Arrays.asList(birthdayItemStacks));
            }
            for (ItemStack item:event.getInventory().getContents()) {
                if(item==null){
                    continue;
                }
                itemStacks.add(item);
            };
            String itemStacksString=itemStackArrayToBase64(itemStacks.toArray(new ItemStack[itemStacks.size()]));
            PlayerBirth.getInstance().setGiftWithName(event.getView().getTitle().replace("礼物盒To",""),itemStacksString);
        }
    }
    @EventHandler
    public void onGiftClosed(InventoryCloseEvent event) throws IOException, SQLException {
        if(event.getView().getTitle().equals("礼物盒")){
            //先从数据库中查询是否有之前存下的礼物，base64
            ItemStack[] itemStacks=event.getInventory().getContents();
            String itemStacksString=itemStackArrayToBase64(itemStacks);
            PlayerBirth.getInstance().setGiftWithUUID(event.getPlayer().getUniqueId().toString(),itemStacksString);
        }
    }
}
