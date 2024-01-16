package mikolajm.project.sportclubui.screenController.generalScreens;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mikolaj.project.backendapp.model.MembershipType;
import mikolaj.project.backendapp.repo.MembershipTypeRepo;
import mikolajm.project.sportclubui.Util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MembershipTypeEditController {
    @FXML private TextField membershipName;
    @FXML private TextField monthsField;
    @FXML private TextField priceField;
    @FXML private TextField discountField;
    @FXML private CheckBox accessCb;
    @FXML private TextArea descriptionArea;
    @FXML private Button submitBtn;
    @FXML private Button deleteBtn;

    private final MembershipTypeRepo membershipTypeRepo;
    private MembershipType membershipType;

    @Autowired
    public MembershipTypeEditController(MembershipTypeRepo membershipTypeRepo) {
        this.membershipTypeRepo = membershipTypeRepo;
    }

    public void initialize(){
        initSubmitBtn();
        initDeleteBtn();
    }

    public void setMembershipType(MembershipType membershipType){
        this.membershipType = membershipType;
        membershipName.setText(membershipType.getName());
        monthsField.setText(membershipType.getMonths().toString());
        priceField.setText(membershipType.getPrice().toString());
        discountField.setText(membershipType.getDiscount().toString());
        accessCb.setSelected(membershipType.getFullAccess());
        descriptionArea.setText(membershipType.getDescription());
    }

    private void initSubmitBtn(){
        Utils utils = new Utils();
        if(membershipType==null) membershipType = new MembershipType();
        submitBtn.setOnAction(e->{
            if(membershipName.getText().isBlank()){
                utils.showErrorMessage("give name");
                return;
            }else membershipType.setName(membershipName.getText());

            if(monthsField.getText().isBlank() || !monthsField.getText().matches("\\d+")){
                utils.showErrorMessage("give months");
                return;
            }else membershipType.setMonths((byte) Integer.parseInt(monthsField.getText()));

            if(priceField.getText().isBlank() || !priceField.getText().matches("\\d+\\.\\d+")){
                utils.showErrorMessage("give price");
                return;
            }else membershipType.setPrice(Double.valueOf(priceField.getText()));

            if(discountField.getText().isBlank()|| !discountField.getText().matches("\\d+\\.\\d+")){
                utils.showErrorMessage("give discount");
                return;
            }else membershipType.setDiscount(Double.valueOf(discountField.getText()));

            if(descriptionArea.getText()!=null) membershipType.setDescription(descriptionArea.getText());

            membershipType.setFullAccess(accessCb.isSelected());

            membershipTypeRepo.save(membershipType);
            Stage thisStage = (Stage) submitBtn.getScene().getWindow();
            thisStage.close();
        });
    }
    private void initDeleteBtn(){
        deleteBtn.setOnAction( e-> {
            if(membershipType!=null) membershipTypeRepo.delete(membershipType);
            Stage stage = (Stage) deleteBtn.getScene().getWindow();
            stage.close();
        });
    }
}
