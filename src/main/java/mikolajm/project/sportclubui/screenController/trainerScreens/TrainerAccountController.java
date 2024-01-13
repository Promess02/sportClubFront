package mikolajm.project.sportclubui.screenController.trainerScreens;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mikolaj.project.backendapp.model.Trainer;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.CurrentSessionUser;
import mikolajm.project.sportclubui.Utils;
import mikolajm.project.sportclubui.screenController.generalScreens.TeamsViewController;
import mikolajm.project.sportclubui.screenController.userScreens.CreditCardController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class TrainerAccountController {
    @FXML private ImageView userImage;
    @FXML private Button addImageBtn;
    @FXML private Label userName;
    @FXML private Label surname;
    @FXML private Label email;
    @FXML private Label phoneNumber;
    @FXML private Label gradeField;
    @FXML private CheckBox creditCardCb;
    @FXML private Label specializationField;
    @FXML private Button viewTeamsBtn;
    @FXML private Button creditCardBtn;

    private final CurrentSessionUser currentSessionUser;
    private Trainer trainer;
    private Image accountImage;
    Utils utils = new Utils();
    private File selectedFile;
    private ConfigurableApplicationContext context;

    @Autowired
    public TrainerAccountController(CurrentSessionUser currentSessionUser) {
        this.currentSessionUser = currentSessionUser;
    }

    public void initialize(){
        trainer = currentSessionUser.getTrainer();
        if(trainer==null) {
            Stage stage = (Stage) addImageBtn.getScene().getWindow();
            stage.close();
            handleError("user loaded not a trainer");
            return;
        }
        initView();
        initAddImageBtn();
        initImage();
        initCreditCardBtn();
        initTeamsBtn();
    }

    private void initImage(){
        Image image;
        if(currentSessionUser.getUser().getProfileImageUrl()==null){
            image = new Image("/images/profileImage.png", false);
        }else{
            if(accountImage==null) image = new Image(currentSessionUser.getUser().getProfileImageUrl(), false);
            else image = accountImage;
            addImageBtn.setText("Change image");
        }
        userImage.setImage(image);
    }

    private void initTeamsBtn(){
        viewTeamsBtn.setOnAction( e->{
            try{
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/user/teamsView.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                TeamsViewController teamsViewController = loader.getController();
                teamsViewController.initForATrainer(trainer);
                Scene scene = new Scene(root);
                Stage primaryStage = new Stage(); // You might use your existing primaryStage here
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch (IOException exception){
                throw new RuntimeException("failed loading teams view");
            }
        });
    }
    private void initCreditCardBtn(){
        creditCardBtn.setOnAction(e ->{
            try{
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/user/creditCardView.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                CreditCardController creditCardController = loader.getController();
                if(currentSessionUser.getUser().getCreditCard()!=null)
                    creditCardController.setCreditCard(currentSessionUser.getUser().getCreditCard());
                Scene scene = new Scene(root);
                Stage primaryStage = new Stage();
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch(IOException exception){
                throw new RuntimeException("unable to load membership type view");
            }
        });
    }

    private void initView(){
        Image image;
        if(trainer.getUser().getProfileImageUrl()==null) image = new Image("/images/profileImage.png");
        else image = new Image(trainer.getUser().getProfileImageUrl());
        userImage.setImage(image);
        userName.setText(trainer.getUser().getName());
        surname.setText(trainer.getUser().getSurname());
        email.setText(trainer.getUser().getEmail());
        phoneNumber.setText(trainer.getUser().getPhoneNumber());
        gradeField.setText(trainer.getGrade().toString());
        if(trainer.getUser().getCreditCard()!=null){
            creditCardCb.setSelected(true);
            creditCardCb.setText("show card");
        }
        specializationField.setText(trainer.getSpecialization());
    }


    private void initAddImageBtn(){
        addImageBtn.setOnAction(event -> {
            // Open a file chooser dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            selectedFile = fileChooser.showOpenDialog(addImageBtn.getScene().getWindow());

            if (selectedFile != null) {
                Image selectedImage = new Image(selectedFile.toURI().toString());
                userImage.setImage(selectedImage);
                accountImage = selectedImage;
                setImage();
            }
        });
    }

    private void setImage(){
        String fileName = selectedFile.getName();
        int dotIndex = fileName.lastIndexOf('.');
        String name = currentSessionUser.getUser().toString();
        String imageExtension = (dotIndex > 0) ? fileName.substring(dotIndex) : "";
        fileName = utils.imagesPath + name.replaceAll("\\s", "")+ imageExtension;
        utils.saveImage(selectedFile.toPath(), fileName);
        String dbUrl = "/images/" + name.replaceAll("\\s", "")+imageExtension;
        currentSessionUser.updateUserImage(dbUrl);
    }

    private void handleError(String error){
        utils.showErrorMessage(error);
    }


}
