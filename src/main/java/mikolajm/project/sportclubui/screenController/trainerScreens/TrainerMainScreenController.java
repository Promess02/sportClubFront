package mikolajm.project.sportclubui.screenController.trainerScreens;

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
import mikolaj.project.backendapp.model.NewsPost;
import mikolaj.project.backendapp.model.SocialMedia;
import mikolaj.project.backendapp.repo.NewsPostRepo;
import mikolaj.project.backendapp.repo.SocialMediaRepo;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.CurrentSessionUser;
import mikolajm.project.sportclubui.LoginManager;
import mikolajm.project.sportclubui.Utils;
import mikolajm.project.sportclubui.screenController.adminScreens.AddActivityViewController;
import mikolajm.project.sportclubui.screenController.adminScreens.AddMemberTeamController;
import mikolajm.project.sportclubui.screenController.calendar.CalendarViewController;
import mikolajm.project.sportclubui.screenController.calendar.FullCalendarView;
import mikolajm.project.sportclubui.screenController.generalScreens.NewsPostViewController;
import mikolajm.project.sportclubui.screenController.generalScreens.SocialsNodeController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.YearMonth;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class TrainerMainScreenController {
    @FXML private Button addPersonalTraining;
    @FXML private MenuItem logoutBtn;
    @FXML private MenuItem showMembersBtn;
    @FXML private Button addNewsPost;
    @FXML private ImageView loginImageView;
    @FXML private Button addActivityBtn;
    @FXML private Button calendarBtn;
    @FXML private MenuItem accountBtn;
    @FXML private MenuItem addMemberBtn;
    @FXML private MenuItem deleteMembersBtn;
    @FXML private HBox newsRow;
    @FXML private HBox socialsRow;

    private final NewsPostRepo newsPostRepo;
    private final SocialMediaRepo socialMediaRepo;
    private final CurrentSessionUser currentSessionUser;
    private ConfigurableApplicationContext context;
    Utils utils = new Utils();

    @Autowired
    public TrainerMainScreenController(NewsPostRepo newsPostRepo, SocialMediaRepo socialMediaRepo, CurrentSessionUser currentSessionUser) {
        this.currentSessionUser = currentSessionUser;
        this.newsPostRepo = newsPostRepo;
        this.socialMediaRepo = socialMediaRepo;
    }

    public void initialize(){
        Image image = new Image("/images/clubLogo.jpg");
        loginImageView.setImage(image);
        initRows();
        initAccountBtn();
        initAddActivityBtn();
        initCalendarBtn();
        initAddNewsPost();
        initAddMemberBtn();
        initShowTeamMembersBtn();
        initDeleteMemberFromTeamBtn();
        initLogoutBtn();
        initPersonalTraining();
    }

    private void initPersonalTraining(){
        addPersonalTraining.setOnAction( e-> {
            try {
                if(currentSessionUser.getTrainer()==null){
                    handleError("somehow you don't have trainer privileges");
                    return;
                }
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/trainer/addPersonalTraining.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                throw new RuntimeException("failed loading add personal training view");
            }
        });
    }

    private void initLogoutBtn(){
        logoutBtn.setOnAction( e-> {
            Stage primaryStage = (Stage) calendarBtn.getScene().getWindow();
            primaryStage.close();
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/loginRegister/EnterScreen.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }catch (IOException exception){
                Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, exception);
            }
            currentSessionUser.logout();
        });
    }

    private void initAddMemberBtn(){
        addMemberBtn.setOnAction( e-> {
            try {
                if(currentSessionUser.getTrainer()==null){
                    handleError("somehow you don't have trainer privileges");
                    return;
                }
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/trainer/AddMemberToTeam.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                AddMemberTeamController controller = loader.getController();
                controller.initForTrainer(currentSessionUser.getTrainer());
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                throw new RuntimeException("failed loading add member view");
            }
        });
    }

    private void initShowTeamMembersBtn(){
        showMembersBtn.setOnAction( e-> {
            try {
                if(currentSessionUser.getTrainer()==null){
                    handleError("somehow you don't have trainer privileges");
                    return;
                }
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/trainer/showTeamMembers.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                ShowTeamMembersController controller = loader.getController();
                controller.setTeam(currentSessionUser.getTrainer().getTeam());
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                throw new RuntimeException("failed loading show member view");
            }
        });
    }

    private void initDeleteMemberFromTeamBtn(){
        deleteMembersBtn.setOnAction( e -> {
            try {
                if(currentSessionUser.getTrainer()==null){
                    handleError("somehow you don't have trainer privileges");
                    return;
                }
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/trainer/deleteMembersTeam.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                DeleteMembersTeamController controller = loader.getController();
                controller.setTeam(currentSessionUser.getTrainer().getTeam());
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                throw new RuntimeException("failed loading delete member view");
            }
        });
    }

    private void initCalendarBtn(){
        calendarBtn.setOnAction( e->{
            try {
                if(currentSessionUser.getTrainer()==null){
                    handleError("somehow you don't have trainer privileges");
                    return;
                }
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/userAndTrainer/fullCalendar.fxml"));
                Parent root = loader.load();
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
        });
    }
    private void initAddActivityBtn(){
        addActivityBtn.setOnAction( e ->{
            try {
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/addActivityView.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                AddActivityViewController activityViewController = loader.getController();
                activityViewController.setTrainer(currentSessionUser.getTrainer());
                Scene scene = new Scene(root);
                Stage primaryStage = new Stage();
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch (IOException ex){
                Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    private void initAccountBtn(){
        accountBtn.setOnAction( e ->{
            try {
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/trainer/trainerAccount.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage primaryStage = new Stage(); // You might use your existing primaryStage here
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch (IOException ex){
                Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void initAddNewsPost(){
        addNewsPost.setOnAction( e-> {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/addNewsPost.fxml"));
                context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }catch (IOException exception){
                throw new RuntimeException("unable to load add news post view");
            }
        });
    }
    private void initRows(){
        List<NewsPost> newsPostList = newsPostRepo.findAll();
        List<SocialMedia> socialMediaList = socialMediaRepo.findAll();

        for (NewsPost newsPost : newsPostList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/userAndTrainer/NewsPost.fxml"));
                Parent root = loader.load();
                NewsPostViewController newsPostViewController = loader.getController();
                newsPostViewController.setNewsPost(newsPost);
                newsRow.getChildren().add(newsPostViewController.getMainView());
            } catch (IOException ex) {
                Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        for(SocialMedia socialMedia: socialMediaList){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/socialsNode.fxml"));
                Parent root = loader.load();
                SocialsNodeController socialsNodeController = loader.getController();
                socialsNodeController.setSocial(socialMedia);
                socialsRow.getChildren().add(socialsNodeController.getFullView());
            }catch (IOException exception){
                Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, exception);
            }

        }
    }

    private void handleError(String error){
        utils.showErrorMessage(error);
    }
}
