package mikolajm.project.sportclubui;

import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.layout.FlowPane;
import mikolaj.project.backendapp.model.MembershipType;
import mikolaj.project.backendapp.repo.MembershipTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MembershipUiController {
    @FXML
    public FlowPane membershipFlowPane;
    private MembershipTypeRepo membershipTypeRepo;

    @Autowired
    public MembershipUiController(MembershipTypeRepo membershipTypeRepo) {
        List<MembershipType> membershipTypeList = membershipTypeRepo.findAll();
        initializeMembershipTypes();
    }

    private void initializeMembershipTypes() {
        List<MembershipType> membershipTypeList = membershipTypeRepo.findAll();
        membershipFlowPane.setOrientation(Orientation.HORIZONTAL);
        membershipFlowPane.setHgap(10);
        for (MembershipType membershipType : membershipTypeList) {
            MembershipTypeController typeController = new MembershipTypeController(membershipType);
            membershipFlowPane.getChildren().add(typeController.getMainFP());
        }
    }
}
