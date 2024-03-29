package mikolajm.project.sportclubui.screenController.generalScreens;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import mikolaj.project.backendapp.model.Team;
import mikolaj.project.backendapp.model.Trainer;
import mikolaj.project.backendapp.repo.TrainerRepo;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.Util.LoginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class TeamView {
    @Getter
    @FXML private AnchorPane fullView;
    @FXML private Label teamName;
    @FXML private ImageView imageView;
    @FXML private Label membersLimit;
    @FXML private Label sportLabel;
    @FXML private Button viewTrainersBtn;

    @Getter
    private Team team;
    private ConfigurableApplicationContext context;
    private final TrainerRepo trainerRepo;
    @Autowired
    public TeamView(TrainerRepo trainerRepo) {
    this.trainerRepo = trainerRepo;
    }

    public void initialize(){
//        initTrainerBtn();
    }

    public void setTeam(Team team, int currentTeamMembers){
        this.team = team;
        Image image = new Image(team.getLogoIconUrl());
        imageView.setImage(image);
        teamName.setText(team.getName());
        membersLimit.setText("Members: " + currentTeamMembers + "/" + team.getMaxMembers());
        sportLabel.setText(team.getSport().toString());
        initTrainerBtn(team);
    }

    private void initTrainerBtn(Team team){
        viewTrainersBtn.setOnAction( e->{
            try {
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/user/trainerView.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                TrainersViewController trainersViewController = loader.getController();
                List<Trainer> list = trainerRepo.findAllByTeam(team);
                trainersViewController.initializeCustom(list);
                Scene scene = new Scene(root);
                Stage primaryStage = new Stage(); // You might use your existing primaryStage here
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch (IOException ex){
                Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }


}
