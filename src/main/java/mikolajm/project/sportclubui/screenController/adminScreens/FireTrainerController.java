package mikolajm.project.sportclubui.screenController.adminScreens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import mikolaj.project.backendapp.model.Trainer;
import mikolaj.project.backendapp.repo.TrainerRepo;
import mikolaj.project.backendapp.service.TrainerService;
import mikolajm.project.sportclubui.Util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FireTrainerController {
    @FXML private ChoiceBox<Trainer> trainerCb;
    @FXML private Button fireBtn;
    private final TrainerService trainerService;
    private final TrainerRepo trainerRepo;

    @Autowired
    public FireTrainerController(TrainerService trainerService, TrainerRepo trainerRepo) {
        this.trainerService = trainerService;
        this.trainerRepo = trainerRepo;
    }

    public void initialize(){
        List<Trainer> trainerList = trainerRepo.findAll();
        ObservableList<Trainer> trainerItems = FXCollections.observableArrayList();
        trainerItems.addAll(trainerList);
        trainerCb.setItems(trainerItems);
        initBtn();
    }

    private void initBtn(){
        fireBtn.setOnAction( e->  {
            if(trainerCb.getValue()==null) {
                handleError("no trainer selected");
                return;
            }
            Trainer trainer = trainerCb.getValue();
            trainerService.deleteTrainer(trainer);

            Stage stage = (Stage) fireBtn.getScene().getWindow();
            stage.close();
        });
    }

    private void handleError(String error){
        Utils utils = new Utils();
        utils.showErrorMessage(error);
    }
}
