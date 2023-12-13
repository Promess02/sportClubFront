package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import mikolaj.project.backendapp.model.MembershipType;
import mikolaj.project.backendapp.service.MembershipService;
import mikolajm.project.sportclubui.CurrentSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final CurrentSessionUser currentSessionUser;
    private final MembershipService membershipService;

    @Autowired
    public MembershipTypeController(CurrentSessionUser currentSessionUser, MembershipService membershipService) {
        this.currentSessionUser = currentSessionUser;
        this.membershipService = membershipService;
    }
    @FXML
    public void initialize(MembershipType membershipType){
        this.membershipType = membershipType;
        membershipName.setText(membershipType.getName());
        sideFP.getChildren().add(new Label("price: " + membershipType.getPrice().toString()));
        sideFP.getChildren().add(new Label("Discount: " + membershipType.getDiscount().toString()));
        sideFP.getChildren().add(new Label("months: " + membershipType.getMonths().toString()));
        sideFP.getChildren().add(new Label(membershipType.getFullAccess()?"full access to all activities":"access only to certain activities"));
        sideFP.getChildren().add(new Label(membershipType.getDescription()));
        initBtn();
    }

    private void initBtn(){
        getBtn.setOnAction(e->{
            membershipService.buyMembership(currentSessionUser.getUser().getEmail(), membershipType.getDescription(), null);
            currentSessionUser.loadMembership();
            Stage stage = (Stage) getBtn.getScene().getWindow();
            stage.close();
        });
    }



}
