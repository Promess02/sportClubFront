package mikolajm.project.sportclubui.screenController.trainerScreens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mikolaj.project.backendapp.model.Calendar;
import mikolaj.project.backendapp.model.Member;
import mikolaj.project.backendapp.model.Trainer;
import mikolaj.project.backendapp.repo.CalendarRepo;
import mikolaj.project.backendapp.repo.MemberRepo;
import mikolajm.project.sportclubui.Util.CurrentSessionUser;
import mikolajm.project.sportclubui.Util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddPersonalTrainingController {
    @FXML private TextField nameField;
    @FXML private ChoiceBox<Trainer> trainerCb;
    @FXML private ChoiceBox<Member> memberCb;
    @FXML private DatePicker datePicker;
    @FXML private TextField timeField;
    @FXML private TextField minuteField;
    @FXML private TextArea descriptionArea;
    @FXML private Button addBtn;
    private Calendar calendar = new Calendar();
    private final CalendarRepo calendarRepo;
    private final MemberRepo memberRepo;

    private final CurrentSessionUser currentSessionUser;
    Utils utils = new Utils();

    @Autowired
    public AddPersonalTrainingController(CalendarRepo calendarRepo,
                                         MemberRepo memberRepo,
                                         CurrentSessionUser currentSessionUser) {
        this.calendarRepo = calendarRepo;
        this.memberRepo = memberRepo;
        this.currentSessionUser = currentSessionUser;
    }

    public void initialize(){
        initChoiceBoxes();
        initBtn();
    }

    private void initChoiceBoxes(){
        if(currentSessionUser.getTrainer()==null) {
            handleError("somehow trainer is null");
            return;
        }
        List<Member> memberList = memberRepo.findAllByTeam(currentSessionUser.getTrainer().getTeam());
        ObservableList<Member> memberItems = FXCollections.observableArrayList(memberList);
        memberCb.setItems(memberItems);
        trainerCb.setItems(FXCollections.observableArrayList(currentSessionUser.getTrainer()));
        trainerCb.setValue(currentSessionUser.getTrainer());
        trainerCb.setDisable(true);
    }

    public void setCalendar(Calendar calendar){
        this.calendar = calendar;
        addBtn.setText("change");
        nameField.setText(calendar.getName());
        trainerCb.setVisible(false);
        trainerCb.setDisable(true);
        memberCb.setItems(FXCollections.observableArrayList(calendar.getMember()));
        memberCb.setValue(calendar.getMember());
        datePicker.setValue(calendar.getDate());
        timeField.setText(calendar.getTime().toString());
        minuteField.setText(calendar.getMinutes().toString());
        descriptionArea.setText(calendar.getDescription());
        addBtn.setOnAction( e-> {
            if(!createCalendar())return;
            createCalendar();
            calendarRepo.save(calendar);
            currentSessionUser.loadCalendarList();
            Stage stage = (Stage) addBtn.getScene().getWindow();
            stage.close();
        });
    }

    public void setViewOnly(){
        nameField.setDisable(true);
        trainerCb.setDisable(true);
        memberCb.setDisable(true);
        datePicker.setDisable(true);
        timeField.setDisable(true);
        minuteField.setDisable(true);
        addBtn.setText("Ok");
        descriptionArea.setEditable(false);
        addBtn.setOnAction( e -> {
            Stage stage = (Stage) addBtn.getScene().getWindow();
            stage.close();
        });
    }

    private boolean createCalendar(){
        if(nameField.getText().isBlank()){
            handleError("please enter name first");
            return false;
        }else calendar.setName(nameField.getText());
        if(trainerCb.getValue()==null){
            handleError("please enter trainer first");
            return false;
        }else calendar.setTrainer(trainerCb.getValue());
        if(memberCb.getValue()==null){
            handleError("please enter trainer first");
            return false;
        }else calendar.setMember(memberCb.getValue());
        if(datePicker.getValue()==null){
            handleError("please enter date first");
            return false;
        }else calendar.setDate(datePicker.getValue());
        if(timeField.getText().isBlank() || !utils.isValidTimeFormat(timeField.getText())){
            handleError("time is blank or in wrong format");
            return false;
        }else calendar.setTime(utils.formatStringToTime(timeField.getText()));
        if(minuteField.getText().isBlank() || !utils.isInteger(minuteField.getText())){
            handleError("minutes are not given or are not an integer");
            return false;
        }else calendar.setMinutes(Integer.parseInt(minuteField.getText()));
        calendar.setDescription(descriptionArea.getText());
        return true;
    }

    private void initBtn(){
        addBtn.setOnAction( e -> {
            if(!createCalendar())return;
            createCalendar();
            if(!calendarRepo.findCalendarsByMemberAndDate(calendar.getMember(), calendar.getDate()).isEmpty())
            {
                handleError("member has something planned then");
                return;
            }
            if(!calendarRepo.findCalendarsByTrainerAndDate(calendar.getTrainer(), calendar.getDate()).isEmpty())
            {
                handleError("trainer has something planned then");
                return;
            }
            calendarRepo.save(calendar);
            currentSessionUser.loadCalendarList();

            Stage stage = (Stage) addBtn.getScene().getWindow();
            stage.close();
        });
    }

    private void handleError(String error){
        Utils utils = new Utils();
        utils.showErrorMessage(error);
    }

}
