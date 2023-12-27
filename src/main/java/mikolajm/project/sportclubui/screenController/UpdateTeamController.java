package mikolajm.project.sportclubui.screenController;

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
import mikolajm.project.sportclubui.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class UpdateTeamController {
    @FXML private TextField teamName;
    @FXML private TextField maxNumber;
    @FXML private ChoiceBox<Sport> SportCb;
    @FXML private Button imageBtn;
    @FXML private ImageView teamImage;
    @FXML private Button submitBtn;

    private final TeamRepo teamRepo;
    private File selectedFile;
    private Utils utils = new Utils();
    private Team team;

    @Autowired
    public UpdateTeamController(TeamRepo teamRepo) {
        this.teamRepo = teamRepo;
    }

    public void initialize(){
        initChooseImg();
        initCheckBox();
        initSubmitBtn();
    }

    public void setTeam(Team team){
        this.team = team;
        teamName.setText(team.getName());
        teamName.setDisable(true);
        maxNumber.setText(team.getMaxMembers().toString());
        Image image;
        if(team.getLogoIconUrl()!=null){
            image = new Image(team.getLogoIconUrl());
            teamImage.setImage(image);
        }
        SportCb.setValue(team.getSport());

    }

    private void initChooseImg() {
        imageBtn.setOnAction(event -> {
            // Open a file chooser dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            selectedFile = fileChooser.showOpenDialog(imageBtn.getScene().getWindow());

            if (selectedFile != null) {
                // Display the selected image
                Image selectedImage = new Image(selectedFile.toURI().toString());
                teamImage.setImage(selectedImage);
                // Save the selected image to the resources/images directory
            }
        });
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
                fileName = utils.imagesPath + team.getName().replaceAll("\\s", "")+ imageExtension;
                utils.saveImage(selectedFile.toPath(), fileName);
                String dbUrl = "/images/" + team.getName().replaceAll("\\s", "")+imageExtension;
                team.setLogoIconUrl(dbUrl);
            }
            List<Team> teamList = teamRepo.findAll();
            for(Team teamDb: teamList){
                if(teamDb.getName().equals(team.getName())){
                    team.setId(teamDb.getId());
                    teamRepo.save(team);
                }
            }
            Stage stage = (Stage) submitBtn.getScene().getWindow();
            stage.close();
        });
    }

    private void initCheckBox(){
        ObservableList<Sport> list = FXCollections.observableArrayList();
        list.addAll(Sport.values());
        SportCb.setItems(list);
    }

    private void handleError(String error){
        Utils utils = new Utils();
        utils.showErrorMessage(error);
    }



}
