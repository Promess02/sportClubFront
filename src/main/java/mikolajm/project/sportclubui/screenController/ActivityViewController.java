package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import mikolaj.project.backendapp.model.Activity;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.LoginManager;
import mikolajm.project.sportclubui.screenController.activityCalendar.ActivitySignupController;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class ActivityViewController {
    @FXML private ImageView imageView;
    @FXML private Button activityBtn;
    @FXML private Label activityName;
    @FXML private AnchorPane mainView;
    private ConfigurableApplicationContext context;
    private Activity activity;

    public ActivityViewController() {
    }

    @FXML
    public void initialize(Activity activity){

    }

    public void setActivity(Activity activity){
        activityName.setText(activity.getName());
        Image image = new Image("/images/newsPostSample.jpg");
        imageView.setImage(image);
        this.activity = activity;
        initBtn();
    }

    private void initBtn(){
        activityBtn.setOnAction(e ->
        {
            try {
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/ActivitySignUp.fxml"));
                loader.setControllerFactory(context::getBean);
                // Load the root node from the FXML file
                Parent root = loader.load();
                // Create a new Scene with the root node
                ActivitySignupController activitySignUpController = loader.getController();
                activitySignUpController.setActivity(activity);
                activitySignUpController.disableSignUp();
                activitySignUpController.disableSpecificActivity();
                Scene scene = new Scene(root);
                // Set the Scene to the primaryStage or a new Stage
                Stage primaryStage = new Stage(); // You might use your existing primaryStage here
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch (IOException ex){
                Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
