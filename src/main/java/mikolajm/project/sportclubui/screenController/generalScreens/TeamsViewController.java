package mikolajm.project.sportclubui.screenController.generalScreens;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import mikolaj.project.backendapp.model.Team;
import mikolaj.project.backendapp.model.Trainer;
import mikolaj.project.backendapp.repo.MemberRepo;
import mikolaj.project.backendapp.repo.TeamRepo;
import mikolajm.project.sportclubui.ClubApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class TeamsViewController {
    @FXML private VBox teamsVBox;
    private List<Team> list;
    private final TeamRepo teamRepo;
    private final MemberRepo memberRepo;
    private ConfigurableApplicationContext context;

    @Autowired
    public TeamsViewController(TeamRepo teamRepo, MemberRepo memberRepo) {
        this.teamRepo = teamRepo;
        this.memberRepo = memberRepo;
    }
    public void initialize(){
    }

    public void initAll(){
        list = teamRepo.findAll();
        fillView();
    }
    private void fillView(){
        context = ClubApplication.getApplicationContext();
        for(Team team: list){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/user/TeamView.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                TeamView teamView = loader.getController();
                int teamMembers = memberRepo.findAllByTeam(team).size();
                teamView.setTeam(team,teamMembers);
                teamsVBox.getChildren().add(teamView.getFullView());
            }catch (IOException ex){
                throw new RuntimeException("unable to load team view");
            }
        }
    }

    public void initForATrainer(Trainer trainer){
        Team team = trainer.getTeam();
        try{
            context = ClubApplication.getApplicationContext();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/user/TeamView.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();
            TeamView teamView = loader.getController();
            int teamMembers = memberRepo.findAllByTeam(team).size();
            teamView.setTeam(team,teamMembers);
            teamsVBox.getChildren().add(teamView.getFullView());
        }catch (IOException ex){
            throw new RuntimeException("unable to load team view");
        }
    }


}
