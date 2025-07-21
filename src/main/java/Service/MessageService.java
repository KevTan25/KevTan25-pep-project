package Service;

import DAO.MessageDAO;

import java.util.List;

import DAO.AccountDAO;
import Model.Message;


public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accDAO;

    /**
     * Creation of messageDAO which will create and use DAO's.
     */
    public MessageService(){
        messageDAO = new MessageDAO();
        accDAO = new AccountDAO(); // Used to access accounts
    }

    /**
     * Helps create message and add it to database.
     * Message successful, message_text is not blank, message_text < 255 characters,
     * posted_by refers to an existing user.
     * @param m Message object not containing message_id.
     * @return A message object containing message_id.
     */
    public Message createMessage(Message m) {
        if (m.getMessage_text().equals("")) {
            return null;
        } else if (!(m.message_text.length() < 255)) {
            return null;
        }

        if (accDAO.getAccountByID(m.getPosted_by()) == null) {
            return null;
        }

        return messageDAO.addMessage(m);
    }

    /**
     * Gets all messages.
     * @return All messages in a List.
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * Get one message by message_id.
     * @return One message.
     */
    public Message getOneMessageById(int m) {
        return messageDAO.getMessageById(m);
    }

    /**
     * Delete one message by message_id.
     * @return The deleted message.
     */
    public Message deleteOneMessageById(int m) {
        return messageDAO.deleteMessageById(m);
    }

    /**
     * Update a message based on the message_id.
     * Message_text cannot be empty and is not over 255 characters.
     * Returns null if message_id doesn't exist.
     * @param m Updated message text and message id.
     * @return Full updated message or null.
     */
    public Message updateMessage(Message m) {
        if (m.getMessage_text().length() > 255 || m.getMessage_text() == "") {
            return null;
        }

        return messageDAO.updateMessageById(m);
    }

    /**
     * Gets all messages based on account id.
     * @return All messages in a List or an empty list.
     */
    public List<Message> getAllMessagesFromAccount(int accId) {
        return messageDAO.getAllMessagesFromAccountId(accId);
    }
}