package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import mikolaj.project.backendapp.model.Trainer;
import mikolaj.project.backendapp.repo.MemberRepo;
import mikolaj.project.backendapp.repo.TeamRepo;
import mikolaj.project.backendapp.service.TrainerService;
import mikolajm.project.sportclubui.CurrentSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CurrencyEditor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TrainerProfileController {
    @Getter
    public AnchorPane fullView;
    @FXML private Label grade;
    @FXML private Label trainerName;
    @FXML private Label trainerSurname;
    @FXML private Label email;
    @FXML private ImageView imageView;
    @FXML private Label specializationLabel;
    @FXML private Button viewTeamsBtn;
    @FXML private ChoiceBox<Object> starRatingChoiceBox;
    @FXML private Button saveGradeBtn;
    private Trainer trainer;
    private final MemberRepo memberRepo;
    private final TrainerService trainerService;
    private final CurrentSessionUser currentSessionUser;
    @Autowired
    public TrainerProfileController(TrainerService trainerService,
                                    MemberRepo memberRepo,
                                    CurrentSessionUser currentSessionUser) {
        this.trainerService = trainerService;
        this.memberRepo = memberRepo;
        this.currentSessionUser = currentSessionUser;
    }

    public void initialize(Trainer trainer){
        this.trainer = trainer;
        trainerName.setText(trainer.getUser().getName());
        trainerSurname.setText(trainer.getUser().getSurname());
        email.setText(trainer.getUser().getEmail());
        Image image = new Image("/images/profileImage.png");
        imageView.setImage(image);
        specializationLabel.setText(trainer.getSpecialization());
        grade.setText("Trainer grade: " + trainer.getGrade());
        initTeamsBtn();
        initSaveGradeBtn();
    }

    public void initTeamsBtn(){
        viewTeamsBtn.setOnAction( e->{
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/TeamView.fxml"));
                Parent root = loader.load();
                TeamView teamView = loader.getController();
                int currentTeamMembers = memberRepo.findAllByTeam(trainer.getTeam()).size();
                teamView.initialize(trainer.getTeam(), currentTeamMembers);
                Scene scene = new Scene(root);
                Stage primaryStage = new Stage(); // You might use your existing primaryStage here
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch (IOException exception){
                throw new RuntimeException("failed loading teams view");
            }
        });
    }

    public void initSaveGradeBtn(){
        saveGradeBtn.setOnAction(e->{
            String stars = starRatingChoiceBox.getValue().toString();
            byte grade = 0;
            switch(stars){
                case "1 Star" -> grade =  1;
                case "2 Stars" -> grade = 2;
                case "3 Stars" -> grade = 3;
                case "4 Stars" -> grade = 4;
                case "5 Stars" -> grade = 5;
            }
            trainerService.gradeTrainer(trainer,grade,currentSessionUser.getMember());
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/errorMsg.fxml"));
                Parent root = loader.load();
                ErrorMessageController errorMessageController = loader.getController();
                errorMessageController.setError("trainer grade saved");
                Scene scene = new Scene(root);
                Stage primaryStage = new Stage();
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch (IOException exception){
                throw new RuntimeException("unable to load waring view");
            }
        });
    }

}
