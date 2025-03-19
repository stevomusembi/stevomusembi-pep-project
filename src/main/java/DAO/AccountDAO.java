package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    
       
    public Account registerAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT into Account(username, password) VALUES(?,?)";
            PreparedStatement ps = connection.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2,account.getPassword());
            ps.executeUpdate();
            ResultSet pkResultSet = ps.getGeneratedKeys();
            if(pkResultSet.next()){
                int account_id = (int) pkResultSet.getInt(1);
                return new Account(account_id, account.getUsername(),account.getPassword());
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account login(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Account WHERE username = ? and password = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2,account.getPassword());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Account login_account = new Account(rs.getInt("account_id"),rs.getString("username"), rs.getString("password"));
                return login_account;
                
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account findByusername(String  username){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Account WHERE username = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Account existingAccount = new Account(rs.getInt("account_id"),rs.getString("username"),rs.getString("password"));
                return existingAccount;
                
            }

            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
