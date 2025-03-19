package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private MessageService messageService;
    private AccountService accountService;

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

     public SocialMediaController(){
        this.messageService = new MessageService();
        this.accountService = new AccountService();
     }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerAccount);
        app.post("/login", this::login);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountId);
        app.post("/messages",this::postMessage);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.delete("messages/{message_id}", this::deleteMessageById);


        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    
    private void registerAccount(Context context)throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        Account account = objectMapper.readValue(context.body(),Account.class);
        Account createdAccount = accountService.registerAccount(account);
        if(createdAccount !=null){
            context.json(objectMapper.writeValueAsString(createdAccount));
        } else {
            context.status(400);
        }
    }

    private void login(Context context)throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account loginAccount = accountService.login(account);
        if(loginAccount !=null){
            context.json(mapper.writeValueAsString(loginAccount));

        }else {
            context.status(401);
        }

    }

    private void getAllMessages(Context context){
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    private void getMessageById(Context context){
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId); 
            if(message != null){
                 context.json(message);
            } else {
                context.status(200).result("Message not found");
                context.result(""); 
                
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }

    private void getMessagesByAccountId(Context context){
        try {
            int accountId = Integer.parseInt(context.pathParam("account_id"));
            List<Message> messages = messageService.getAllMessagesByAccountId(accountId);
            if(messages!= null){
                context.json(messages);
            } else {
                context.status(200).result("No messagens found for the account");
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void postMessage(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        try {
            Message message = mapper.readValue(context.body(), Message.class);
            Message addedMessage = messageService.addMessage(message);
            if(addedMessage != null){
                context.json(mapper.writeValueAsString(addedMessage));
            }else {
                context.status(400);
                context.result("");
            }

        } catch (Exception e) {
           System.out.println(e.getMessage());
    }

    }

    private void updateMessage(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(),Message.class);
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message_id, message);

        if(updatedMessage == null){
            context.status(400);
            context.result("");

        } else{
            context.json(mapper.writeValueAsString(updatedMessage));
        }

    }

    private void deleteMessageById(Context ctx){
        ObjectMapper mapper = new ObjectMapper();

        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId);
            if(message != null){
                messageService.deleteMessageById(messageId);
                ctx.json(mapper.writeValueAsString(message));
            } else {
                ctx.status(200).result("Message not found");
                ctx.result("");
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}