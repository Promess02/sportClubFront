package mikolajm.project.sportclubui.screenController.generalScreens;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import lombok.Getter;
import mikolaj.project.backendapp.model.Trainer;
import mikolaj.project.backendapp.repo.TrainerRepo;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.LoginManager;
import mikolajm.project.sportclubui.screenController.generalScreens.TrainerProfileController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Component
public class TrainersViewController {
    public VBox trainersVBox;
    private List<Trainer> trainerList;
    private final TrainerRepo trainerRepo;

    @Autowired
    public TrainersViewController(TrainerRepo trainerRepo) {
        this.trainerRepo = trainerRepo;
    }

    public void initialize(){
        this.trainerList = trainerRepo.findAll();
    }
    public void initializeCustom(List<Trainer> trainerList){
        this.trainerList = trainerList;
        initView();
    }
    public void initView(){
        for(Trainer trainer: trainerList){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/trainerProfile.fxml"));
                ConfigurableApplicationContext context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                TrainerProfileController trainerProfileController = loader.getController();
                trainerProfileController.setTrainer(trainer);
                trainersVBox.getChildren().add(trainerProfileController.getFullView());
            }catch (IOException exception){
                Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }
}
