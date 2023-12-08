package mikolajm.project.sportclubui.screenController.activityCalendar;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.Getter;
import mikolaj.project.backendapp.model.Activity;
import mikolaj.project.backendapp.model.Calendar;
import mikolaj.project.backendapp.service.CalendarService;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.CurrentSessionUser;
import mikolajm.project.sportclubui.LoginManager;
import mikolajm.project.sportclubui.screenController.AccountViewController;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Component
public class ActivityNode extends AnchorPane {
    private LocalDate date;
    private Activity activity;
    private Button getActivityBtn;
    private boolean enableBtn = true;
    private ConfigurableApplicationContext context;

    public ActivityNode() {
        // Add action handler for mouse clicked
        this.setOnMouseClicked(e -> {
            System.out.println("This pane's date is: " + date);
            if(activity!=null) System.out.println("This pane's calendar entity name:  " + activity.getName());
        });
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setActivity(Activity activity, Boolean isAlreadySigned) {
        this.activity = activity;
        setBackground(new javafx.scene.layout.Background(
                new javafx.scene.layout.BackgroundFill(Color.GREEN, null, null)));
        VBox vbox = new VBox();
        getActivityBtn = new Button();
        vbox.setSpacing(5.0);

        Label textView = new Label();
        textView.setText(activity.getName());
        textView.setFont(new Font(14.0));
        textView.setAlignment(Pos.TOP_CENTER);
        vbox.getChildren().add(textView);

        Label memberLimit  = new Label();
        String limit = "Members: " + activity.getCurrentMembers() + "/" + activity.getMemberLimit();
        memberLimit.setText(limit);
        memberLimit.setFont(new Font(12.0));
        memberLimit.setAlignment(Pos.BOTTOM_CENTER);
        memberLimit.setPadding(new Insets(0,0,5,0));
        vbox.getChildren().add(memberLimit);
        vbox.setLayoutX(10.0);
        vbox.setLayoutY(17.0);
        if(isAlreadySigned){
            Label alreadySignedText = new Label("Signed!");
            alreadySignedText.setFont(new Font(15.0));
            vbox.getChildren().add(alreadySignedText);
            getActivityBtn.setText("=>");
            enableBtn = false;
        }else getActivityBtn.setText("Sign up");

        getActivityBtn.setFont(new Font(15.0));
        getActivityBtn.setMaxWidth(50.0);
        getActivityBtn.setMaxHeight(25.0);
        getActivityBtn.setAlignment(Pos.CENTER);
        vbox.getChildren().add(getActivityBtn);
        if(activity.getCurrentMembers().equals(activity.getMemberLimit())) {
            getActivityBtn.setVisible(false);
            Label limitReached= new Label("limit reached");
            limitReached.setFont(new Font(12.0));
            vbox.getChildren().add(limitReached);
        }

        this.getChildren().add(vbox);
        initActivityBtn();
    }

    private void initActivityBtn(){
        getActivityBtn.setOnAction( e-> {
            try {
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/ActivitySignUp.fxml"));
                loader.setControllerFactory(context::getBean);
                // Load the root node from the FXML file
                Parent root = loader.load();
                // Create a new Scene with the root node
                ActivitySignupController activitySignUpController = loader.getController();
                activitySignUpController.setActivity(activity);
                if(!enableBtn) activitySignUpController.disableSignUp();
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
