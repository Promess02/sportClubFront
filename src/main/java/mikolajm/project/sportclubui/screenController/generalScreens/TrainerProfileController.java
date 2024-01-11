package mikolajm.project.sportclubui.screenController.generalScreens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import mikolaj.project.backendapp.enums.StarRating;
import mikolaj.project.backendapp.model.Trainer;
import mikolaj.project.backendapp.repo.MemberRepo;
import mikolaj.project.backendapp.service.TrainerService;
import mikolajm.project.sportclubui.ClubApplication;
import mikolajm.project.sportclubui.CurrentSessionUser;
import mikolajm.project.sportclubui.Utils;
import mikolajm.project.sportclubui.screenController.ErrorMessageController;
import mikolajm.project.sportclubui.screenController.generalScreens.MailWindow;
import mikolajm.project.sportclubui.screenController.generalScreens.TeamsViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TrainerProfileController {
    @Getter
    public AnchorPane fullView;
    @FXML private Button sendEmailBtn;
    @FXML private Label grade;
    @FXML private Label trainerName;
    @FXML private Label trainerSurname;
    @FXML private Label email;
    @FXML private ImageView imageView;
    @FXML private Label specializationLabel;
    @FXML private Button viewTeamsBtn;
    @FXML private ChoiceBox<StarRating> starRatingChoiceBox;
    @FXML private Button saveGradeBtn;
    private Trainer trainer;
    private final MemberRepo memberRepo;
    private final TrainerService trainerService;
    private final CurrentSessionUser currentSessionUser;
    private ConfigurableApplicationContext context;
    @Autowired
    public TrainerProfileController(TrainerService trainerService,
                                    MemberRepo memberRepo,
                                    CurrentSessionUser currentSessionUser) {
        this.trainerService = trainerService;
        this.memberRepo = memberRepo;
        this.currentSessionUser = currentSessionUser;
    }

    public void initialize(){
        initChoiceBox();
        initTeamsBtn();
        initSaveGradeBtn();
        initMailBtn();
    }

    public void setTrainer(Trainer trainer){
        this.trainer = trainer;
        trainerName.setText(trainer.getUser().getName());
        trainerSurname.setText(trainer.getUser().getSurname());
        email.setText(trainer.getUser().getEmail());
        Image image;
        if(trainer.getUser().getProfileImageUrl()==null)
        image = new Image("/images/profileImage.png");
        else image = new Image(trainer.getUser().getProfileImageUrl());
        imageView.setImage(image);
        specializationLabel.setText(trainer.getSpecialization());
        grade.setText("Trainer grade: " + trainer.getGrade());
    }

    private void initChoiceBox(){
        ObservableList<StarRating> items = FXCollections.observableArrayList();
        items.addAll(StarRating.values());
        starRatingChoiceBox.setItems(items);
    }

    public void initTeamsBtn(){
        viewTeamsBtn.setOnAction( e->{
            try{
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/teamsView.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                TeamsViewController teamsViewController = loader.getController();
                teamsViewController.initForATrainer(trainer);
                Scene scene = new Scene(root);
                Stage primaryStage = new Stage();
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch (IOException exception){
                throw new RuntimeException("failed loading teams view");
            }
        });
    }

    private void initMailBtn(){
        sendEmailBtn.setOnAction( e-> {
            try{
                context = ClubApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/mailWindow.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                MailWindow mailWindow = loader.getController();
                mailWindow.setTargetEmail(trainer.getUser().getEmail());
                Scene scene = new Scene(root);
                Stage primaryStage = new Stage();
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch (IOException exception){
                throw new RuntimeException("failed loading mail view");
            }
        });
    }

    public void initSaveGradeBtn(){
        saveGradeBtn.setOnAction(e->{
            StarRating starRating = starRatingChoiceBox.getValue();
            Utils utils = new Utils();
            if(starRating == null){
                utils.showErrorMessage("star rating not chosen");
                return;
            }
            byte grade = starRating.getGrade();
            if(grade==0){
                utils.showErrorMessage("star rating is zero");
                return;
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
                throw new RuntimeException("unable to load warning view");
            }
        });
    }

}
