package mikolajm.project.sportclubui.screenController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import mikolaj.project.backendapp.DTO.ServiceResponse;
import mikolaj.project.backendapp.enums.Sport;
import mikolaj.project.backendapp.model.Activity;
import mikolaj.project.backendapp.model.Location;
import mikolaj.project.backendapp.model.Team;
import mikolaj.project.backendapp.model.Trainer;
import mikolaj.project.backendapp.repo.ActivityRepo;
import mikolaj.project.backendapp.repo.LocationRepo;
import mikolaj.project.backendapp.repo.TeamRepo;
import mikolaj.project.backendapp.repo.TrainerRepo;
import mikolaj.project.backendapp.service.ActivityService;
import mikolajm.project.sportclubui.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AddActivityViewController {
    public TextField weeksTF;
    @FXML private HBox weeksHBox;
    @FXML private HBox minutesHBox;
    @FXML private HBox membersHBox;
    @FXML private TextField activityName;
    @FXML private ChoiceBox<Sport> sportCb;
    @FXML private ChoiceBox<Location> locationCb;
    @FXML private ChoiceBox<Team> TeamCb;
    @FXML private ChoiceBox<Trainer> TrainerCb;
    @FXML private DatePicker datePicker;
//    private Slider weeksSlider;
    private Slider minutesSlider;
    private Slider membersSlider;
    @FXML private TextField timeField;
    @FXML private TextArea descriptionArea;
    @FXML private Button createBtn;

    private final LocationRepo locationRepo;
    private final TeamRepo teamRepo;
    private final TrainerRepo trainerRepo;
    private final ActivityRepo activityRepo;

    @Autowired
    public AddActivityViewController(LocationRepo locationRepo, TeamRepo teamRepo,
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

//        weeksSlider = new Slider(1, 15,1);
//        weeksSlider.setShowTickMarks(true);
//        weeksSlider.setShowTickLabels(true);
//        weeksSlider.setMajorTickUnit(5);
//        weeksSlider.setValue(1);
//        weeksSlider.setBlockIncrement(5);
//        weeksSlider.setSnapToTicks(true);
//        weeksSlider.setMaxWidth(400.0);
//        weeksSlider.setLabelFormatter(createLabelFormatter(5,1));
//        weeksHBox.getChildren().add(weeksSlider);


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
            if(datePicker.getValue()==null) {
                handleError("please provide date");
                return;
            } else activity.setDate(datePicker.getValue());
            activity.setMinutes((int) minutesSlider.getValue());
            
            if(TeamCb.getValue()!=null) activity.setTeam(TeamCb.getValue());
            if(descriptionArea.getText()!=null) activity.setDescription(descriptionArea.getText());
            activity.setMemberLimit((int) membersSlider.getValue());

            int numOfWeeks;
            if(weeksTF.getText()==null || !weeksTF.getText().matches("[1-9]|1[0-5]")){
                handleError("please provide a number in range 1-15");
                return;
            }else numOfWeeks = Integer.parseInt(weeksTF.getText());
            //int numOfWeeks = (int)weeksSlider.getValue();


            for(int i=1; i<=numOfWeeks; i++){
                if (activityRepo.findActivityByDate(activity.getDate()).isPresent()) {
                    handleError("activity already exists in that date");
                    return;
                }
                Activity dbActivity = new Activity(activity.getName(), activity.getDate(), activity.getTime(),
                        activity.getMinutes(),activity.getDescription(), activity.getSport(),0,activity.getMemberLimit(),activity.getLocation(),
                        activity.getTrainer(), activity.getTeam());
                activityRepo.save(dbActivity);
                LocalDate date = activity.getDate();
                activity.setDate(date.plusWeeks(1));
            }

            Stage stage = (Stage) createBtn.getScene().getWindow();
            stage.close();

        });
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
