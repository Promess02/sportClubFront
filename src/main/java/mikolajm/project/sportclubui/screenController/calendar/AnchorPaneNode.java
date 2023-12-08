package mikolajm.project.sportclubui.screenController.calendar;

import javafx.fxml.FXMLLoader;
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
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.screenController.activityCalendar.ActivityCalendarController;
import mikolajm.project.sportclubui.screenController.activityCalendar.ActivitySignupController;
import mikolajm.project.sportclubui.screenController.activityCalendar.CalendarView;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * Create an anchor pane that can store additional data.
 */

@Getter
public class AnchorPaneNode extends AnchorPane {

    // Date associated with this pane
    private LocalDate date;
    private Calendar calendar;
    private Button getActivityBtn;
    private ConfigurableApplicationContext context;

    /**
     * Create a anchor pane node. Date is not assigned in the constructor.
     * @param children children of the anchor pane
     */

    public AnchorPaneNode(Node... children) {
        super(children);
        // Add action handler for mouse clicked
        this.setOnMouseClicked(e -> {
            System.out.println("This pane's date is: " + date);
            if(calendar!=null) System.out.println("This pane's calendar entity name:  " + calendar.getName());
        });
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
        setBackground(new javafx.scene.layout.Background(
                new javafx.scene.layout.BackgroundFill(Color.GREEN, null, null)));
        getActivityBtn = new Button();
        getActivityBtn.setText("=>");
        getActivityBtn.setMaxWidth(35.0);
        getActivityBtn.setMaxHeight(25.0);
        getActivityBtn.setAlignment(Pos.CENTER);
        Label textView = new Label();
        textView.setText(calendar.getName());
        textView.setFont(new Font(18.0));
        textView.setAlignment(Pos.TOP_CENTER);
        VBox vbox = new VBox();
        vbox.setSpacing(5.0);
        vbox.getChildren().add(textView);
        vbox.getChildren().add(getActivityBtn);
        vbox.setLayoutX(20.0);
        vbox.setLayoutY(20.0);
        this.getChildren().add(vbox);
        initButton();
    }

    private void initButton(){
        getActivityBtn.setOnAction(e-> {
            try {
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/ActivitySignUp.fxml"));
                loader.setControllerFactory(context::getBean);
                // Load the root node from the FXML file
                Parent root = loader.load();
                // Create a new Scene with the root node
                ActivitySignupController activitySignUpController = loader.getController();
                activitySignUpController.setActivity(calendar.getActivity());
                activitySignUpController.disableSignUp();
                Scene scene = new Scene(root);
                // Set the Scene to the primaryStage or a new Stage
                Stage primaryStage = new Stage(); // You might use your existing primaryStage here
                primaryStage.setScene(scene);
                primaryStage.show();
            } catch (IOException ex) {
                throw new RuntimeException("failed loading calendar activity view");
            }

        });
    }
}