package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message createMessage(int postedBy, String messageText, long timePostedEpoch) {
        if (messageText == null || messageText.isBlank() || messageText.length() > 255) {
            return null;
        }
        return messageDAO.createMessage(new Message(postedBy, messageText, timePostedEpoch));
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public boolean deleteMessageById(int messageId) {
        return messageDAO.deleteMessageById(messageId);
    }

    public Message updateMessage(int messageId, String newMessageText) {
        if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
            return null;
        }
        return messageDAO.updateMessage(messageId, newMessageText);
    }

    public List<Message> getMessagesByUserId(int userId) {
        return messageDAO.getMessagesByUserId(userId);
    }
}
