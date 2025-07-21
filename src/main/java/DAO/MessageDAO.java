package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageDAO {
    
    /**
     * Add a message to the database.
     * @param m Message Object
     * @return Message Object containing message_id or null.
     */
    public Message addMessage(Message m) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)" ;

            // Statement.RETURN_GENERATED_KEYS retrieved from Flight Tracker Mini Project
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, m.getPosted_by());
            preparedStatement.setString(2, m.getMessage_text());
            preparedStatement.setLong(3, m.getTime_posted_epoch());


            preparedStatement.executeUpdate();

            // Fetching generated keys. Retrieved from Flight Tracker Mini Project
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                int generatedId = generatedKeys.getInt(1);
                return new Message(generatedId, m.getPosted_by(), m.getMessage_text(), m.getTime_posted_epoch());
            } else {
                throw new SQLException("Creating message failed, no ID obtained.");
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Get all messages from the database.
     * @return List of all the messages.
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message ms = new Message(rs.getInt("message_id"), 
                                        rs.getInt("posted_by"),
                                        rs.getString("message_text"),
                                        rs.getLong("time_posted_epoch"));
                messages.add(ms);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * Returns a message from the database.
     * If message doesn't exist, return null.
     * @param message_id Used to search up the message.
     * @return Message or null.
     */
    public Message getMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message ms = new Message(rs.getInt("message_id"), 
                                        rs.getInt("posted_by"),
                                        rs.getString("message_text"),
                                        rs.getLong("time_posted_epoch"));
                return ms;
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Delete a message from the database, based on the message_id.
     * If there is no matching message, return null.
     * @param message_id Used to search up the message.
     * @return Deleted message or null.
     */
    public Message deleteMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        Message deletedMessage = getMessageById(message_id);
        
        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            
            preparedStatement.executeUpdate();

            return deletedMessage;
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Update a message_text in the database based on the message_id;.
     * If there is no matching message, return null.
     * @param m Message to update.
     * @return Fully updated message or null.
     */
    public Message updateMessageById(Message m) {
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, m.getMessage_text());
            preparedStatement.setInt(2, m.getMessage_id());
            
            preparedStatement.executeUpdate();

            Message updatedMessage = getMessageById(m.getMessage_id());

            return updatedMessage;
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Get all messages from the database based on account_id.
     * @return List of all the messages.
     */
    public List<Message> getAllMessagesFromAccountId(int accId) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, accId);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message ms = new Message(rs.getInt("message_id"), 
                                        rs.getInt("posted_by"),
                                        rs.getString("message_text"),
                                        rs.getLong("time_posted_epoch"));
                messages.add(ms);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
