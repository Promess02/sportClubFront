package mikolajm.project.sportclubui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import lombok.Getter;
import mikolaj.project.backendapp.model.MembershipType;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MembershipTypeController {
    private MembershipType membershipType;
    @FXML
    public Button getBtn;
    @FXML
    public FlowPane sideFP;
    @FXML
    public FlowPane mainFP;
    @FXML
    public Label membershipName;
    @FXML
    public AnchorPane AnchorPane;

    public MembershipTypeController() {
    }
    @FXML
    public void initialize(MembershipType membershipType){
        membershipName.setText(membershipType.getName());
        sideFP.getChildren().add(new Label("price: " + membershipType.getPrice().toString()));
        sideFP.getChildren().add(new Label("Discount: " + membershipType.getDiscount().toString()));
        sideFP.getChildren().add(new Label("months: " + membershipType.getMonths().toString()));
        sideFP.getChildren().add(new Label(membershipType.getFullAccess()?"full access to all activities":"access only to certain activities"));
        sideFP.getChildren().add(new Label(membershipType.getDescription()));
    }


}
