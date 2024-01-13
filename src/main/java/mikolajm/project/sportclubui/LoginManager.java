package mikolajm.project.sportclubui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import lombok.Getter;
import mikolajm.project.sportclubui.screenController.UtilityScreens.MainViewController;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages control flow for logins
 */


public class LoginManager {
    @Getter
    private final Scene scene;
    private final ConfigurableApplicationContext context;

    public LoginManager(Scene scene, ConfigurableApplicationContext applicationContext) {
        this.context = applicationContext;
        this.scene = scene;
    }

    /**
     * Callback method invoked to notify that a user has been authenticated.
     * Will show the main application screen.
     */
    public void authenticated(String sessionID) {
        showMainView(sessionID);
    }

    /**
     * Callback method invoked to notify that a user has logged out of the main application.
     * Will show the login application screen.
     */
    public void logout() {
        showLoginScreen();
    }

    public void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/loginRegister/login.fxml"));
            loader.setControllerFactory(context::getBean);
            scene.setRoot(loader.load());
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showMainView(String sessionID) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/screens/popups/mainview.fxml")
            );
            scene.setRoot(loader.load());
            MainViewController controller =
                    loader.getController();
            controller.initSessionID(this, sessionID);
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}