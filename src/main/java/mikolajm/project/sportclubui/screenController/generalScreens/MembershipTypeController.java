package mikolajm.project.sportclubui.screenController.generalScreens;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import mikolaj.project.backendapp.model.MembershipType;
import mikolaj.project.backendapp.service.MembershipService;
import mikolajm.project.sportclubui.Util.CurrentSessionUser;
import mikolajm.project.sportclubui.screenController.UtilityScreens.WarningViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

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

    public void setMembershipType(MembershipType membershipType){
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
            if(currentSessionUser.getMembership()!=null){
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/popups/warningView.fxml"));
                    Parent root = loader.load();
                    WarningViewController warningViewController = loader.getController();
                    warningViewController.initialize("You already have a membership");
                    Scene scene = new Scene(root);
                    warningViewController.getOkBtn().setOnAction(exp->{
                        saveMembership();
                        Stage stage = (Stage) warningViewController.getOkBtn().getScene().getWindow();
                        stage.close();
                    });
                    Stage primaryStage = new Stage();
                    primaryStage.setScene(scene);
                    primaryStage.show();
                }catch (IOException ex){
                    throw new RuntimeException("failed loading warning view");
                }
            }
            saveMembership();
        });
    }

    private void saveMembership(){
        membershipService.buyMembership(currentSessionUser.getUser().getEmail(),
                membershipType.getDescription(), null);
        currentSessionUser.loadMembership();
        Stage stage = (Stage) getBtn.getScene().getWindow();
        stage.close();
    }



}
