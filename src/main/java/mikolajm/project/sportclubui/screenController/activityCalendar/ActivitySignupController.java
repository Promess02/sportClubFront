package mikolajm.project.sportclubui.screenController.activityCalendar;

import jakarta.persistence.Cache;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Setter;
import mikolaj.project.backendapp.model.Activity;
import mikolaj.project.backendapp.model.Calendar;
import mikolaj.project.backendapp.model.Member;
import mikolaj.project.backendapp.service.ActivityService;
import mikolaj.project.backendapp.service.CalendarService;
import mikolajm.project.sportclubui.CurrentSessionUser;
import org.springframework.stereotype.Component;

@Component
public class ActivitySignupController {
    public Button signUpBtn;
    public TextArea descriptionArea;
    public Button locationBtn;
    public Label locationLabel;
    public Button teamBtn;
    public Label teamLabel;
    public Button trainerBtn;
    public Label trainerLabel;
    public Label membersLimit;
    public Label timeLabel;
    public ImageView imageView;
    public Label nameLabel;
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
        initSignUpBtn();
    }

    public void disableSignUp(){
        signUpBtn.setText("OK");
        signUpBtn.setOnAction(e-> {
            Stage stage = (Stage) signUpBtn.getScene().getWindow();
            stage.close();
        });
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
