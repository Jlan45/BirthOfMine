package darkflow.birthofmine;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PlayerBirth {
    Connection connection = null;
    Statement statement = null;
    String dburl="jdbc:sqlite:birth.db";
    private Statement PlayerBirth() throws SQLException {
        if (this.connection==null){
            try {
                connection= DriverManager.getConnection("jdbc:sqlite:birth.db");
                String createTableQuery = "CREATE TABLE IF NOT EXISTS birthday (id INTEGER PRIMARY KEY AUTOINCREMENT, uuid TEXT NOT NULL, birthday TEXT NOT NULL)";
                statement=connection.createStatement();
                statement.execute(createTableQuery);
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        this.statement = connection.createStatement();
        return statement;
    }
    public List<String> getTodayPlayer() throws SQLException {
        Date today=new Date();
        String todayString=new SimpleDateFormat("MM-dd").format(today);
        String sql="SELECT * FROM birthday WHERE birthday like '%"+todayString+"'";
        this.statement=PlayerBirth();
        ResultSet resultSet=this.statement.executeQuery(sql);
        List<String> todayPlayer=new ArrayList<>();
        while (resultSet.next()) {
            String name = resultSet.getString("uuid");
            todayPlayer.add(name);
        }
        return todayPlayer;
    }

    public void setBirth(String playerUUID, String birthString) throws SQLException {
        String sql="SELECT * FROM birthday WHERE uuid = '"+playerUUID+"'";
        this.statement=PlayerBirth();
        ResultSet resultSet=this.statement.executeQuery(sql);
        if (resultSet.next()){
            this.statement.close();
            sql="UPDATE birthday SET birthday = '"+birthString+"' WHERE uuid = '"+playerUUID+"'";
            this.statement=PlayerBirth();
            this.statement.execute(sql);
            this.statement.close();
            return;
        }
        sql="INSERT INTO birthday (uuid,birthday) VALUES ('"+playerUUID+"','"+birthString+"')";
        try {
            this.statement=PlayerBirth();
            this.statement.execute(sql);
            this.statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
