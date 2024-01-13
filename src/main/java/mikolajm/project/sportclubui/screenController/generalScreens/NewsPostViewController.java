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
import mikolaj.project.backendapp.model.NewsPost;
import mikolajm.project.sportclubui.LoginManager;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
//@Component
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

    public void initialize(){
    }

    public void setNewsPost(NewsPost newsPost){
        this.newsPost = newsPost;
        dateLabel.setText(newsPost.getDateOfPosting().toString());
        articleName.setText(newsPost.getName());
        Image sampleimage;
        if(newsPost.getImageUrl()==null) sampleimage = new Image("/images/newsPostSample.jpg");
        else sampleimage = new Image(newsPost.getImageUrl(), false);
        imageView.setImage(sampleimage);
        initBtn();
    }

    private void initBtn(){
        button.setOnAction(e ->
        {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/userAndTrainer/NewsPostView.fxml"));
                Parent root = loader.load();
                NewsPostController activitySignUpController = loader.getController();
                activitySignUpController.setNewsPost(newsPost);
                Scene scene = new Scene(root);
                Stage primaryStage = new Stage(); // You might use your existing primaryStage here
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch (IOException ex){
                Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
