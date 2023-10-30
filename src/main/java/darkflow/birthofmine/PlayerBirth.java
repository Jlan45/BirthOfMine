package darkflow.birthofmine;

import org.bukkit.entity.Player;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PlayerBirth {
    private static PlayerBirth instance;

    static {
        try {
            instance = new PlayerBirth();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Connection connection = null;
    Statement statement = null;
    PreparedStatement preparedStatement=null;
    String dburl="jdbc:sqlite:birth.db";

    public static PlayerBirth getInstance(){
        return instance;
    }
    private PlayerBirth() throws SQLException {
        if (this.connection==null){
            try {
                connection= DriverManager.getConnection(dburl);
                String createTableQuery = "CREATE TABLE IF NOT EXISTS birthday (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL, uuid TEXT NOT NULL, birthday TEXT NOT NULL, gifts TEXT)";
                statement=connection.createStatement();
                statement.execute(createTableQuery);
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public List<String> getTodayPlayer() throws SQLException {
        Date today=new Date();
        String todayString=new SimpleDateFormat("MM-dd").format(today);
        String sql="SELECT * FROM birthday WHERE birthday like ?";
        preparedStatement=connection.prepareStatement(sql);
        preparedStatement.setString(1,"%"+todayString);
        ResultSet resultSet=preparedStatement.executeQuery();
        List<String> todayPlayer=new ArrayList<>();
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            todayPlayer.add(name);
        }
        return todayPlayer;
    }

    public void setBirth(Player player, String birthString) throws SQLException {
        String sql="SELECT * FROM birthday WHERE uuid = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,player.getUniqueId().toString());
        ResultSet resultSet=preparedStatement.executeQuery();
        if (resultSet.next()){
            sql="UPDATE birthday SET birthday = ? WHERE uuid = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,birthString);
            preparedStatement.setString(2,player.getUniqueId().toString());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return;
        }
        sql="INSERT INTO birthday (uuid,name,birthday) VALUES (?, ?, ?)";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,player.getUniqueId().toString());
        preparedStatement.setString(2,player.getName());
        preparedStatement.setString(3,birthString);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    public Boolean todayIsBirthday(Player currentPlayer) throws SQLException {
        String sql="SELECT * FROM birthday WHERE uuid = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,currentPlayer.getUniqueId().toString());
        ResultSet resultSet=preparedStatement.executeQuery();
        if (resultSet.next()){
            String birthString=resultSet.getString("birthday");
            Date today=new Date();
            String todayString=new SimpleDateFormat("MM-dd").format(today);
            if (birthString.contains(todayString)){
                return true;
            }
        }
        return false;
    }
    public Boolean setGiftWithName(String playerName, String giftString) throws SQLException {
        String sql="SELECT * FROM birthday WHERE name = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,playerName);
        ResultSet resultSet=preparedStatement.executeQuery();
        if (resultSet.next()){
            sql="UPDATE birthday SET gifts = ? WHERE name = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,giftString);
            preparedStatement.setString(2,playerName);
            preparedStatement.executeUpdate();
            return true;
        }
        return false;
    }
    public Boolean setGiftWithUUID(String playerUUID, String giftString) throws SQLException {
        String sql="SELECT * FROM birthday WHERE uuid = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,playerUUID);
        ResultSet resultSet=preparedStatement.executeQuery();
        if (resultSet.next()){
            sql="UPDATE birthday SET gifts = ? WHERE uuid = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,giftString);
            preparedStatement.setString(2,playerUUID);
            preparedStatement.executeUpdate();
            return true;
        }
        return false;
    }
    public String getGiftWithUUID(String playerUUID)throws SQLException{
        String sql="SELECT * FROM birthday WHERE uuid = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,playerUUID);
        ResultSet resultSet=preparedStatement.executeQuery();
        if (resultSet.next()){
            String giftString=resultSet.getString("gifts");
            return giftString;
        }
        return null;
    }
    public String getGiftWithName(String playerName)throws SQLException{
        String sql="SELECT * FROM birthday WHERE name = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,playerName);
        ResultSet resultSet=preparedStatement.executeQuery();
        if (resultSet.next()){
            String giftString=resultSet.getString("gifts");
            return giftString;
        }
        return null;
    }
}
