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
import mikolaj.project.backendapp.model.Activity;
import mikolaj.project.backendapp.repo.ActivityRepo;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EditActivityController {
    @FXML private Label label;
    @FXML private ChoiceBox<Activity> activityCb;
    @FXML private Button submitBtn;
    private  List<Activity> activityList;

    private final ActivityRepo activityRepo;

    private ConfigurableApplicationContext context;

    @Autowired
    public EditActivityController(ActivityRepo activityRepo) {
        this.activityRepo = activityRepo;
    }

    public void initialize(){
        activityList = activityRepo.findAll();
        initBoxes();
    }

    public void setDeleteView(){
        label.setText("Choose activity to delete");
        submitBtn.setText("delete");

        submitBtn.setOnAction( e-> {
            if(activityCb.getValue()==null) {
                handleError("choose activity first");
                return;
            }
            for(Activity activity: activityList){
                if(activity.getName().equals(activityCb.getValue().getName()))
                    activityRepo.delete(activity);
            }
            Stage stage = (Stage) submitBtn.getScene().getWindow();
            stage.close();
        });
    }

    public void setUpdateView(){
        label.setText("Choose activity to update");
        submitBtn.setText("update");

        submitBtn.setOnAction( e-> {
            if(activityCb.getValue()==null) {
                handleError("choose activity first");
                return;
            }
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/updateActivityView.fxml"));
                context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                UpdateActivityController updateActivityController = loader.getController();
                updateActivityController.setActivity(activityCb.getValue());
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

    private void initBoxes(){
        List<Activity> activityList = activityRepo.findAll();
        Set<Activity> set = new HashSet<>(activityList.stream()
                .collect(Collectors.toMap(Activity::getName, activity -> activity, (a, b) -> a))
                .values());
        List<Activity> distinctActivities = set.stream().toList();
        ObservableList<Activity> activities = FXCollections.observableArrayList();
        activities.addAll(distinctActivities);
        activityCb.setItems(activities);
    }

    private void handleError(String error){
        Utils utils = new Utils();
        utils.showErrorMessage(error);
    }
}
