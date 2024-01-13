package mikolajm.project.sportclubui.screenController.generalScreens;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import mikolaj.project.backendapp.model.Location;
import mikolaj.project.backendapp.repo.LocationRepo;
import mikolajm.project.sportclubui.ClubApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class LocationsViewController {
    @FXML
    private VBox locationsVBox;
    private final LocationRepo locationRepo;

    @Autowired
    public LocationsViewController(LocationRepo locationRepo) {
        this.locationRepo = locationRepo;
    }

    public void initialize(){
        List<Location> locationList = locationRepo.findAll();
        for(Location location: locationList){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/userAndTrainer/locationView.fxml"));
                ConfigurableApplicationContext context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                LocationViewController locationViewController = loader.getController();
                locationViewController.setLocation(location);
                locationViewController.setViewOnly();
                locationsVBox.getChildren().add(locationViewController.getFullView());
            }catch (IOException exception){
                throw new RuntimeException("unable to load location view");
            }
        }
    }

}
