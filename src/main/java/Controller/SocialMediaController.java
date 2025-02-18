package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

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

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this:updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserHandler);
        return app;
    }

    private void registerHandler(Context context){
        try{
            Account account = context.bodyAsClass(Account.class);
            Account registeredAccount = accountService.register(account);
            context.json(registeredAccount);
            context.status(HttpCode.OK);
        } catch (IllegalArgumentException e){
            context.status(HttpCode.BAD_REQUEST).result(e.getMessage());
        }
    }

    private void loginHandler(Context context){
        try{
            Account account = context.bodyAsClass(Account.class);
            Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());
            context.json(loggedInAccount);
            context.status(HttpCode.OK);
        } catch (IllegalArgumentException e){
            context.status(HttpCode.UNAUTHORIZED).result(e.getMessage());
        }
    }

    private void createMessageHandler(Context context) {
        try{
            Message message = context.bodyAsClass(Message.class);
            Message createdMessage = messageService.createMessage(message);
            context.json(createdMessage);
            context.status(HttpCode.OK);
        } catch (IllegalArgumentException e){
            context.status(HttpCode.BAD_REQUEST).result(e.getMessage());
        }
    }
    private void getAllMessagesHandler(Context context){
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
        context.status(HttpCode.OK);
    }

    private void getMessageHandler(Context context){
        int messageId = context.pathParam("message_id", Integer.class).get();
        Message message = messageService.getMessage(messageId);
        if (message == null){
            context.status(HttpCode.OK);
        } else{
            context.json(message);
            context.status(HttpCode.OK);
        }
    }

    private void deleteMessageHandler(Context context){
        int messageId = context.pathParam("message_id", Integer.class).get();
        Message message = messageService.deleteMessage(messageId);
        if (message == null){
            context.status(HttpCode.OK);
        } else {
            context.json(message);
            context.status(HttpCode.OK);
        }
    }

    private void updateMessageHandler(Context context){
        int messageId = context.pathParam("message_id", Integer.class).get();
        String messageText = context.body();
        try{
            Message updatedMessage = messageService.updateMessage(messageId, messageText);
            if (updatedMessage == null){
                context.status(HttpCode.BAD_REQUEST);
            } else{
                context.json(updatedMessage);
                context.status(HttpCode.OK);
            }
        } catch (IllegalArgumentException e){
            context.status(HttpCode.BAD_REQUEST).result(e.getMessage());
        }
    }
    private void getMessagesByUserHandler(Context context){
        int accountId = context.pathParam("account_id", Integer.class).get();
        List<Message> messages = messageService.getMessagesByUser(accountId);
        context.json(messages);
        context.status(HttpCode.OK);
    }
}