package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    // Saves a new message to the database
    public Message save(Message message) {
        String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                message.setMessage_id(rs.getInt(1)); // Set the generated message ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating message");
        }

        return message;
    }

    // Retrieves all messages from the database
    public List<Message> findAll() {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    // Retrieves a message by ID
    public Message findById(int messageId) {
        String sql = "SELECT * FROM Message WHERE message_id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, messageId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Deletes a message by ID
    public void delete(int messageId) {
        String sql = "DELETE FROM Message WHERE message_id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, messageId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting message");
        }
    }

    // Updates a message by ID
    public void update(int messageId, String messageText) {
        String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, messageText);
            ps.setInt(2, messageId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating message");
        }
    }

    // Retrieves messages by user ID
    public List<Message> findAllByPostedBy(int accountId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message WHERE posted_by = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
