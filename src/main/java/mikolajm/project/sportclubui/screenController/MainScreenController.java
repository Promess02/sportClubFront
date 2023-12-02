package mikolajm.project.sportclubui.screenController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;
import mikolaj.project.backendapp.controller.ActivityController;
import mikolaj.project.backendapp.controller.NewsPostController;
import mikolaj.project.backendapp.model.Activity;
import mikolaj.project.backendapp.model.NewsPost;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.LoginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Getter
@Setter
public class MainScreenController {

    @FXML private HBox activityRow;
    @FXML private HBox newsRow;
    @FXML private Button TeamsBtn;
    @FXML private Button accountBtn;
    @FXML private Button trainersBtn;
    @FXML private Button calendarBtn;
    @FXML private Button activityBtn;
    @FXML private ImageView logoView;
    private final ActivityController activityController;
    private final NewsPostController newsPostController;

    @Autowired
    public MainScreenController(ActivityController activityController, NewsPostController newsPostController) {
        this.activityController = activityController;
        this.newsPostController = newsPostController;
    }

    @FXML
    public void initialize(){
        List<Activity> activityList;
        Gson gson = new Gson();
        if(activityController.getActivities().getBody()==null) activityList = new ArrayList<>();
        String activityJson =  activityController.getActivities().getBody().toString();
        TypeToken<List<Activity>> token = new TypeToken<>() {};
        activityList = gson.fromJson(activityJson, token.getType());
        List<NewsPost> newsPostList;
        if(newsPostController.getAllPosts().getBody()==null) newsPostList = new ArrayList<>();
        String newsPostListJson = newsPostController.getAllPosts().getBody().toString();
        TypeToken<List<NewsPost>> tokenNews = new TypeToken<>(){};
        newsPostList = gson.fromJson(newsPostListJson, tokenNews.getType());
        loadNewsPostRow(newsPostList);
        loadActivityPostRow(activityList);
        Image image = new Image("src/main/resources/images/clubLogo.png");
        logoView = new ImageView(image);

    }

    private void loadNewsPostRow(List<NewsPost> list){
        for (NewsPost newsPost : list) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/NewsPost.fxml"));
                ConfigurableApplicationContext context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);

                // Load the root node from the FXML file
                Parent root = loader.load();

                // Get the MembershipTypeController from the loader
                NewsPostViewController newsPostViewController = loader.getController();
                newsPostViewController.initialize(newsPost); // You can initialize if needed

                newsRow.getChildren().add(newsPostViewController.getMainView());

            } catch (IOException ex) {
                Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void loadActivityPostRow(List<Activity> list){
        for (Activity activity : list) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/ActivityView.fxml"));
                ConfigurableApplicationContext context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);

                // Load the root node from the FXML file
                Parent root = loader.load();

                // Get the MembershipTypeController from the loader
                ActivityViewController activityViewController = loader.getController();
                activityViewController.initialize(activity); // You can initialize if needed

                activityRow.getChildren().add(activityViewController.getMainView());

            } catch (IOException ex) {
                Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
