package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Message;

import Util.ConnectionUtil;

public class MessageDAO {
 
   
    public List<Message> getAllMessages(){ 
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        

        try {
            String sql = "SELECT * FROM Message";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"),rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
            

        } catch (SQLException e) {
            
            System.err.println(e.getMessage());
        }
        return messages;
    }
    
    public Message addMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES(?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ps.executeUpdate();
            ResultSet pKResultSet = ps.getGeneratedKeys();
            if(pKResultSet.next()){
                int generatedMessage_id = (int) pKResultSet.getInt(1);
                return new Message(generatedMessage_id,message.getPosted_by(), message.getMessage_text(),message.getTime_posted_epoch());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    public Message getMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),rs.getInt("posted_by"),rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
                return message;
            }

            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessagesByAccountId(int id){
        List<Message> messages = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM Message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"),rs.getInt("posted_by"),rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message updateMessage(int id, Message message){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String checkMsgSql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement checkPs = connection.prepareStatement(checkMsgSql);
            checkPs.setInt(1, id);
            ResultSet rs = checkPs.executeQuery();
            if(!rs.next()){
                return null;
            }

            String sql = "UPDATE Message set message_text = ? WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, message.getMessage_text());
            ps.setInt(2, id);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected >0){
                return new Message(id,rs.getInt("posted_by"),message.getMessage_text(),rs.getLong("time_posted_epoch"));
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean deleteMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        Message messageToDelete = getMessageById(id);
        if(messageToDelete == null){
            return false;
        }
        try {
            String sql = "DELETE FROM Message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

            } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

}

