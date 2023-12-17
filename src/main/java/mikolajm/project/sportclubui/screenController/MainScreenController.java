package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import mikolaj.project.backendapp.model.Activity;
import mikolaj.project.backendapp.model.NewsPost;
import mikolaj.project.backendapp.service.ActivityService;
import mikolaj.project.backendapp.service.CalendarService;
import mikolaj.project.backendapp.service.NewsPostService;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.CurrentSessionUser;
import mikolajm.project.sportclubui.LoginManager;
import mikolajm.project.sportclubui.screenController.activityCalendar.ActivityCalendarController;
import mikolajm.project.sportclubui.screenController.activityCalendar.CalendarView;
import mikolajm.project.sportclubui.screenController.calendar.CalendarViewController;
import mikolajm.project.sportclubui.screenController.calendar.FullCalendarView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.YearMonth;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
public class MainScreenController {

    @FXML private HBox activityRow;
    @FXML private HBox newsRow;
    @FXML private MenuItem TeamsBtn;
    @FXML private MenuItem accountBtn;
    @FXML private MenuItem trainersBtn;
    @FXML private MenuItem logoutBtn;
    @FXML private MenuItem calendarBtn;
    @FXML private MenuItem teamsBtn;
    @FXML private MenuItem activityBtn;
    @FXML private Button membershipTypeBtn;
    @FXML private ImageView logoView;
    private final ActivityService activityService;
    private final NewsPostService newsPostService;
    private final CalendarService calendarService;
    private final CurrentSessionUser currentSessionUser;
    ConfigurableApplicationContext context = ClubApplication.getApplicationContext();

    public MainScreenController(ActivityService activityService, NewsPostService newsPostService,
                                CurrentSessionUser currentSessionUser, CalendarService calendarService) {
        this.activityService = activityService;
        this.newsPostService = newsPostService;
        this.currentSessionUser = currentSessionUser;
        this.calendarService = calendarService;
    }

    @FXML
    public void initialize(){
        Image image = new Image("/images/clubLogo.jpg");
        logoView.setImage(image);
        Optional<List<Activity>> activityList;
        activityList = activityService.getAllActivities().getData();
        Optional<List<NewsPost>> newsPostList;
        newsPostList = newsPostService.viewAllNewsPost().getData();
        if(activityList.isEmpty()) return;
        if(newsPostList.isEmpty()) return;
        Set<Activity> set = new HashSet<>(activityList.get().stream()
                .collect(Collectors.toMap(Activity::getName, activity -> activity, (a, b) -> a))
                .values());
        List<Activity> distinctActivities = set.stream().toList();
        loadNewsPostRow(newsPostList.get());
        loadActivityPostRow(distinctActivities);
        initAccountBtn();
        initCalendarBtn();
        initActivityBtn();
        initMembershipTypeBtn();
        initTeamsBtn();
        initTrainersBtn();
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

    private void initTrainersBtn(){
        trainersBtn.setOnAction( e ->{
            try {
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/trainerView.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                TrainersViewController trainersViewController = loader.getController();
                Scene scene = new Scene(root);
                Stage primaryStage = new Stage(); // You might use your existing primaryStage here
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch (IOException ex){
                Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void initTeamsBtn(){
        teamsBtn.setOnAction( event ->
        {
            try {
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/teamsView.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                TeamsViewController teamsViewController = loader.getController();
                Scene scene = new Scene(root);
                Stage primaryStage = new Stage(); // You might use your existing primaryStage here
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch (IOException ex){
                Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void initMembershipTypeBtn(){
        membershipTypeBtn.setOnAction( e ->{
            try{
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/membership.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                MembershipUiController membershipUiController = loader.getController();
                Scene scene = new Scene(root);
                // Set the Scene to the primaryStage or a new Stage
                Stage primaryStage = new Stage(); // You might use your existing primaryStage here
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch(IOException exception){
                throw new RuntimeException("unable to load membership type view");
            }
        });
    }

    private void initActivityBtn(){
        activityBtn.setOnAction( event -> {
                    try {
                        if(currentSessionUser.getMember()==null){
                           showErrorMessage("unable to show calendar without an active membership");
                           return;
                        }
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/activityCalendar.fxml"));
                        Parent root = loader.load();
                        // Get the controller and add the calendar view to it
                        ActivityCalendarController controller = loader.getController();
                        List<Activity> listOfActivities = activityService.getAllActivities().getData().orElse(new ArrayList<>());
                        List<Activity> alreadySignedList =  currentSessionUser.getAlreadySignedList();
                        controller.getActivityPane().getChildren().add(
                                new CalendarView(YearMonth.now(), listOfActivities, alreadySignedList).getView());
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        throw new RuntimeException("failed loading calendar activity view");
                    }
                }
        );
    }

    private void initCalendarBtn(){
        calendarBtn.setOnAction( event -> {
                    try {
                        if(currentSessionUser.getMember()==null){
                            showErrorMessage("unable to show calendar without an active membership");
                            return;
                        }
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
    private void showErrorMessage(String error){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/errorMsg.fxml"));
            Parent root = loader.load();
            ErrorMessageController errorMessageController = loader.getController();
            errorMessageController.setError(error);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }catch (IOException exception){
            throw new RuntimeException("failed loading error view");
        }
    }

}
