package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    // Method to create a new message in the database
    public Message createMessage(Message message) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, message.getPosted_by());
            statement.setString(2, message.getMessage_text());
            statement.setLong(3, message.getTime_posted_epoch());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    message.setMessage_id(generatedKeys.getInt(1));
                    return message;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to retrieve all messages from the database
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM message";
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                messages.add(new Message(
                        result.getInt("message_id"),
                        result.getInt("posted_by"),
                        result.getString("message_text"),
                        result.getLong("time_posted_epoch")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    // Method to retrieve a message by its ID
    public Message getMessageById(int messageId) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, messageId);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new Message(
                        result.getInt("message_id"),
                        result.getInt("posted_by"),
                        result.getString("message_text"),
                        result.getLong("time_posted_epoch")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to delete a message by its ID
    public boolean deleteMessageById(int messageId) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, messageId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to update a message's text by its ID
    public Message updateMessage(int messageId, String newText) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, newText);
            statement.setInt(2, messageId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 1) {
                return getMessageById(messageId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to retrieve all messages posted by a specific user
    public List<Message> getMessagesByUserId(int userId) {
        List<Message> messages = new ArrayList<>();
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                messages.add(new Message(
                        result.getInt("message_id"),
                        result.getInt("posted_by"),
                        result.getString("message_text"),
                        result.getLong("time_posted_epoch")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
