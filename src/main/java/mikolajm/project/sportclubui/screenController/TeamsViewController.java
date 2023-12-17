package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
        ConfigurableApplicationContext context = ClubApplication.getApplicationContext();
        list = teamRepo.findAll();
        for(Team team: list){
                try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/TeamView.fxml"));
               loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                TeamView teamView = loader.getController();
                int teamMembers = memberRepo.findAllByTeam(team).size();
                teamView.initialize(team,teamMembers);
                teamsVBox.getChildren().add(teamView.getFullView());
            }catch (IOException ex){
                throw new RuntimeException("unable to load team view");
            }
        }
    }


}
