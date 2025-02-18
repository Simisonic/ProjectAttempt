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

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);
        return app;
    }

    private void registerHandler(Context context) {
        try {
            Account account = context.bodyAsClass(Account.class);
            Account registeredAccount = accountService.register(account);
            context.json(registeredAccount);
            context.status(200); // OK
        } catch (IllegalArgumentException e) {
            context.status(400).result(e.getMessage()); // BAD_REQUEST
        }
    }

    private void loginHandler(Context context) {
        try {
            Account account = context.bodyAsClass(Account.class);
            Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());
            context.json(loggedInAccount);
            context.status(200); // OK
        } catch (IllegalArgumentException e) {
            context.status(401).result(e.getMessage()); // UNAUTHORIZED
        }
    }

    private void createMessageHandler(Context context) {
        try {
            Message message = context.bodyAsClass(Message.class);
            Message createdMessage = messageService.createMessage(message);
            context.json(createdMessage);
            context.status(200); // OK
        } catch (IllegalArgumentException e) {
            context.status(400).result(e.getMessage()); // BAD_REQUEST
        }
    }

    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
        context.status(200); // OK
    }

    private void getMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessage(messageId);
        if (message == null) {
            context.status(200); // OK
        } else {
            context.json(message);
            context.status(200); // OK
        }
    }

    private void deleteMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.deleteMessage(messageId);
        if (message == null) {
            context.status(200); // OK
        } else {
            context.json(message);
            context.status(200); // OK
        }
    }

    private void updateMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        String messageText = context.body();
        try {
            Message updatedMessage = messageService.updateMessage(messageId, messageText);
            if (updatedMessage == null) {
                context.status(400); // BAD_REQUEST
            } else {
                context.json(updatedMessage);
                context.status(200); // OK
            }
        } catch (IllegalArgumentException e) {
            context.status(400).result(e.getMessage()); // BAD_REQUEST
        }
    }

    private void getMessagesByUserHandler(Context context) {
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByUser(accountId);
        context.json(messages);
        context.status(200); // OK
    }
}
