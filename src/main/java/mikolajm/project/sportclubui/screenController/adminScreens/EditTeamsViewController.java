package mikolajm.project.sportclubui.screenController.adminScreens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import mikolaj.project.backendapp.model.Team;
import mikolaj.project.backendapp.repo.TeamRepo;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class EditTeamsViewController {
    @FXML private Label label;
    @FXML private ChoiceBox<Team> teamCb;
    @FXML private Button submitBtn;
    private final TeamRepo teamRepo;
    private List<Team> teamList;
    private ConfigurableApplicationContext context;

    @Autowired
    public EditTeamsViewController(TeamRepo teamRepo) {
        this.teamRepo = teamRepo;
    }

    public void initialize(){
        teamList = teamRepo.findAll();
        initChoiceBox();
    }

    public void setDeleteView(){
        label.setText("Choose team to delete");
        submitBtn.setText("delete");

        submitBtn.setOnAction( e -> {
            if(teamCb.getValue()==null){
                handleError("choose team first");
                return;
            }
            for(Team team: teamList)
                if(team.getName().equals(teamCb.getValue().getName()))
                    teamRepo.delete(team);

            Stage stage = (Stage) submitBtn.getScene().getWindow();
            stage.close();
        });
    }

    public void setUpdateView(){
        label.setText("choose team to update");
        submitBtn.setText("update");

        submitBtn.setOnAction( e -> {
            if(teamCb.getValue()==null){
                handleError("choose team first");
                return;
            }
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/updateTeamView.fxml"));
                context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                UpdateTeamController updateTeamController = loader.getController();
                updateTeamController.setTeam(teamCb.getValue());
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }catch (IOException exception){
                throw new RuntimeException("unable to load update activity view");
            }

            Stage stage = (Stage) submitBtn.getScene().getWindow();
            stage.close();

        });
    }

    private void initChoiceBox(){
        List<Team> teamList = teamRepo.findAll();
        ObservableList<Team> teamItems = FXCollections.observableArrayList();
        teamItems.addAll(teamList);
        teamCb.setItems(teamItems);
    }


    private void handleError(String error){
        Utils utils = new Utils();
        utils.showErrorMessage(error);
    }
}
