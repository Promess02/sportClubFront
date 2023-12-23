package mikolajm.project.sportclubui.screenController;

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
import mikolaj.project.backendapp.model.Member;
import mikolaj.project.backendapp.model.User;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.CurrentSessionUser;
import mikolajm.project.sportclubui.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class AccountViewController {
    @FXML private Label membershipName;
    @FXML private Button addImageBtn;
    @FXML private Button membershipStatusBtn;
    @FXML private Button creditCardBtn;
    @FXML private ImageView userImage;
    @FXML private Label userName;
    @FXML private Label surname;
    @FXML private Label email;
    @FXML private Label phoneNumber;
    @FXML private Label team;
    @FXML private CheckBox creditCardCb;

    private final CurrentSessionUser currentSessionUser;
    private ConfigurableApplicationContext context;
    private File selectedFile;
    @Autowired
    public AccountViewController(CurrentSessionUser currentSessionUser) {
        this.currentSessionUser = currentSessionUser;
    }

    @FXML
    public void initialize(){
        User user = currentSessionUser.getUser();
        Member member = currentSessionUser.getMember();
        userName.setText(user.getName());
        surname.setText(user.getSurname());
        email.setText(user.getEmail());
        phoneNumber.setText(user.getPhoneNumber());
        if(member==null || member.getTeam()==null) team.setText("not yet added to a team");
        else team.setText(member.getTeam().getName());
        //disable checkboxes
        creditCardCb.setDisable(true);
        creditCardCb.setSelected(user.getCreditCard() != null);
        if(user.getCreditCard()!=null) {
            creditCardBtn.setText("show card");
        }

        if(currentSessionUser.isMembershipStatus()) {
            membershipStatusBtn.setText("show membership");
            membershipName.setText(currentSessionUser.getMembership().getMembershipType().getName());
        }else membershipName.setText("not active");
        Image image;
        if(currentSessionUser.getUser().getProfileImageUrl()==null){
            image = new Image("/images/profileImage.png");
        }else{
            image = new Image(currentSessionUser.getUser().getProfileImageUrl());
            addImageBtn.setText("Change image");
        }
        userImage.setImage(image);
        initMembershipBtn();
        initCreditCardBtn();
        initAddImageBtn();
    }

    private void initAddImageBtn(){
        addImageBtn.setOnAction( e->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            selectedFile = fileChooser.showOpenDialog(addImageBtn.getScene().getWindow());

            if (selectedFile != null) {
                // Display the selected imag
                Utils utils = new Utils();
                String fileName = selectedFile.getName();
                int dotIndex = fileName.lastIndexOf('.');
                String name = currentSessionUser.getUser().toString();
                String imageExtension = (dotIndex > 0) ? fileName.substring(dotIndex) : "";
                fileName = "/home/mikolajmichalczyk/IdeaProjects/sportClub/sportClubUi/src/main/resources/images/" + name.replaceAll("\\s", "")+ imageExtension;
                utils.saveImage(selectedFile.toPath(), fileName);
                String dbUrl = "/images/" + name.replaceAll("\\s", "")+imageExtension;
                currentSessionUser.updateUserImage(dbUrl);
                userImage.setImage(new Image(dbUrl));
                Stage stage = (Stage) addImageBtn.getScene().getWindow();
                stage.close();
                //addImageBtn.setVisible(false);
                // Save the selected image to the resources/images directory
//                utils.saveImage(selectedFile.toPath(), "resources/images/" + selectedFile.getName());
            }
        });

    }

    private void initCreditCardBtn(){
        creditCardBtn.setOnAction(e ->{
            try{
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/creditCardView.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                CreditCardController creditCardController = loader.getController();
                if(currentSessionUser.getUser().getCreditCard()!=null) creditCardController.setCreditCard(currentSessionUser.getUser().getCreditCard());
                Scene scene = new Scene(root);
                // Set the Scene to the primaryStage or a new Stage
                Stage primaryStage = new Stage(); // You might use your existing primaryStage here
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch(IOException exception){
                throw new RuntimeException("unable to load membership type view");
            }
        });
    }

    private void initMembershipBtn(){
        membershipStatusBtn.setOnAction( e-> {
            try{
                if(currentSessionUser.getUser().getCreditCard()==null) {
                    Utils utils = new Utils();
                    utils.showErrorMessage("credit card info not provided");
                    return;
                }
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/membership.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                MembershipUiController membershipUiController = loader.getController();
                Scene scene = new Scene(root);
                // Set the Scene to the primaryStage or a new Stage
                Stage primaryStage = new Stage(); // You might use your existing primaryStage here
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch(IOException exception){
                throw new RuntimeException("unable to load membership type view");
            }
        });
    }
}
