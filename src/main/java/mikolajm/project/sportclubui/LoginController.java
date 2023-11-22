package mikolajm.project.sportclubui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import mikolaj.project.backendapp.controller.UserController;
import mikolaj.project.backendapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Controls the login screen
 */

@Component
@Slf4j
public class LoginController {
    @FXML
    private TextField user;
    @FXML
    private TextField password;
    @FXML
    private Button loginButton;
    private final UserController userController;

    public void initialize() {
        loginButton.setOnAction(event -> {
            String sessionID = authorize();
            if (sessionID != null) {
                String session = authorize();
                if (session != null)
                    log.info("session id:" + session);
                else log.info("authentication failed");
            }
        });
    }

    @Autowired
    public LoginController(final UserController userController) {
        this.userController = userController;
    }

    /**
     * Check authorization credentials.
     * <p>
     * If accepted, return a sessionID for the authorized session
     * otherwise, return null.
     */
    private String authorize() {
        return userController.loginUser(new User(user.getText(), password.getText())).getStatusCode().isSameCodeAs(HttpStatus.OK)
                ? generateSessionID()
                : null;
    }

    private static int sessionID = 0;

    private String generateSessionID() {
        sessionID++;
        return "xyzzy - session " + sessionID;
    }
}