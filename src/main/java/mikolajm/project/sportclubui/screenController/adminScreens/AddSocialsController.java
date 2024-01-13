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
import mikolaj.project.backendapp.model.Location;
import mikolaj.project.backendapp.model.SocialMedia;
import mikolaj.project.backendapp.repo.LocationRepo;
import mikolaj.project.backendapp.repo.SocialMediaRepo;
import mikolajm.project.sportclubui.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class AddSocialsController {
    @FXML private TextField socialMediaField;
    @FXML private TextField urlField;
    @FXML private ChoiceBox<Location> locationCb;
    @FXML private Button chooseImgBtn;
    @FXML private ImageView imageView;
    @FXML private Button submitBtn;

    private final LocationRepo locationRepo;
    private final SocialMediaRepo socialMediaRepo;
    private File selectedFile;
    private final Utils utils = new Utils();
    private final SocialMedia socialMedia = new SocialMedia();


    @Autowired
    public AddSocialsController(LocationRepo locationRepo, SocialMediaRepo socialMediaRepo) {
        this.locationRepo = locationRepo;
        this.socialMediaRepo = socialMediaRepo;
    }

    public void initialize(){
        initCheckBoxes();
        setImageView();
        initImageHandle();
        initSubmitBtn();
    }

    private void initCheckBoxes(){
        List<Location> list = locationRepo.findAll();
        ObservableList<Location> locations = FXCollections.observableArrayList();
        locations.addAll(list);
        locationCb.setItems(locations);
    }

    private void initImageHandle(){
        chooseImgBtn.setOnAction( e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            selectedFile = fileChooser.showOpenDialog(chooseImgBtn.getScene().getWindow());
             if(selectedFile != null){
                 if(socialMediaField.getText().isBlank()){
                     handleError("enter name first");
                     return;
                 }
                 String fileName = selectedFile.getName();
                 int dotIndex = fileName.lastIndexOf('.');
                 String name = socialMediaField.getText();
                 String imageExtension = (dotIndex > 0) ? fileName.substring(dotIndex) : "";
                 fileName = utils.imagesPath + name.replaceAll("\\s", "")+ imageExtension;
                 utils.saveImage(selectedFile.toPath(), fileName);
                 String dbUrl = "/images/" + name.replaceAll("\\s", "")+imageExtension;
                 socialMedia.setIconUrl(dbUrl);
                 setImageView();
             }
        });
    }

    private void setImageView(){
        Image image;
        if(socialMedia.getIconUrl()==null){
            image = new Image("/images/blankFile.png");
            imageView.setImage(image);
        }else {
            image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
        }
    }

    private void initSubmitBtn(){
        submitBtn.setOnAction( e->{
            if(socialMediaField.getText().isBlank()){
                handleError("enter name first");
                return;
            }else socialMedia.setName(socialMediaField.getText());
            if(urlField.getText().isBlank()){
                handleError("enter url first");
                return;
            }else socialMedia.setUrl(urlField.getText());
            if(locationCb.getValue()==null){
                handleError("enter name first");
                return;
            }else socialMedia.setLocation(locationCb.getValue());
            if(socialMedia.getIconUrl()==null){
                handleError("pleas choose image");
                return;
            }
            socialMediaRepo.save(socialMedia);
            Stage stage = (Stage) submitBtn.getScene().getWindow();
            stage.close();
        });
    }

    private void handleError(String error){
        utils.showErrorMessage(error);
    }
}
