package mikolajm.project.sportclubui.screenController.adminScreens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import mikolaj.project.backendapp.model.Team;
import mikolaj.project.backendapp.model.Trainer;
import mikolaj.project.backendapp.repo.TeamRepo;
import mikolaj.project.backendapp.repo.TrainerRepo;
import mikolaj.project.backendapp.service.TrainerService;
import mikolajm.project.sportclubui.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssignTrainerController {
    @FXML private Button confirmBtn;
    @FXML private ChoiceBox<Trainer> trainerCb;
    @FXML public ChoiceBox<Team> teamCb;
    private final TrainerService trainerService;
    private final TrainerRepo trainerRepo;
    private final TeamRepo teamRepo;
    @Autowired
    public AssignTrainerController(TrainerService trainerService, TrainerRepo trainerRepo,
                                   TeamRepo teamRepo) {
        this.trainerService = trainerService;
        this.trainerRepo = trainerRepo;
        this.teamRepo = teamRepo;
    }

    public void initialize(){
        initChoiceBoxes();
        initConfirmBtn();
    }

    private void initChoiceBoxes(){
        List<Trainer> trainerList = trainerRepo.findAll();
        List<Team> teams = teamRepo.findAll();
        ObservableList<Trainer> trainerItems = FXCollections.observableArrayList();
        ObservableList<Team> teamItems = FXCollections.observableArrayList();
        trainerItems.addAll(trainerList);
        teamItems.addAll(teams);
        trainerCb.setItems(trainerItems);
        teamCb.setItems(teamItems);
    }

    private void initConfirmBtn(){
        confirmBtn.setOnAction( e-> {
            Trainer trainer;
            Team team;
            if(trainerCb.getValue()==null) {
                handleError("please choose a trainer");
                return;
            }else trainer = trainerCb.getValue();
            if(teamCb.getValue()==null) {
                handleError("please choose a team");
                return;
            }else team = teamCb.getValue();

            trainerService.assignTrainerToTeam(trainer, team);

            Stage stage = (Stage) confirmBtn.getScene().getWindow();
            stage.close();
        });
    }

    private void handleError(String error){
        Utils utils = new Utils();
        utils.showErrorMessage(error);
        trainerCb.setItems(FXCollections.observableArrayList());
        teamCb.setItems(FXCollections.observableArrayList());
    }

}
