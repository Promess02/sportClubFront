package mikolajm.project.sportclubui.screenController.generalScreens;

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
    public void initialize(){

    }

    public void setActivity(Activity activity){
        activityName.setText(activity.getName());
        Image image;
        if(activity.getImageUrl()==null) image = new Image("/images/newsPostSample.jpg");
        else image = new Image(activity.getImageUrl());
        imageView.setImage(image);
        this.activity = activity;
        initBtn();
    }

    private void initBtn(){
        activityBtn.setOnAction(e ->
        {
            try {
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/user/ActivitySignUp.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                ActivitySignupController activitySignUpController = loader.getController();
                activitySignUpController.setActivity(activity);
                activitySignUpController.disableSignUp();
                activitySignUpController.disableSpecificActivity();
                Scene scene = new Scene(root);
                Stage primaryStage = new Stage();
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch (IOException ex){
                Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
