package mikolajm.project.sportclubui.screenController.adminScreens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import mikolaj.project.backendapp.model.Member;
import mikolaj.project.backendapp.model.Team;
import mikolaj.project.backendapp.model.Trainer;
import mikolaj.project.backendapp.repo.MemberRepo;
import mikolajm.project.sportclubui.Util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddMemberTeamController {
    @FXML private ChoiceBox<Member> memberCb;
    @FXML private ChoiceBox<Team> teamCb;
    @FXML private Button submitBtn;
    private final MemberRepo memberRepo;
    Utils utils = new Utils();
    @Autowired
    public AddMemberTeamController(MemberRepo memberRepo) {
        this.memberRepo = memberRepo;
    }
    public void initialize(){
        List<Member> memberList = memberRepo.findAll();
        ObservableList<Member> memberItems = FXCollections.observableArrayList(memberList);
        memberCb.setItems(memberItems);
        initBtn();
    }

    public void initForTrainer(Trainer trainer){
        teamCb.setItems(FXCollections.observableArrayList(trainer.getTeam()));
        teamCb.setValue(trainer.getTeam());
    }

    private void initBtn(){
        submitBtn.setOnAction( e-> {
            if(teamCb.getValue()==null){
                handleError("choose team first");
                return;
            }
            if(memberCb.getValue()==null){
                handleError("choose member first");
                return;
            }
            Member member = memberCb.getValue();
            member.setTeam(teamCb.getValue());
            memberRepo.save(member);
            Stage stage = (Stage) submitBtn.getScene().getWindow();
            stage.close();
        });
    }

    private void handleError(String error) {
        utils.showErrorMessage(error);
    }
}
