package Controller;

import Service.AccountService;
import Service.MessageService;
import DAO.AccountDAO;
import DAO.MessageDAO;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;



/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */


    private AccountService accountService;
    private MessageService messageService;
    private ObjectMapper objectMapper;

    public SocialMediaController() {
        this.accountService = new AccountService(new AccountDAO());
        this.messageService = new MessageService(new MessageDAO());
        this.objectMapper = new ObjectMapper();
    }

 
     public SocialMediaController(AccountService accountService, MessageService messageService) {
         this.accountService = accountService;
         this.messageService = messageService;
     }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("/register", this::registerUser);
        app.post("/login", this::loginUser);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/:message_id", this::getMessageById);
        app.delete("/messages/:message_id", this::deleteMessageById);
        app.patch("/messages/:message_id", this::updateMessage);
        app.get("/accounts/:account_id/messages", this::getMessagesByUser);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerUser(Context ctx) throws Exception {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        Account registeredAccount = accountService.registerAccount(username, password); 


        //Account account = ctx.bodyAsClass(Account.class);
        //Account registeredAccount = accountService.registerAccount(account.getUsername(), account.getPassword());
        if (registeredAccount != null) {
            String accountJson = objectMapper.writeValueAsString(registeredAccount);
            ctx.result(accountJson);
        } else {
            ctx.status(400).result("{}"); // Set status to 400 and ensure the response body is an empty JSON object
        }
    }

    private void loginUser(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());
        if (loggedInAccount != null) {
            ctx.json(loggedInAccount);
        } else {
            ctx.status(401);
        }
    }

    private void createMessage(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
        Message createdMessage = messageService.createMessage(message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
        if (createdMessage != null) {
            ctx.json(createdMessage);
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessages(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            ctx.json(message);
        } else {
            ctx.status(404);
        }
    }

    private void deleteMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if (message != null && messageService.deleteMessageById(messageId)) {
            ctx.json(message);
        } else {
            ctx.status(404);
        }
    }

    private void updateMessage(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        String newMessageText = ctx.bodyAsClass(Message.class).getMessage_text();
        Message updatedMessage = messageService.updateMessage(messageId, newMessageText);
        if (updatedMessage != null) {
            ctx.json(updatedMessage);
        } else {
            ctx.status(400);
        }
    }

    private void getMessagesByUser(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByUserId(userId);
        ctx.json(messages);
    }
  
}