package mikolajm.project.sportclubui.screenController.adminScreens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mikolaj.project.backendapp.enums.Sport;
import mikolaj.project.backendapp.model.Team;
import mikolaj.project.backendapp.repo.TeamRepo;
import mikolajm.project.sportclubui.Util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class AddTeamController {

    @FXML private Button submitBtn;
    @FXML private TextField teamName;
    @FXML private TextField maxNumber;
    @FXML private ChoiceBox<Sport> SportCb;
    @FXML private Button imageBtn;
    @FXML private ImageView teamImage;
    private final TeamRepo teamRepo;
    private File selectedFile;

    @Autowired
    public AddTeamController(TeamRepo teamRepo) {
     this.teamRepo = teamRepo;
    }

    public void initialize() {
        initChooseImg();
        initCheckBox();
        initSubmitBtn();
    }

    private void initChooseImg() {
        imageBtn.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            selectedFile = fileChooser.showOpenDialog(imageBtn.getScene().getWindow());

            if (selectedFile != null) {
                Image selectedImage = new Image(selectedFile.toURI().toString());
                teamImage.setImage(selectedImage);
            }
        });
    }

    private void initCheckBox(){
        ObservableList<Sport> list = FXCollections.observableArrayList();
        list.addAll(Sport.values());
        SportCb.setItems(list);
    }

    private void initSubmitBtn(){
        submitBtn.setOnAction( e->{
            Utils utils = new Utils();
            Team team = new Team();
            if (teamName.getText().isBlank()) {
                handleError("please enter a team name");
                return;
            } else team.setName(teamName.getText());
            if (SportCb.getValue()==null) {
                handleError("please enter a team name");
                return;
            } else team.setSport(SportCb.getValue());
            if (maxNumber.getText().isBlank() || !maxNumber.getText().matches("\\d+")){
                handleError("enter valid max member number");
                return;
            }else {
                try{
                    int maxMembers = Integer.parseInt(maxNumber.getText());
                    team.setMaxMembers(maxMembers);
                } catch (NumberFormatException exception){
                    team.setMaxMembers(Integer.getInteger(maxNumber.getText()));
                    return;
                }
            }

            if(SportCb.getValue()==null){
                handleError("please enter Sport");
                return;
            }else team.setSport(SportCb.getValue());

            if(teamImage.getImage()!=null){
                String fileName = selectedFile.getName();
                int dotIndex = fileName.lastIndexOf('.');
                String imageExtension = (dotIndex > 0) ? fileName.substring(dotIndex) : "";
                fileName = "/home/mikolajmichalczyk/IdeaProjects/sportClub/sportClubUi/src/main/resources/images/"
                        + team.getName().replaceAll("\\s", "")+ imageExtension;
                utils.saveImage(selectedFile.toPath(), fileName);
                String dbUrl = "/images/" + team.getName().replaceAll("\\s", "")+imageExtension;
                team.setLogoIconUrl(dbUrl);
            }
            teamRepo.save(team);

            Stage stage = (Stage) submitBtn.getScene().getWindow();
            stage.close();
        });
    }

    private void handleError(String error){
        Utils utils = new Utils();
        utils.showErrorMessage(error);
    }
}
