package mikolajm.project.sportclubui.screenController.calendar;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.Getter;
import mikolaj.project.backendapp.model.Calendar;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.Util.CurrentSessionUser;
import mikolajm.project.sportclubui.screenController.activityCalendar.ActivitySignupController;
import mikolajm.project.sportclubui.screenController.trainerScreens.AddPersonalTrainingController;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.time.LocalDate;


@Getter
public class AnchorPaneNode extends AnchorPane {

    private LocalDate date;
    private Calendar calendar;
    private Button getActivityBtn;
    private ConfigurableApplicationContext context;

    public AnchorPaneNode(Node... children) {
        super(children);
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
                if(calendar.getActivity()!=null){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/user/ActivitySignUp.fxml"));
                    loader.setControllerFactory(context::getBean);
                    Parent root = loader.load();
                    ActivitySignupController activitySignUpController = loader.getController();
                    activitySignUpController.setActivity(calendar.getActivity());
                    activitySignUpController.disableSignUp();
                    Scene scene = new Scene(root);
                    Stage primaryStage = new Stage();
                    primaryStage.setScene(scene);
                    primaryStage.show();
                }else{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/trainer/addPersonalTraining.fxml"));
                    loader.setControllerFactory(context::getBean);
                    Parent root = loader.load();
                    AddPersonalTrainingController controller = loader.getController();
                    CurrentSessionUser currentSessionUser = context.getBean(CurrentSessionUser.class);
                    controller.setCalendar(calendar);
                    if(currentSessionUser.getTrainer()==null) controller.setViewOnly();
                    Scene scene = new Scene(root);
                    Stage primaryStage = new Stage();
                    primaryStage.setScene(scene);
                    primaryStage.show();
                }
            } catch (IOException ex) {
                throw new RuntimeException("failed loading calendar activity view");
            }

        });
    }
}