package mikolajm.project.sportclubui.screenController.activityCalendar;

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
import mikolaj.project.backendapp.DTO.ServiceResponse;
import mikolaj.project.backendapp.model.Activity;
import mikolaj.project.backendapp.model.Member;
import mikolaj.project.backendapp.repo.MemberRepo;
import mikolaj.project.backendapp.service.CalendarService;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.CurrentSessionUser;
import mikolajm.project.sportclubui.Utils;
import mikolajm.project.sportclubui.screenController.TeamView;
import mikolajm.project.sportclubui.screenController.TrainerProfileController;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Getter
public class ActivitySignupController {
    @FXML private Button signUpBtn;
    @FXML private TextArea descriptionArea;
    @FXML private Button locationBtn;
    @FXML private Label locationLabel;
    @FXML private Button teamBtn;
    @FXML private Label teamLabel;
    @FXML private Button trainerBtn;
    @FXML private Label trainerLabel;
    @FXML private Label membersLimit;
    @FXML private Label timeLabel;
    @FXML private ImageView imageView;
    @FXML private Label nameLabel;
    private Activity activity;
    private final CurrentSessionUser currentSessionUser;
    private final CalendarService calendarService;
    private ConfigurableApplicationContext context;
    private final MemberRepo memberRepo;

    Utils utils = new Utils();

    public ActivitySignupController(CurrentSessionUser currentSessionUser,
                                    CalendarService calendarService, MemberRepo memberRepo) {
        this.currentSessionUser = currentSessionUser;
        this.calendarService = calendarService;
        this.memberRepo = memberRepo;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
        nameLabel.setText(activity.getName());
        Image image = new Image("/images/newsPostSample.jpg");
        imageView.setImage(image);
        String timeTxt = activity.getDate().toString()+": " + activity.getTime() + " " + activity.getMinutes()+" minutes";
        timeLabel.setText(timeTxt);
        String membersTxt = "current Members: " + activity.getCurrentMembers() + "/"+ activity.getMemberLimit();
        membersLimit.setText(membersTxt);
        String trainerTxt = "Trainer: " + activity.getTrainer().getUser().getName() + " " +activity.getTrainer().getUser().getSurname();
        trainerLabel.setText(trainerTxt);
        if(activity.getTeam()!=null){
            String teamTxt = "Team: " + activity.getTeam().getName();
            teamLabel.setText(teamTxt);
        }else teamBtn.setVisible(false);
        String locationTxt = "Location: " + activity.getLocation().getName();
        locationLabel.setText(locationTxt);
        descriptionArea.setText(activity.getDescription());
        descriptionArea.setEditable(false);
        initSignUpBtn();
        initTrainerBtn();
        initTeamBtn();
    }

    public void disableSignUp(){
        signUpBtn.setText("OK");
        signUpBtn.setOnAction(e-> {
            Stage stage = (Stage) signUpBtn.getScene().getWindow();
            stage.close();
        });
    }

    public void initTrainerBtn(){
        trainerBtn.setOnAction( e->{
            try{
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/trainerProfile.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                TrainerProfileController trainerProfileController = loader.getController();
                trainerProfileController.setTrainer(activity.getTrainer());
                // Set the Scene to the primaryStage or a new Stage
                Stage primaryStage = new Stage(); // You might use your existing primaryStage here
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch (IOException exception){
                throw new RuntimeException("unable to load activity view");
            }
        });
    }

    public void initTeamBtn(){
        teamBtn.setOnAction( e->{
            try{
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/TeamView.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                TeamView teamView = loader.getController();
                int currTeamMembers = memberRepo.findAllByTeam(activity.getTeam()).size();
                teamView.setTeam(activity.getTeam(), currTeamMembers);
                // Set the Scene to the primaryStage or a new Stage
                Stage primaryStage = new Stage(); // You might use your existing primaryStage here
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch (IOException exception){
                throw new RuntimeException("unable to load activity view");
            }
        });
    }

    public void disableSpecificActivity(){
        timeLabel.setVisible(false);
        membersLimit.setVisible(false);
    }
    private void initSignUpBtn(){
        signUpBtn.setOnAction(e->
        {
            Member member = currentSessionUser.getMember();
            ServiceResponse<?> serviceResponse = calendarService.signUpForActivity(member, activity);
            if(serviceResponse.getMessage().equals("member is busy")) {
                handleError("you have an activity during that time");
                return;
            }
            currentSessionUser.loadCalendarList();
            context = ClubApplication.getApplicationContext();
            CalendarView calendarView = context.getBean(CalendarView.class);
            calendarView.refreshView();
            Stage stage = (Stage) signUpBtn.getScene().getWindow();
            stage.close();
        });
    }

    private void handleError(String error){
        utils.showErrorMessage(error);
    }
}
