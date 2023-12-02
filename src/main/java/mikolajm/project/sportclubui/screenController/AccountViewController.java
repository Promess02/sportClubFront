package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import mikolaj.project.backendapp.model.Member;
import org.springframework.stereotype.Component;

@Component
public class AccountViewController {
    @FXML private Button membershipStatusBtn;
    @FXML private Button creditCardBtn;
    @FXML private AnchorPane mainView;
    @FXML private ImageView userImage;
    @FXML private Label userName;
    @FXML private Label surname;
    @FXML private Label email;
    @FXML private Label phoneNumber;
    @FXML private Label team;
    @FXML private CheckBox creditCardCb;
    @FXML private CheckBox membershipStatusCb;

    public AccountViewController() {
    }

    @FXML
    public void initialize(Member member, boolean membershipStatus){
        userName.setText(member.getUser().getName());
        surname.setText(member.getUser().getSurname());
        email.setText(member.getUser().getEmail());
        phoneNumber.setText(member.getUser().getPhoneNumber());
        if(member.getTeam()==null) team.setText("not yet added to a team");
        team.setText(member.getTeam().getName());
        creditCardCb.setDisable(true);
        membershipStatusCb.setDisable(true);
        creditCardCb.setSelected(member.getUser().getCreditCard() != null);
        membershipStatusCb.setSelected(membershipStatus);
        membershipStatusBtn.setVisible(!membershipStatus);
    }
}
