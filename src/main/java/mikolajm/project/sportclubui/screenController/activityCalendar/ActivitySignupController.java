package mikolajm.project.sportclubui.screenController.activityCalendar;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Getter;
import mikolaj.project.backendapp.model.Activity;
import mikolaj.project.backendapp.model.Member;
import mikolaj.project.backendapp.service.CalendarService;
import mikolajm.project.sportclubui.CurrentSessionUser;
import org.springframework.stereotype.Component;

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

    public ActivitySignupController(CurrentSessionUser currentSessionUser,
                                    CalendarService calendarService) {
        this.currentSessionUser = currentSessionUser;
        this.calendarService = calendarService;
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
        String teamTxt = "Team: " + activity.getTeam().getName();
        teamLabel.setText(teamTxt);
        String locationTxt = "Location: " + activity.getLocation().getName();
        locationLabel.setText(locationTxt);
        descriptionArea.setText(activity.getDescription());
        descriptionArea.setEditable(false);
        initSignUpBtn();
    }

    public void disableSignUp(){
        signUpBtn.setText("OK");
        signUpBtn.setOnAction(e-> {
            Stage stage = (Stage) signUpBtn.getScene().getWindow();
            stage.close();
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
            calendarService.signUpForActivity(member, activity);
            currentSessionUser.loadCalendarList();
            //close screen
            Stage stage = (Stage) signUpBtn.getScene().getWindow();
            stage.close();
        });
    }
}
