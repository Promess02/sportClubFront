package mikolajm.project.sportclubui.screenController.adminScreens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import mikolaj.project.backendapp.DTO.ServiceResponse;
import mikolaj.project.backendapp.enums.Sport;
import mikolaj.project.backendapp.model.Activity;
import mikolaj.project.backendapp.model.Location;
import mikolaj.project.backendapp.model.Team;
import mikolaj.project.backendapp.model.Trainer;
import mikolaj.project.backendapp.repo.LocationRepo;
import mikolaj.project.backendapp.repo.TeamRepo;
import mikolaj.project.backendapp.repo.TrainerRepo;
import mikolaj.project.backendapp.service.ActivityService;
import mikolajm.project.sportclubui.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AddActivityViewController {
    public TextField weeksTF;
    @FXML private ImageView imageView;
    @FXML private Button addImageBtn;
    @FXML private HBox trainerHBox;
    @FXML private HBox minutesHBox;
    @FXML private HBox membersHBox;
    @FXML private TextField activityName;
    @FXML private ChoiceBox<Sport> sportCb;
    @FXML private ChoiceBox<Location> locationCb;
    @FXML private ChoiceBox<Team> TeamCb;
    @FXML private ChoiceBox<Trainer> TrainerCb;
    @FXML private DatePicker datePicker;
    private Slider minutesSlider;
    private Slider membersSlider;
    @FXML private TextField timeField;
    @FXML private TextArea descriptionArea;
    @FXML private Button createBtn;

    private final LocationRepo locationRepo;
    private final TeamRepo teamRepo;
    private final TrainerRepo trainerRepo;
    private final ActivityService activityService;
    private File selectedFile;
    private Trainer trainer;
    Utils utils = new Utils();
    @Autowired
    public AddActivityViewController(LocationRepo locationRepo, TrainerRepo trainerRepo, TeamRepo teamRepo,
                                      ActivityService activityService) {
        this.locationRepo = locationRepo;
        this.teamRepo = teamRepo;
        this.trainerRepo = trainerRepo;
        this.activityService = activityService;
    }

    public void initialize(){
        initCheckBoxes();
        initAddImageBtn();
        initSliders();
        initButton();
    }

    private void initCheckBoxes(){
        List<Location> locationList = locationRepo.findAll();
        List<Team> teamList = teamRepo.findAll();
        List<Trainer> trainerList = trainerRepo.findAll();

        ObservableList<Location> locationItems = FXCollections.observableArrayList();
        ObservableList<Team> teamItems = FXCollections.observableArrayList();
        ObservableList<Trainer> trainerItems = FXCollections.observableArrayList();
        ObservableList<Sport> sportItems = FXCollections.observableArrayList();

        locationItems.addAll(locationList);
        teamItems.addAll(teamList);
        trainerItems.addAll(trainerList);
        sportItems.addAll(Sport.values());

        locationCb.setItems(locationItems);
        TeamCb.setItems(teamItems);
        TrainerCb.setItems(trainerItems);
        sportCb.setItems(sportItems);
    }

    private void initAddImageBtn(){
        addImageBtn.setOnAction( e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            selectedFile = fileChooser.showOpenDialog(addImageBtn.getScene().getWindow());
            if (selectedFile != null) {
                Image selectedImage = new Image(selectedFile.toURI().toString());
                imageView.setImage(selectedImage);
            }
        });
    }

    public void setTrainer(Trainer trainer){
        trainerHBox.setVisible(false);
        this.trainer = trainer;
        TeamCb.setItems(FXCollections.observableArrayList(trainer.getTeam()));
        TeamCb.setValue(trainer.getTeam());
    }
    private void initSliders(){
        minutesSlider = new Slider(30, 90,30);
        minutesSlider.setShowTickMarks(true);
        minutesSlider.setShowTickLabels(true);
        minutesSlider.setMajorTickUnit(15);
        minutesSlider.setBlockIncrement(15);
        minutesSlider.setValue(30);
        minutesSlider.setSnapToTicks(true);
        minutesSlider.setMaxWidth(400.0);
        minutesSlider.setLabelFormatter(createLabelFormatter(15, 30));
        minutesHBox.getChildren().add(minutesSlider);

        membersSlider = new Slider(10, 60,10);
        membersSlider.setShowTickMarks(true);
        membersSlider.setValue(10);
        membersSlider.setShowTickLabels(true);
        membersSlider.setMajorTickUnit(10);
        membersSlider.setBlockIncrement(10);
        membersSlider.setSnapToTicks(true);
        membersSlider.setMaxWidth(400.0);
        membersSlider.setLabelFormatter(createLabelFormatter(10,10));
        membersHBox.getChildren().add(membersSlider);
    }

    private StringConverter<Double> createLabelFormatter(int multiple, int startVal){
        return new StringConverter<>() {
            @Override
            public String toString(Double value) {
                if(value == startVal) return String.valueOf(value.intValue());
                return (value) % multiple == 0 ? String.valueOf(value.intValue()) : null;
            }

            @Override
            public Double fromString(String string) {
                return null;
            }
        };
    }

    private void initButton(){
        createBtn.setOnAction( e->{
            Activity activity;
            if(!validateFields()) return;
            if(createActivity()!=null) activity = createActivity();
            else return;
            int numOfWeeks = Integer.parseInt(weeksTF.getText());
            if(imageView.getImage()!=null){
                String fileName = selectedFile.getName();
                int dotIndex = fileName.lastIndexOf('.');
                String imageExtension = (dotIndex > 0) ? fileName.substring(dotIndex) : "";
                fileName = "/home/mikolajmichalczyk/IdeaProjects/sportClub/sportClubUi/src/main/resources/images/" + activity.getName().replaceAll("\\s", "")+ imageExtension;
                utils.saveImage(selectedFile.toPath(), fileName);
                String dbUrl = "/images/" + activity.getName().replaceAll("\\s", "")+imageExtension;
                activity.setImageUrl(dbUrl);
            }
            for(int i=1; i<=numOfWeeks; i++){
                Activity dbActivity = new Activity(activity.getName(), activity.getDate(),
                        activity.getTime(), activity.getMinutes(),activity.getDescription(),
                        activity.getSport(), 0,activity.getMemberLimit(),
                        activity.getLocation(), activity.getTrainer(), activity.getTeam(), activity.getImageUrl());

                ServiceResponse<?> serviceResponse = activityService.addActivity(dbActivity);
                if(serviceResponse.getMessage().equals("location is busy during that time")){
                    handleError("location is busy during that time");
                    return;
                }
                if(serviceResponse.getMessage().equals("trainer is busy during that time")){
                    handleError("trainer is busy during that time");
                    return;
                }
                LocalDate date = activity.getDate();
                activity.setDate(date.plusWeeks(1));
            }
            Stage stage = (Stage) createBtn.getScene().getWindow();
            stage.close();
        });
    }

    private boolean validateFields(){
        if(activityName.getText().isEmpty()) {
            handleError("please provide name");
            return false;
        }
        if(timeField.getText().isEmpty()) {
            handleError("please provide time");
            return false;
        }
        if(sportCb.getValue()==null) {
            handleError("please provide sport");
            return false;
        }
        if(locationCb.getValue()==null) {
            handleError("please provide location name");
            return false;
        }
        if(trainer == null && TrainerCb.getValue()==null) {
            handleError("please provide trainer name");
            return false;
        }
        if(datePicker.getValue()==null) {
            handleError("please provide date");
            return false;
        }

        if(weeksTF.getText()==null || !weeksTF.getText().matches("[1-9]|1[0-5]")){
            handleError("please provide a number in range 1-15");
            return false;
        }
        return true;
    }

    private Activity createActivity(){
        Activity activity = new Activity();
        activity.setName(activityName.getText());
        LocalTime time;
        try{
            time = formatStringToTime(timeField.getText());
        }catch (IllegalArgumentException exception){
            handleError("give time in format HH:MM");
            return null;
        }
        activity.setTime(time);
        activity.setSport(sportCb.getValue());
        activity.setLocation(locationCb.getValue());
        if(trainer==null) activity.setTrainer(TrainerCb.getValue());
        else activity.setTrainer(trainer);
        activity.setDate(datePicker.getValue());
        activity.setMinutes((int) minutesSlider.getValue());

        if(TeamCb.getValue()!=null) activity.setTeam(TeamCb.getValue());
        if(descriptionArea.getText()!=null) activity.setDescription(descriptionArea.getText());
        activity.setMemberLimit((int) membersSlider.getValue());
        return activity;
    }
    private LocalTime formatStringToTime(String timeString){
        if (!isValidFormat(timeString)) {
            throw new IllegalArgumentException("Invalid time format. Use HH:mm format.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(timeString, formatter);
    }

    private void handleError(String error){
        Utils utils = new Utils();
        utils.showErrorMessage(error);
    }

    private boolean isValidFormat(String timeString) {
        Pattern TIME_PATTERN = Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
        Matcher matcher = TIME_PATTERN.matcher(timeString);
        return matcher.matches();
    }


}
