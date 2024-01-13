package mikolajm.project.sportclubui.screenController.UtilityScreens;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import mikolaj.project.backendapp.controller.UserController;
import mikolaj.project.backendapp.model.User;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.CurrentSessionUser;
import mikolajm.project.sportclubui.LoginManager;
import mikolajm.project.sportclubui.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controls the login screen
 */

@Component
@Slf4j
public class LoginController {
    @FXML
    private TextField user;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;
    private final UserController userController;
    private final CurrentSessionUser currentSessionUser;
    private ConfigurableApplicationContext context = ClubApplication.getApplicationContext();

    public void initialize() {
        loginButton.setOnAction(event -> {
            String sessionID = authorize();
            if (sessionID != null) {
                String session = authorize();
                if (session != null) {
                    log.info("session id:" + session);
                    if(currentSessionUser.getUser().getEmail().equals("admin")) openAdminView();
                    else{
                        if(currentSessionUser.getTrainer()!=null) openTrainerView();
                        else openMainScreenView();
                    }
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.close();
                }
            }else{
                Utils util = new Utils();
                util.showErrorMessage("user not found");
                user.setText("");
                password.setText("");
            }
        });
    }

    private void openTrainerView(){
        try {
            context = ClubApplication.getApplicationContext();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/trainer/TrainerMainScreen.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            closeStage();
        }catch (IOException exception){
            throw new RuntimeException("unable to load trainer view");
        }
    }

    private void openAdminView(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/adminView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            closeStage();
        }catch (IOException exception){
            throw new RuntimeException("unable to load admin view");
        }

    }

    private void openMainScreenView(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/user/mainScreen.fxml"));
            context = ClubApplication.getApplicationContext();
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = new Stage(); // You might use your existing primaryStage here
            primaryStage.setScene(scene);
            primaryStage.show();
            closeStage();
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Autowired
    public LoginController(final UserController userController, final CurrentSessionUser currentSessionUser) {
        this.userController = userController;
        this.currentSessionUser = currentSessionUser;
    }

    private String authorize() {
        User createdUser = new User(user.getText(), password.getText());
        Optional<User> userDb = userController.loginUser(createdUser).
                getStatusCode().isSameCodeAs(HttpStatus.OK)
                ? Optional.of(createdUser)
                : Optional.empty();
        return userDb.isPresent()
                ? generateSessionID(createdUser)
                : null;
    }

    private void closeStage(){
        Stage curStage = (Stage) user.getScene().getWindow();
        curStage.close();
    }

    private static int sessionID = 0;

    private String generateSessionID(User user) {
        sessionID++;
        currentSessionUser.setUser(user);
        return "xyzzy - session " + sessionID;
    }
}