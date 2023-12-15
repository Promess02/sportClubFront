package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import mikolaj.project.backendapp.model.Team;
import mikolaj.project.backendapp.model.Trainer;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public TeamView() {
    }

    @FXML
    public void initialize(Team team, int currentTeamMembers, List<Trainer> trainerList){
        this.team = team;
        Image image = new Image("/images/newsPostSample.jpg");
        imageView.setImage(image);
        teamName.setText(team.getName());
        membersLimit.setText("Members: " + currentTeamMembers + "/" + team.getMaxMembers());
        sportLabel.setText(team.getSport().toString());
        //TODO() - add btn action listener to view trainers
    }
}
