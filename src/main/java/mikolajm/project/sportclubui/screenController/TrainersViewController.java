package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import lombok.Getter;
import mikolaj.project.backendapp.model.Trainer;
import mikolaj.project.backendapp.repo.TrainerRepo;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.LoginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Component
public class TrainersViewController implements Initializable {
    public VBox trainersVBox;
    private final List<Trainer> trainerList;

    @Autowired
    public TrainersViewController(TrainerRepo trainerRepo) {
        trainerList = trainerRepo.findAll();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        for(Trainer trainer: trainerList){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/trainerProfile.fxml"));
                ConfigurableApplicationContext context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                TrainerProfileController trainerProfileController = loader.getController();
                trainerProfileController.initialize(trainer);
                trainersVBox.getChildren().add(trainerProfileController.getFullView());
            }catch (IOException exception){
                Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }
}
