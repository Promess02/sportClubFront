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
import javafx.stage.Stage;
import mikolaj.project.backendapp.model.Member;
import mikolaj.project.backendapp.model.User;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.CurrentSessionUser;
import mikolajm.project.sportclubui.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AccountViewController {
    @FXML private Button membershipStatusBtn;
    @FXML private Button creditCardBtn;
    @FXML private ImageView userImage;
    @FXML private Label userName;
    @FXML private Label surname;
    @FXML private Label email;
    @FXML private Label phoneNumber;
    @FXML private Label team;
    @FXML private CheckBox creditCardCb;
    @FXML private CheckBox membershipStatusCb;

    private final CurrentSessionUser currentSessionUser;
    private ConfigurableApplicationContext context;
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
        membershipStatusCb.setDisable(true);
        creditCardCb.setSelected(user.getCreditCard() != null);
        creditCardBtn.setVisible(false);
        if(user.getCreditCard()==null) creditCardBtn.setVisible(true);
        membershipStatusCb.setSelected(currentSessionUser.isMembershipStatus());
        membershipStatusBtn.setVisible(!currentSessionUser.isMembershipStatus());
        Image image = new Image("/images/profileImage.png");
        userImage.setImage(image);
        initMembershipBtn();
        initCreditCardBtn();
    }

    private void initCreditCardBtn(){
        creditCardBtn.setOnAction(e ->{
            try{
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/creditCardView.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                CreditCardController creditCardController = loader.getController();
                creditCardController.initialize();
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
