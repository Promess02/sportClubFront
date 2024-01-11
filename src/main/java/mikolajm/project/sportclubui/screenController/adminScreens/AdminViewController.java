package mikolajm.project.sportclubui.screenController.adminScreens;

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
    @FXML private MenuItem updateActivityBtn;
    @FXML private MenuItem updateTeamsBtn;
    @FXML private MenuItem addTeamsBtn;
    @FXML private MenuItem deleteTeamsBtn;
    @FXML private MenuItem deleteActivityBtn;
    @FXML private MenuItem addActivityBtn;
    @FXML private Button newsPostBtn;
    @FXML private Button addTrainerBtn;
    @FXML private Button editMembershipTypeBtn;
    @FXML private Button fireTrainerBtn;
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
        initEditLocationBtn();
        initAssignTrainerBtn();
        initFireTrainerBtn();
        initEditSocialBtn();
        initUpdateActivityBtn();
        initDeleteActivityBtn();
        initUpdateTeamsBtn();
        initDeleteTeamsBtn();
    }

    private void initNewsPostBtn(){
        newsPostBtn.setOnAction(e->{
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/addNewsPost.fxml"));
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

    private void initUpdateTeamsBtn(){
        updateTeamsBtn.setOnAction( e-> {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/editTeamsView.fxml"));
                context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                EditTeamsViewController editTeamsViewController = loader.getController();
                editTeamsViewController.setUpdateView();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }catch (IOException exception){
                throw new RuntimeException("unable to load update teams view");
            }
        });
    }

    private void initDeleteTeamsBtn(){
        deleteTeamsBtn.setOnAction( e-> {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/editTeamsView.fxml"));
                context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                EditTeamsViewController editTeamsViewController = loader.getController();
                editTeamsViewController.setDeleteView();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }catch (IOException exception){
                throw new RuntimeException("unable to load delete teams view");
            }
        });
    }

    private void initUpdateActivityBtn(){
        updateActivityBtn.setOnAction( e-> {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/editActivity.fxml"));
                context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                EditActivityController editActivityController = loader.getController();
                editActivityController.setUpdateView();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }catch (IOException exception){
                throw new RuntimeException("unable to load update activity view");
            }
        });
    }

    private void initDeleteActivityBtn(){
        deleteActivityBtn.setOnAction( e-> {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/editActivity.fxml"));
                context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                EditActivityController editActivityController = loader.getController();
                editActivityController.setDeleteView();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }catch (IOException exception){
                throw new RuntimeException("unable to load delete activity view");
            }
        });
    }



    private void initEditLocationBtn(){
        editLocationBtn.setOnAction( e->{
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/chooseLocation.fxml"));
                context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                //AddNewsPostController controller = loader.getController();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }catch (IOException exception){
                throw new RuntimeException("unable to load choose location view");
            }
        });
    }

    private void initEditSocialBtn(){
        editSocialBtn.setOnAction( e->{
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/addSocialMedia.fxml"));
                context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }catch (IOException exception){
                throw new RuntimeException("unable to load edit socials view");
            }
        });
    }
    private void initFireTrainerBtn(){
        fireTrainerBtn.setOnAction( e-> {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/fireTrainerView.fxml"));
                context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }catch (IOException exception){
                throw new RuntimeException("unable to load 'fire trainer' view");
            }
        });
    }

    private void initAssignTrainerBtn(){
        assignTrainerBtn.setOnAction( e->{
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/AssignTrainerView.fxml"));
                context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }catch (IOException exception){
                throw new RuntimeException("unable to load assign trainer view");
            }
        });
    }

    private void initEditMembershipBtn(){
        editMembershipTypeBtn.setOnAction( e->{
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/chooseMembership.fxml"));
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/addTeamBtn.fxml"));
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/addTrainerView.fxml"));
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/addActivityView.fxml"));
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
