import Controller.SocialMediaController;
import io.javalin.Javalin;

/**
 * This class is provided with a main method to allow you to manually run and test your application. This class will not
 * affect your program in any way and you may write whatever code you like here.
 */
public class Main {
    public static void main(String[] args) {
        // Create an instance of the SocialMediaController
        SocialMediaController controller = new SocialMediaController();
        // Start the Javalin app using the SocialMediaController
        Javalin app = controller.startAPI();
        // Start the Javalin app on port 8080
        app.start(8080);
        System.out.println("Server has started on port 8080.");
    }
}
