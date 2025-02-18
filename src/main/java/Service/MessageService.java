package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService {

    private MessageDAO messageDAO = new MessageDAO();
    private AccountService accountService = new AccountService();

    // Creates a new message
    public Message createMessage(Message message) {
        // Validate message text is not blank and not over 255 characters
        if (message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Invalid message text");
        }

        // Validate posted_by refers to an existing user
        if (!accountService.userExists(message.getPosted_by())) {
            throw new IllegalArgumentException("Invalid user");
        }

        // Save the message to the database
        return messageDAO.save(message);
    }

    // Retrieves all messages
    public List<Message> getAllMessages() {
        return messageDAO.findAll();
    }

    // Retrieves a message by ID
    public Message getMessage(int messageId) {
        return messageDAO.findById(messageId);
    }

    // Deletes a message by ID
    public Message deleteMessage(int messageId) {
        Message message = messageDAO.findById(messageId);
        if (message != null) {
            messageDAO.delete(messageId);
        }
        return message;
    }

    // Updates a message by ID
    public Message updateMessage(int messageId, String messageText) {
        // Validate message text is not blank and not over 255 characters
        if (messageText.isBlank() || messageText.length() > 255) {
            throw new IllegalArgumentException("Invalid message text");
        }

        Message message = messageDAO.findById(messageId);
        if (message != null) {
            messageDAO.update(messageId, messageText);
            message.setMessage_text(messageText);
        }
        return message;
    }

    // Retrieves messages by user ID
    public List<Message> getMessagesByUser(int accountId) {
        return messageDAO.findAllByPostedBy(accountId);
    }
}
