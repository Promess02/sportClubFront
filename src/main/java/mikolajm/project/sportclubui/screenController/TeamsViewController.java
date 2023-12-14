package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mikolaj.project.backendapp.model.Member;
import mikolaj.project.backendapp.model.Team;
import mikolaj.project.backendapp.repo.MemberRepo;
import mikolaj.project.backendapp.repo.TeamRepo;
import mikolajm.project.sportclubui.ClubApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class TeamsViewController {
    @FXML private VBox teamsVBox;
    private List<Team> list;
    private final TeamRepo teamRepo;
    private final MemberRepo memberRepo;
    @Autowired
    public TeamsViewController(TeamRepo teamRepo, MemberRepo memberRepo) {
        this.teamRepo = teamRepo;
        this.memberRepo = memberRepo;
    }

    public void initialize(){
        list = teamRepo.findAll();
        for(Team team: list){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/TeamView.fxml"));
                Parent root = loader.load();
                TeamView teamView = loader.getController();
                int teamMembers = memberRepo.findAllByTeam(team).size();
                teamView.initialize(team,teamMembers,null);
                teamsVBox.getChildren().add(teamView.getFullView());
//                Scene scene = new Scene(root);
//                // Set the Scene to the primaryStage or a new Stage
//                Stage primaryStage = new Stage(); // You might use your existing primaryStage here
//                primaryStage.setScene(scene);
//                primaryStage.show();
            }catch(IOException exception){
                throw new RuntimeException("unable to load team view");
            }
        }
    }


}
