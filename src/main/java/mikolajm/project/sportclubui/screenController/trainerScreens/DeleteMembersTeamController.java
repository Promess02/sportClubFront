package mikolajm.project.sportclubui.screenController.trainerScreens;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mikolaj.project.backendapp.model.Member;
import mikolaj.project.backendapp.model.Team;
import mikolaj.project.backendapp.repo.MemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DeleteMembersTeamController {
    @FXML private Label teamName;
    @FXML private ImageView teamLogo;
    @FXML private Label membersNum;
    @FXML private VBox membersColumn;
    @FXML private Button deleteBtn;

    private final MemberRepo memberRepo;
    List<Member> memberList;
    List<Member> membersToDelete;
    private Team team;

    List<DeleteMemberRowController> rows = new ArrayList<>();
    @Autowired
    public DeleteMembersTeamController(MemberRepo memberRepo) {
        this.memberRepo = memberRepo;
    }

    public void initialize(){
        initDeleteBtn();
    }
    public void setTeam(Team team){
        this.team = team;
        teamName.setText(team.getName());
        Image image;
        if(team.getLogoIconUrl()==null) image = new Image("/images/clubLogo.jpg");
        else image = new Image(team.getLogoIconUrl());
        teamLogo.setImage(image);
        loadMembers();
    }

    private void loadMembers(){
        memberList = memberRepo.findAllByTeam(team);
        membersNum.setText("Members: " + memberList.size());
        membersColumn.getChildren().clear();
        for(Member member: memberList){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/trainer/deleteMemberRow.fxml"));
                Parent root = loader.load();
                DeleteMemberRowController memberRowController = loader.getController();
                memberRowController.setMember(member);
                rows.add(memberRowController);
                membersColumn.getChildren().add(memberRowController.getFullView());
            }catch (IOException exception){
                throw new RuntimeException("unable to load member view");
            }
        }
    }

    private void initDeleteBtn(){
        deleteBtn.setOnAction( e-> {
            membersToDelete = new ArrayList<>();

            for (DeleteMemberRowController node : rows) {
                    if (node.isCheckBoxSelected()) {
                        membersToDelete.add(node.getMember());
                    }
            }
            deleteSelectedMembers();

            Stage stage = (Stage) deleteBtn.getScene().getWindow();
            stage.close();
        });
    }
    private void deleteSelectedMembers() {
        for (Member member : membersToDelete) {
            member.setTeam(null);
            memberRepo.save(member);
        }
    }
}
