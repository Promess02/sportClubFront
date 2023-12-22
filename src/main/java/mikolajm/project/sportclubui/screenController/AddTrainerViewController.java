package mikolajm.project.sportclubui.screenController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import mikolaj.project.backendapp.enums.Sport;
import mikolaj.project.backendapp.model.Team;
import mikolaj.project.backendapp.model.Trainer;
import mikolaj.project.backendapp.model.User;
import mikolaj.project.backendapp.repo.TeamRepo;
import mikolaj.project.backendapp.repo.UserRepo;
import mikolaj.project.backendapp.service.TrainerService;
import mikolajm.project.sportclubui.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddTrainerViewController {

    @FXML private ChoiceBox<Sport> SpecializationCb;
    @FXML private ChoiceBox<Team> teamCb;
    @FXML private ChoiceBox<User> userCb;
    @FXML private Button hireBtn;

    private final TrainerService trainerService;
    private final TeamRepo teamRepo;
    private final UserRepo userRepo;
    @Autowired
    public AddTrainerViewController(TrainerService trainerService, TeamRepo teamRepo,
                                    UserRepo userRepo) {
    this.trainerService = trainerService;
    this.teamRepo = teamRepo;
    this.userRepo = userRepo;
    }

    public void initialize(){
        initCheckBoxes();
        initSubmitBtn();
    }

    private void initCheckBoxes(){
        List<Team> teamList = teamRepo.findAll();
        List<User> users = userRepo.findAll();
        ObservableList<Team> teamItems = FXCollections.observableArrayList();
        ObservableList<User> userItems = FXCollections.observableArrayList();
        ObservableList<Sport> specializationItems = FXCollections.observableArrayList();

        teamItems.addAll(teamList);
        userItems.addAll(users);
        specializationItems.addAll(Sport.values());

        userCb.setItems(userItems);
        teamCb.setItems(teamItems);
        SpecializationCb.setItems(specializationItems);
    }

    private void initSubmitBtn(){
        hireBtn.setOnAction( e->{
            Trainer trainer = new Trainer();
            if(teamCb.getValue()!=null) trainer.setTeam(teamCb.getValue());
            if(userCb.getValue()==null) {
                handleError("please select one user");
                return;
            }
            else trainer.setUser(userCb.getValue());
            if(SpecializationCb.getValue()==null) {
                handleError("please select one specialization");
                return;
            } else trainer.setSpecialization(SpecializationCb.getValue().getName());
            trainerService.addTrainer(userCb.getValue(), trainer);
            Stage stage =(Stage) hireBtn.getScene().getWindow();
            stage.close();
        });
    }

    private void handleError(String error){
        Utils utils = new Utils();
        utils.showErrorMessage(error);
    }
}
