package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.LoginManager;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class EnterScreenController {
    @FXML private ImageView logoImage;
    @FXML private Button registerUserBtn;
    @FXML private Button loginMemberBtn;
    @FXML private Button loginTrainerBtn;
    private ConfigurableApplicationContext context;

    public EnterScreenController() {
    }

    public void initialize(){
        Image image = new Image("/images/clubLogo.jpg");
        logoImage.setImage(image);
        initLoginMemberBtn();
        initRegisterBtn();
    }
    private void initLoginMemberBtn(){
        loginMemberBtn.setOnAction(e->{
            Scene scene = new Scene(new StackPane());
            context = ClubApplication.getApplicationContext();
            LoginManager loginManager = new LoginManager(scene, context);
            loginManager.showLoginScreen();
            Stage primaryStage = new Stage();
            primaryStage.setScene(scene);
            primaryStage.setTitle("SportClub application");
            primaryStage.show();
        });
    }

    private void initRegisterBtn(){
        registerUserBtn.setOnAction(e ->{
            try{
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/registerView.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                RegisterViewController registerViewController = loader.getController();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("SportClub registration");
                stage.show();
            }catch (IOException exception){
                throw new RuntimeException("failed loading registration view");
            }

        });
    }
}
