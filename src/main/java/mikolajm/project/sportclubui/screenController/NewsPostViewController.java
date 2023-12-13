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
import mikolaj.project.backendapp.model.NewsPost;
import mikolajm.project.sportclubui.LoginManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Getter
public class NewsPostViewController {
    @FXML private Button button;
    @FXML private Label dateLabel;
    @FXML private Label articleName;
    @FXML private ImageView imageView;
    @FXML private AnchorPane mainView;
    private ConfigurableApplicationContext context;
    private NewsPost newsPost;

    public NewsPostViewController() {
    }

    @FXML
    public void initialize(NewsPost newsPost){
        this.newsPost = newsPost;
        dateLabel.setText(newsPost.getDateOfPosting().toString());
        articleName.setText(newsPost.getName());
        Image sampleimage = new Image("/images/newsPostSample.jpg");
        imageView.setImage(sampleimage);
        initBtn();
    }

    private void initBtn(){
        button.setOnAction(e ->
        {
            try {
               // context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/NewsPostView.fxml"));
                //loader.setControllerFactory(context::getBean);
                // Load the root node from the FXML file
                Parent root = loader.load();
                // Create a new Scene with the root node
                NewsPostController activitySignUpController = loader.getController();
                activitySignUpController.setNewsPost(newsPost);
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
