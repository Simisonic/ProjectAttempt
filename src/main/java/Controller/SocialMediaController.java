package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;

public class SocialMediaController {
    private AccountService accountService = new AccountService();
    private MessageService messageService = new MessageService();

    // Starts the Javalin API and defines the endpoints
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler); // Endpoint for user registration
        app.post("/login", this::loginHandler); // Endpoint for user login
        app.post("/messages", this::createMessageHandler); // Endpoint for creating a new message
        app.get("/messages", this::getAllMessagesHandler); // Endpoint for retrieving all messages
        app.get("/messages/{message_id}", this::getMessageHandler); // Endpoint for retrieving a message by ID
        app.delete("/messages/{message_id}", this::deleteMessageHandler); // Endpoint for deleting a message by ID
        app.patch("/messages/{message_id}", this::updateMessageHandler); // Endpoint for updating a message by ID
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler); // Endpoint for retrieving messages by user ID
        return app;
    }

    // Handles user registration requests
    private void registerHandler(Context context) {
        try {
            Account account = context.bodyAsClass(Account.class); // Deserialize request body to Account object
            Account registeredAccount = accountService.register(account); // Register the account
            context.json(registeredAccount); // Send the registered account as JSON response
            context.status(200); // HTTP status 200: OK
        } catch (IllegalArgumentException e) {
            context.status(400).result(e.getMessage()); // HTTP status 400: Bad Request with error message
        }
    }

    // Handles user login requests
    private void loginHandler(Context context) {
        try {
            Account account = context.bodyAsClass(Account.class); // Deserialize request body to Account object
            Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword()); // Login the account
            context.json(loggedInAccount); // Send the logged-in account as JSON response
            context.status(200); // HTTP status 200: OK
        } catch (IllegalArgumentException e) {
            context.status(401).result(e.getMessage()); // HTTP status 401: Unauthorized with error message
        }
    }

    // Handles requests for creating a new message
    private void createMessageHandler(Context context) {
        try {
            Message message = context.bodyAsClass(Message.class); // Deserialize request body to Message object
            Message createdMessage = messageService.createMessage(message); // Create the message
            context.json(createdMessage); // Send the created message as JSON response
            context.status(200); // HTTP status 200: OK
        } catch (IllegalArgumentException e) {
            context.status(400).result(e.getMessage()); // HTTP status 400: Bad Request with error message
        }
    }

    // Handles requests for retrieving all messages
    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages(); // Retrieve all messages
        context.json(messages); // Send the messages as JSON response
        context.status(200); // HTTP status 200: OK
    }

    // Handles requests for retrieving a message by ID
    private void getMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id")); // Get message ID from path parameter
        Message message = messageService.getMessage(messageId); // Retrieve the message by ID
        if (message == null) {
            context.status(200); // HTTP status 200: OK (empty response)
        } else {
            context.json(message); // Send the message as JSON response
            context.status(200); // HTTP status 200: OK
        }
    }

    // Handles requests for deleting a message by ID
    private void deleteMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id")); // Get message ID from path parameter
        Message message = messageService.deleteMessage(messageId); // Delete the message by ID
        if (message == null) {
            context.status(200); // HTTP status 200: OK (empty response)
        } else {
            context.json(message); // Send the deleted message as JSON response
            context.status(200); // HTTP status 200: OK
        }
    }

    // Handles requests for updating a message by ID
    private void updateMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id")); // Get message ID from path parameter
        String messageText = context.body(); // Get new message text from request body
        try {
            Message updatedMessage = messageService.updateMessage(messageId, messageText); // Update the message
            if (updatedMessage == null) {
                context.status(400); // HTTP status 400: Bad Request
            } else {
                context.json(updatedMessage); // Send the updated message as JSON response
                context.status(200); // HTTP status 200: OK
            }
        } catch (IllegalArgumentException e) {
            context.status(400).result(e.getMessage()); // HTTP status 400: Bad Request with error message
        }
    }

    // Handles requests for retrieving messages by user ID
    private void getMessagesByUserHandler(Context context) {
        int accountId = Integer.parseInt(context.pathParam("account_id")); // Get user ID from path parameter
        List<Message> messages = messageService.getMessagesByUser(accountId); // Retrieve messages by user ID
        context.json(messages); // Send the messages as JSON response
        context.status(200); // HTTP status 200: OK
    }
}
