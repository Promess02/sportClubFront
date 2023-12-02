package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import lombok.Getter;
import lombok.Setter;
import mikolaj.project.backendapp.model.MembershipType;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class MembershipTypeController {
    private MembershipType membershipType;
    @FXML private Button getBtn;
    @FXML private FlowPane sideFP;
    @FXML private FlowPane mainFP;
    @FXML private Label membershipName;
    @FXML private AnchorPane AnchorPane;

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
