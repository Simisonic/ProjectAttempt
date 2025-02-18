package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {

    private MessageDAO messageDAO = new MessageDAO();
    private AccountService accountService = new AccountService();

    public Message createMessage(Message message) {
        if (message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Invalid message text");
        }
        if (!accountService.userExists(message.getPosted_by())) {
            throw new IllegalArgumentException("Invalid user");
        }
        return messageDAO.save(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.findAll();
    }

    public Message getMessage(int messageId) {
        return messageDAO.findById(messageId);
    }

    public Message deleteMessage(int messageId) {
        Message message = messageDAO.findById(messageId);
        if (message != null) {
            messageDAO.delete(messageId);
        }
        return message;
    }

    public Message updateMessage(int messageId, String messageText) {
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

    public List<Message> getMessagesByUser(int accountId) {
        return messageDAO.findAllByPostedBy(accountId);
    }
}
