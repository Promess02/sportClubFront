package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import mikolaj.project.backendapp.model.Activity;
import mikolaj.project.backendapp.model.NewsPost;
import mikolaj.project.backendapp.service.ActivityService;
import mikolaj.project.backendapp.service.NewsPostService;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.CurrentSessionUser;
import mikolajm.project.sportclubui.LoginManager;
import mikolajm.project.sportclubui.screenController.calendar.CalendarViewController;
import mikolajm.project.sportclubui.screenController.calendar.FullCalendarView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
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
    private final ActivityService activityService;
    private final NewsPostService newsPostService;
    private final CurrentSessionUser currentSessionUser;
    ConfigurableApplicationContext context = ClubApplication.getApplicationContext();

    public MainScreenController(ActivityService activityService, NewsPostService newsPostService,
                                CurrentSessionUser currentSessionUser) {
        this.activityService = activityService;
        this.newsPostService = newsPostService;
        this.currentSessionUser = currentSessionUser;
    }

    @FXML
    public void initialize(){
        Image image = new Image("/images/clubLogo.png");
        logoView = new ImageView(image);
        Optional<List<Activity>> activityList;
        activityList = activityService.getAllActivities().getData();
        Optional<List<NewsPost>> newsPostList;
        newsPostList = newsPostService.viewAllNewsPost().getData();
        if(activityList.isEmpty()) return;
        if(newsPostList.isEmpty()) return;
        loadNewsPostRow(newsPostList.get());
        loadActivityPostRow(activityList.get());
        initAccountBtn();
        initCalendarBtn();
    }

    private void initAccountBtn(){
            accountBtn.setOnAction( event ->
            {
                try {
                    context = ClubApplication.getApplicationContext();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/accountView.fxml"));
                    loader.setControllerFactory(context::getBean);
                    // Load the root node from the FXML file
                    Parent root = loader.load();
                    // Create a new Scene with the root node
                    AccountViewController accountViewController = loader.getController();
                    accountViewController.initialize();
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

    private void initCalendarBtn(){
        calendarBtn.setOnAction( event -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/fullCalendar.fxml"));
                        Parent root = loader.load();
                        // Get the controller and add the calendar view to it
                        CalendarViewController controller = loader.getController();
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        controller.getCalendarPane().getChildren().add(new FullCalendarView(YearMonth.now(),
                                currentSessionUser.getCalendarList()).getView());
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        throw new RuntimeException("failed loading calendar view");
                    }
                }
        );
    }

    private void loadNewsPostRow(List<NewsPost> list){
        for (NewsPost newsPost : list) {
            try {
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/NewsPost.fxml"));
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
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/ActivityView.fxml"));
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
