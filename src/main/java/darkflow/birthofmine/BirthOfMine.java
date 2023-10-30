package darkflow.birthofmine;

import darkflow.birthofmine.commands.setBirth;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class BirthOfMine extends JavaPlugin {
    Connection connection = null;
    Statement statement = null;
    @Override
    public void onEnable() {
        // Plugin startup logic

        getLogger().info("欢迎使用BrithOfMine，今天有人需要过生日吗？");
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        getCommand("setBirth").setExecutor(new setBirth());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
