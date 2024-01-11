package mikolajm.project.sportclubui.screenController.userScreens;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import mikolaj.project.backendapp.model.Member;

public class MemberRowController {
    @Getter
    @FXML private AnchorPane fullView;
    @FXML private Label nameField;
    @FXML private Label surnameField;
    @FXML private Label emailField;
    @FXML private Label phoneNumberField;
    @FXML private ImageView memberImage;

    public MemberRowController() {
    }

    public void setMember(Member member){
        nameField.setText(member.getUser().getName());
        surnameField.setText(member.getUser().getSurname());
        emailField.setText(member.getUser().getEmail());
        phoneNumberField.setText(member.getUser().getPhoneNumber());
        Image image;
        if(member.getUser().getProfileImageUrl()==null) image = new Image("/images/profileImage.png");
        else image = new Image(member.getUser().getProfileImageUrl());
        memberImage.setImage(image);
    }

}
