package mikolajm.project.sportclubui.screenController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import mikolaj.project.backendapp.model.MembershipType;
import mikolaj.project.backendapp.repo.MembershipTypeRepo;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.LoginManager;
import mikolajm.project.sportclubui.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ChooseMembershipController {
    public ChoiceBox<MembershipType> membershipCheckBox;
    public Button confirmBtn;
    private final MembershipTypeRepo membershipTypeRepo;
    public Button addNewBtn;
    private ConfigurableApplicationContext context;

    @Autowired
    public ChooseMembershipController(MembershipTypeRepo membershipTypeRepo) {
        this.membershipTypeRepo = membershipTypeRepo;
    }

    public void initialize(){
        List<MembershipType> membershipTypeList = membershipTypeRepo.findAll();
        ObservableList<MembershipType> observableList = FXCollections.observableArrayList();
        observableList.addAll(membershipTypeList);
        membershipCheckBox.setItems(observableList);
        initConfirmBtn();
        initAddNewBtn();
    }

    public void initConfirmBtn(){
        confirmBtn.setOnAction( e->{
            Utils utils = new Utils();
            if(membershipCheckBox.getValue()==null) {
                utils.showErrorMessage("choose membership");
                return;
            }
            MembershipType membershipType = membershipCheckBox.getValue();
            try {
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/MembershipTypeView.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                MembershipTypeEditController membershipTypeEditController = loader.getController();
                membershipTypeEditController.setMembershipType(membershipType);
                Stage thisStage = (Stage) confirmBtn.getScene().getWindow();
                thisStage.close();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }catch (IOException ex){
                Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public void initAddNewBtn(){
        addNewBtn.setOnAction( e->{
            try {
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/admin/MembershipTypeView.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                Stage thisStage = (Stage) addNewBtn.getScene().getWindow();
                thisStage.close();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }catch (IOException ex){
                Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }


}
