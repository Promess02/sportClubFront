package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import mikolajm.project.sportclubui.ClubApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class AdminViewController {
    @FXML private MenuItem addTeamsBtn;
    @FXML private MenuItem deleteTeamsBtn;
    //@FXML private MenuItem editActivityBtn;
    @FXML private MenuItem deleteActivityBtn;
    @FXML private MenuItem addActivityBtn;
    @FXML private Button newsPostBtn;
    @FXML private Button addTrainerBtn;
    @FXML private Button editActivitiesBtn;
    @FXML private Button editMembershipTypeBtn;
    @FXML private Button fireTrainerBtn;
    //@FXML private Button editTeamsBtn;

    @FXML private Button assignTrainerBtn;
    @FXML private Button editLocationBtn;
    @FXML private Button editSocialBtn;
    private ConfigurableApplicationContext context;

    public AdminViewController() {
    }
    @FXML
    public void initialize(){
        initNewsPostBtn();
        initAddTrainerBtn();
        initAddActivityBtn();
        initAddTeamsBtn();
        initEditMembershipBtn();
    }

    private void initNewsPostBtn(){
        newsPostBtn.setOnAction(e->{
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/addNewsPost.fxml"));
                context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                //AddNewsPostController controller = loader.getController();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }catch (IOException exception){
                throw new RuntimeException("unable to load add news post view");
            }
        });
    }

    private void initDeleteActivityBtn(){

    }

    private void initEditMembershipBtn(){
        editMembershipTypeBtn.setOnAction( e->{
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/chooseMembership.fxml"));
                context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                //AddNewsPostController controller = loader.getController();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }catch (IOException exception){
                throw  new RuntimeException("unable to load membership type view");
            }
        });
    }

    private void initAddTeamsBtn(){
        addTeamsBtn.setOnAction( e->{
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/addTeamBtn.fxml"));
                context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                //AddNewsPostController controller = loader.getController();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }catch (IOException exception){
                throw new RuntimeException("unable to load add teams view");
            }
        });
    }

    private void initAddTrainerBtn(){
        addTrainerBtn.setOnAction(e->{
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/addTrainerView.fxml"));
                context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                //AddNewsPostController controller = loader.getController();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }catch (IOException exception){
                throw new RuntimeException("unable to load add news post view");
            }
        });
    }

    private void initAddActivityBtn(){
        addActivityBtn.setOnAction( e-> {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/addActivityView.fxml"));
                context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                //AddNewsPostController controller = loader.getController();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }catch (IOException exception){
                throw new RuntimeException("unable to load add activity view");
            }
        });
    }


}