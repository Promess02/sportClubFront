package mikolajm.project.sportclubui.screenController.adminScreens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import mikolaj.project.backendapp.enums.Sport;
import mikolaj.project.backendapp.model.Activity;
import mikolaj.project.backendapp.model.Location;
import mikolaj.project.backendapp.model.Team;
import mikolaj.project.backendapp.model.Trainer;
import mikolaj.project.backendapp.repo.ActivityRepo;
import mikolaj.project.backendapp.repo.LocationRepo;
import mikolaj.project.backendapp.repo.TeamRepo;
import mikolaj.project.backendapp.repo.TrainerRepo;
import mikolajm.project.sportclubui.Util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UpdateActivityController {
    @FXML private TextField activityName;
    @FXML private ChoiceBox<Sport> sportCb;
    @FXML private ChoiceBox<Location> locationCb;
    @FXML private ChoiceBox<Team> TeamCb;
    @FXML private ChoiceBox<Trainer> TrainerCb;

    @FXML private TextField timeField;
    @FXML private HBox membersHBox;
    @FXML private TextArea descriptionArea;
    @FXML private Button createBtn;
    @FXML private HBox minutesHBox;
    private Slider minutesSlider;
    private Slider membersSlider;
    private final LocationRepo locationRepo;
    private final TeamRepo teamRepo;
    private final TrainerRepo trainerRepo;
    private final ActivityRepo activityRepo;

    private Activity activity;

    @Autowired
    public UpdateActivityController(LocationRepo locationRepo, TeamRepo teamRepo,
                                    TrainerRepo trainerRepo, ActivityRepo activityRepo) {
        this.locationRepo = locationRepo;
        this.activityRepo = activityRepo;
        this.teamRepo = teamRepo;
        this.trainerRepo = trainerRepo;
    }

    public void initialize(){
        initCheckBoxes();
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

    public void setActivity(Activity activity){
        this.activity = activity;
        activityName.setText(activity.getName());
        timeField.setText(activity.getTime().toString());
        minutesSlider.setValue(activity.getMinutes());
        membersSlider.setValue(activity.getMemberLimit());
        descriptionArea.setText(activity.getDescription());
        sportCb.setValue(activity.getSport());
        TeamCb.setValue(activity.getTeam());
        locationCb.setValue(activity.getLocation());
        TrainerCb.setValue(activity.getTrainer());
        activityName.setDisable(true);
    }

    private void initButton(){
        createBtn.setOnAction( e->{
            Activity activity = new Activity();
            if(activityName.getText().isEmpty()) {
                handleError("please provide name");
                return;
            }else activity.setName(activityName.getText());
            if(timeField.getText().isEmpty()) {
                handleError("please provide time");
                return;
            }else {
                LocalTime time;
                try{
                    time = formatStringToTime(timeField.getText());
                }catch (IllegalArgumentException exception){
                    handleError("give time in format HH:MM");
                    return;
                }
                activity.setTime(time);
            }
            if(sportCb.getValue()==null) {
                handleError("please provide sport");
                return;
            }else activity.setSport(sportCb.getValue());
            if(locationCb.getValue()==null) {
                handleError("please provide name");
                return;
            } else  activity.setLocation(locationCb.getValue());
            if(TrainerCb.getValue()==null) {
                handleError("please provide name");
                return;
            } else activity.setTrainer(TrainerCb.getValue());

            if(TeamCb.getValue()!=null) activity.setTeam(TeamCb.getValue());
            if(descriptionArea.getText()!=null) activity.setDescription(descriptionArea.getText());
            activity.setMemberLimit((int) membersSlider.getValue());
            activity.setMinutes((int) minutesSlider.getValue());

            List<Activity> activityList = activityRepo.findAll();

            for(Activity dbActivity: activityList){
                if(dbActivity.getName().equals(activity.getName())) {
                    activity.setDate(dbActivity.getDate());
                    activity.setId(dbActivity.getId());
                    activityRepo.save(activity);
                }
            }

            Stage stage = (Stage) createBtn.getScene().getWindow();
            stage.close();

        });
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
