package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import mikolaj.project.backendapp.model.Member;

import java.util.List;

public class DeleteMemberRowController extends AnchorPane{
    @FXML private Label nameField;
    @FXML private Label surnameField;
    @FXML private Label emailField;
    @FXML private Label phoneNumberField;
    @FXML private ImageView memberImage;
    @Getter
    @FXML private CheckBox checkBox;
    @Getter
    @FXML private AnchorPane fullView;
    @Getter
    private Member member;


    public DeleteMemberRowController() {
    }

    public void setMember(Member member){
        this.member = member;
        nameField.setText(member.getUser().getName());
        surnameField.setText(member.getUser().getSurname());
        emailField.setText(member.getUser().getEmail());
        phoneNumberField.setText(member.getUser().getPhoneNumber());
        Image image;
        if(member.getUser().getProfileImageUrl()==null) image = new Image("/images/profileImage.png");
        else image = new Image(member.getUser().getProfileImageUrl());
        memberImage.setImage(image);
    }

    public boolean isCheckBoxSelected(){
        return checkBox.isSelected();
    }

}
