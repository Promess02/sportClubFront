package mikolajm.project.sportclubui.screenController.adminScreens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import mikolaj.project.backendapp.model.Location;
import mikolaj.project.backendapp.repo.LocationRepo;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.Utils;
import mikolajm.project.sportclubui.screenController.generalScreens.LocationViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ChooseLocationController {
    @FXML private ChoiceBox<Location> locationCheckBox;
    @FXML private Button confirmBtn;
    @FXML private Button addNewBtn;
    private final LocationRepo locationRepo;
    private ConfigurableApplicationContext context;
    @Autowired
    public ChooseLocationController(LocationRepo locationRepo) {
        this.locationRepo = locationRepo;
    }

    public void initialize(){
        List<Location> locationList = locationRepo.findAll();
        ObservableList<Location> locationItems = FXCollections.observableArrayList();
        locationItems.addAll(locationList);
        locationCheckBox.setItems(locationItems);
        initConfirmBtn();
        initAddNewBtn();
    }

    public void initConfirmBtn(){
        confirmBtn.setOnAction( e->{
            if(locationCheckBox.getValue()==null){
                handleError("set location first");
                return;
            }
            Location location = locationCheckBox.getValue();
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/userAndTrainer/locationView.fxml"));
                context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                LocationViewController locationViewController = loader.getController();
                locationViewController.setLocation(location);
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();

                Stage primaryStage = (Stage) confirmBtn.getScene().getWindow();
                primaryStage.close();
            }catch (IOException exception){
                throw new RuntimeException("unable to load location view");
            }

        });
    }

    public void initAddNewBtn(){
        addNewBtn.setOnAction( e->{
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/userAndTrainer/locationView.fxml"));
                context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();

                Stage primaryStage = (Stage) confirmBtn.getScene().getWindow();
                primaryStage.close();
            }catch (IOException exception){
                throw new RuntimeException("unable to load location view");
            }
        });
    }

    private void handleError(String error){
        Utils utils = new Utils();
        utils.showErrorMessage(error);
    }


}
