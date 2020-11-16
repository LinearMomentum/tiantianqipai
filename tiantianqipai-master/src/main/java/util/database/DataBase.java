package util.database;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class DataBase {
    static Connection connection;
    public DataBase() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        String user="postgres";
        String password="1003134225";
        String url="jdbc:postgresql://localhost:5432/postgres";
        connection=DriverManager.getConnection(url,user,password);//
    }
    public Connection getConnection() {
        return connection;
    }
    public void closeConnect() throws SQLException {
        if (connection!=null)
            connection.close();
    }
    public boolean checkPassword(String username,String password) throws NoSuchAlgorithmException, UnsupportedEncodingException, SQLException {
        MessageDigest messageDigest=MessageDigest.getInstance("MD5");
        byte[]bytes=messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder result= new StringBuilder();
        for(byte b:bytes){
            String tmp=Integer.toHexString(b&0xff);
            if(tmp.length()==1)
                tmp="0"+tmp;
            result.append(tmp);
        }
        String sql="select password from users where username = '"+username+"';";
        Statement statement=connection.createStatement();
        ResultSet resultSet=statement.executeQuery(sql);
        StringBuilder sb=new StringBuilder();
        if(resultSet.next()){
            sb.append(resultSet.getString("password")).append("\t\t");
        }
        return sb.toString().equals(result.toString());
    }
    public boolean signIn(String username,String password,String phoneNumber) throws NoSuchAlgorithmException, UnsupportedEncodingException, SQLException {
        MessageDigest messageDigest=MessageDigest.getInstance("MD5");
        byte[]bytes=messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder result= new StringBuilder();
        for(byte b:bytes){
            String tmp=Integer.toHexString(b&0xff);
            if(tmp.length()==1)
                tmp="0"+tmp;
            result.append(tmp);
        }
        String sql2="select password from users where username = '"+username+"';";
        Statement statement=connection.createStatement();
        ResultSet resultSet=statement.executeQuery(sql2);
        StringBuilder sb=new StringBuilder();
        if(resultSet.next()){
            return false;
        }
        String sql="insert into users(username,password,phoneNumber) values(?,?,?)";
        PreparedStatement preparedStatement=connection.prepareStatement(sql);
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,result.toString());
        preparedStatement.setString(3,phoneNumber);
        preparedStatement.executeUpdate();
        return true;
    }
}
