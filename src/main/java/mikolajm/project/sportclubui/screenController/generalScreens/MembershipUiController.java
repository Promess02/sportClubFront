package mikolajm.project.sportclubui.screenController.generalScreens;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import mikolaj.project.backendapp.model.MembershipType;
import mikolaj.project.backendapp.repo.MembershipTypeRepo;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.LoginManager;
import mikolajm.project.sportclubui.screenController.generalScreens.MembershipTypeController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class MembershipUiController implements Initializable {
    private final MembershipTypeRepo membershipTypeRepo;
    @FXML
    public HBox membershipHBox;
    @FXML
    public ScrollPane ScrollPane;

    @Autowired
    public MembershipUiController(MembershipTypeRepo membershipTypeRepo) {
        this.membershipTypeRepo = membershipTypeRepo;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        List<MembershipType> membershipTypeList = membershipTypeRepo.findAll();

        for (MembershipType membershipType : membershipTypeList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/membershipType.fxml"));
                ConfigurableApplicationContext context = ClubApplication.getApplicationContext();
                loader.setControllerFactory(context::getBean);

                // Load the root node from the FXML file
                Parent root = loader.load();

                // Get the MembershipTypeController from the loader
                MembershipTypeController typeController = loader.getController();
                typeController.setMembershipType(membershipType); // You can initialize if needed

                // Add the AnchorPane to the HBox
                membershipHBox.getChildren().add(typeController.getAnchorPane());

            } catch (IOException ex) {
                Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
