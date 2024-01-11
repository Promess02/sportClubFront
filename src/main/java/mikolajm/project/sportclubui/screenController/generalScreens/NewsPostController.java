package mikolajm.project.sportclubui.screenController.generalScreens;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Getter;
import mikolaj.project.backendapp.model.NewsPost;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.screenController.activityCalendar.ActivitySignupController;
import mikolajm.project.sportclubui.screenController.generalScreens.LocationViewController;
import mikolajm.project.sportclubui.screenController.generalScreens.MembershipTypeController;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@Getter
public class NewsPostController {
    @FXML private TextArea articleName;
    @FXML private Label postDate;
    @FXML private ImageView articleImage;
    @FXML private Button locationBtn;
    @FXML private Button membershipBtn;
    @FXML private Button activityBtn;
    @FXML private TextArea articleContent;
    private NewsPost newsPost;

    private ConfigurableApplicationContext context;
    public NewsPostController() {
    }

    public void setNewsPost(NewsPost newsPost) {
        this.newsPost = newsPost;
        articleName.setText(newsPost.getName());
        articleContent.setText(newsPost.getContent());
        postDate.setText("date: " + newsPost.getDateOfPosting().toString());
        Image image;
        if(newsPost.getImageUrl()==null)image = new Image("/images/newsPostSample.jpg");
        else image = new Image(newsPost.getImageUrl());
        articleImage.setImage(image);
        if(newsPost.getActivity()==null) {
            activityBtn.setVisible(false);
            activityBtn.setDisable(true);
        }
        else initActivityBtn();
        if(newsPost.getLocation()==null){
            locationBtn.setVisible(false);
            locationBtn.setDisable(true);
        }
        else initLocationBtn();
        if(newsPost.getMembershipType()==null) membershipBtn.setVisible(false);
        else initMembershipBtn();
        articleContent.setEditable(false);
        articleName.setEditable(false);
    }

    private void initActivityBtn(){
        activityBtn.setOnAction( e->{
            try{
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/ActivitySignUp.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                ActivitySignupController activitySignupController = loader.getController();
                activitySignupController.setActivity(newsPost.getActivity());
                activitySignupController.disableSpecificActivity();
                activitySignupController.disableSignUp();
                Scene scene = new Scene(root);
                // Set the Scene to the primaryStage or a new Stage
                Stage primaryStage = new Stage(); // You might use your existing primaryStage here
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch (IOException exception){
                throw new RuntimeException("unable to load activity view");
            }
        });
    }
    private void initLocationBtn(){
        locationBtn.setOnAction( e -> {
            try{
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/locationView.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                LocationViewController controller = loader.getController();
                controller.setLocation(newsPost.getLocation());
                controller.setViewOnly();
                Scene scene = new Scene(root);
                Stage primaryStage = new Stage(); // You might use your existing primaryStage here
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch (IOException exception){
                throw new RuntimeException("unable to load activity view");
            }
        });
    }
    private void initMembershipBtn(){
        membershipBtn.setOnAction( e->{
            try{
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/membershipType.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                MembershipTypeController membershipTypeController = loader.getController();
                membershipTypeController.setMembershipType(newsPost.getMembershipType());
                Scene scene = new Scene(root);
                Stage primaryStage = new Stage();
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch (IOException exception){
                throw new RuntimeException("unable to load activity view");
            }
        });
    }
}
