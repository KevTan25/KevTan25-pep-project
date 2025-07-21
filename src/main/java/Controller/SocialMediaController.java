package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

// Import Models 
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    // Servcie Objects
    AccountService accService;
    MessageService msService;

    /**
     * Constructor for social media controller.
     * Will initalize service objects.
     */
    public SocialMediaController(){
        this.accService = new AccountService();
        this.msService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        // Registration
        app.post("/register", this::registerHandler);
        // Login
        app.post("/login", this::loginHandler);
        // Create new message
        app.post("/messages", this::createMessagesHandler);
        // Get all messages
        app.get("/messages", this::getAllMessagesHandler);
        // Get one message by message ID
        app.get("/messages/{message_id}", this::getOneMessageHandler);
        // Delete one message by message ID
        app.delete("/messages/{message_id}", this::deleteOneMessageHandler);
        // Update one message by message ID
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        // Get all message by account ID
        app.get("/accounts/{account_id}/messages", this::getAllMessagesFromAccountHandler);

        // app.get("example-endpoint", this::exampleHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    // private void exampleHandler(Context context) {
    //     context.json("sample text");
    // }

    /**
     * Registration Handler
     * Sends an account object to AccountService.
     * Successful response returns account JSON and status is 200, Not successful returns status 400.
     * @param ctx Handles HTTP request and the response
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void registerHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account acc = mapper.readValue(ctx.body(), Account.class);
        Account addedAcc = accService.addAccount(acc);
        
        if (addedAcc != null) {
            ctx.json(mapper.writeValueAsString(addedAcc));
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }

    /**
     * Login Handler.
     * Request Body contains only username and password, no account_id.
     * Response Body should contain account_id, username, and password.
     * Successful response: 200, Unsuccessful Response: 401.
     * @param ctx Handles HTTP request and the response
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account acc = mapper.readValue(ctx.body(), Account.class);
        Account loggedAcc = accService.login(acc);

        if (loggedAcc != null) {
            ctx.json(mapper.writeValueAsString(loggedAcc));
            ctx.status(200);
        } else {
            ctx.status(401);
        }
    }

    /**
     * Message Creation Handler.
     * Request body contains message but no message_id.
     * Response is message JSON containing message_id. 
     * Successful response: 200, Unsuccessful response: 400 (Client Error).
     * @param ctx
     * @throws JsonProcessingException
     */
    private void createMessagesHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message m = mapper.readValue(ctx.body(), Message.class);
        Message createdMs = msService.createMessage(m);

        if (createdMs != null) {
            ctx.json(mapper.writeValueAsString(createdMs));
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }

    /**
     * Get All Messages Handler.
     * Response is a list of all the messages in the database.
     * All responses: 200.
     * @param ctx
     * @throws JsonProcessingException
     */
    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> createdMs = msService.getAllMessages();

        ctx.json(mapper.writeValueAsString(createdMs));
        ctx.status(200);
    }

    /**
     * Get one message by message_id.
     * Request has message_id in the path.
     * Response is one message from the database. Empty message if there is no such message.
     * All responses: 200.
     * @param ctx
     * @throws JsonProcessingException
     */
    private void getOneMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message createdMs = msService.getOneMessageById(messageId);

        // Only return a JSON message if not null.
        if (createdMs != null) {
            ctx.json(mapper.writeValueAsString(createdMs));
        }

        ctx.status(200);
    }

    /**
     * Delete one message by message_id.
     * Request has message_id in the path.
     * If deleted the response body contains the deleted message. Empty JSON if there is no matched message.
     * Response is 200.
     * @param ctx
     * @throws JsonProcessingException
     */
    private void deleteOneMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMs = msService.deleteOneMessageById(messageId);

        if (deletedMs != null) {
            ctx.json(mapper.writeValueAsString(deletedMs));
        }

        ctx.status(200);
    }

    /**
     * Updates a message text based on message id.
     * Request body contains the new message_text value and message_id.
     * Response body contains the full updated message.
     * Successful response: 200, Unsuccessful response: 400. 
     * @param ctx
     * @throws JsonProcessingException
     */
    private void patchMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message m = mapper.readValue(ctx.body(), Message.class);

        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        m.setMessage_id(messageId);

        Message updatedMs = msService.updateMessage(m);

        if (updatedMs != null) {
            ctx.json(mapper.writeValueAsString(updatedMs));
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }

    /**
     * Gets all messages from a specific account.
     * Request body is only the account_id.
     * Reponse body contains a JSON representation of a list containing all messages. Empty list if no messages.
     * All responses: 200.
     * @param ctx
     * @throws JsonProcessingException
     */
    private void getAllMessagesFromAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int accId = Integer.parseInt(ctx.pathParam("account_id"));

        List<Message> allMessages = msService.getAllMessagesFromAccount(accId);
        ctx.json(mapper.writeValueAsString(allMessages));
        ctx.status(200);
    }
}