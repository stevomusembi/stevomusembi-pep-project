package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;


public class MessageService {
    
    public MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();

    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    
    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message addMessage(Message message){
        if(message.getMessage_text() == null || message.getMessage_text().length() == 0){
            return null;
           
        }

        if(message.getMessage_text().length() > 255){
            return null;
            
        }

        if(message.getPosted_by()== 0){
            return null;
           
        }

        return messageDAO.addMessage(message);
    }

    public Message getMessageById(int message_id){
        return messageDAO.getMessageById(message_id);
    }

    public List<Message> getAllMessagesByAccountId(int account_id){
        return messageDAO.getAllMessagesByAccountId(account_id);
    }

    public Message updateMessage(int id, Message message){
        if(message.getMessage_text() == null || message.getMessage_text().length() == 0){
           return null;
        }

        if(message.getMessage_text().length() > 255){
           return null;
        }

        return messageDAO.updateMessage(id, message);
    }

    public void deleteMessageById(int messsage_id){
         messageDAO.deleteMessageById(messsage_id);
    }

}
