package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import mikolaj.project.backendapp.controller.MembershipController;
import mikolaj.project.backendapp.model.Member;
import mikolaj.project.backendapp.model.User;
import mikolajm.project.sportclubui.CurrentSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    }
}
