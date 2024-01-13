package mikolajm.project.sportclubui.screenController.trainerScreens;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import mikolaj.project.backendapp.model.Member;
import mikolaj.project.backendapp.model.Team;
import mikolaj.project.backendapp.repo.MemberRepo;
import mikolajm.project.sportclubui.screenController.userScreens.MemberRowController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ShowTeamMembersController {
    @FXML private Label teamName;
    @FXML private ImageView teamLogo;
    @FXML private Label membersNum;
    @FXML private VBox membersColumn;
    private final MemberRepo memberRepo;
    private Team team;
    @Autowired
    public ShowTeamMembersController(MemberRepo memberRepo) {
        this.memberRepo = memberRepo;
    }
    public void initialize(){
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
        List<Member> memberList = memberRepo.findAllByTeam(team);
        membersNum.setText("Members: " + memberList.size());
        for(Member member: memberList){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/trainer/MemberRow.fxml"));
                Parent root = loader.load();
                MemberRowController memberRowController = loader.getController();
                memberRowController.setMember(member);
                membersColumn.getChildren().add(memberRowController.getFullView());
            }catch (IOException exception){
                throw new RuntimeException("unable to load member view");
            }

        }
    }
}
