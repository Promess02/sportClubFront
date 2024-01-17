package mikolajm.project.sportclubui.screenController.generalScreens;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import mikolaj.project.backendapp.model.Address;
import mikolaj.project.backendapp.model.Location;
import mikolaj.project.backendapp.repo.AddressRepo;
import mikolaj.project.backendapp.repo.LocationRepo;
import mikolajm.project.sportclubui.Util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class LocationViewController {
    @Getter
    @FXML private AnchorPane fullView;
    @FXML private TextField CityTf;
    @FXML private TextField streetTf;
    @FXML private TextField houseNumberTf;
    @FXML private TextField postCodeTf;
    @FXML private TextField nameTF;
    @FXML private TextField maxCapacityTF;
    @FXML private TextField weekDayOpenTF;
    @FXML private TextField weekDayCloseTF;
    @FXML private TextField weekEndOpenTF;
    @FXML private TextField weekEndCloseTF;

    @FXML private Button okBtn;

    private Location location;
    private final LocationRepo locationRepo;
    private final AddressRepo addressRepo;
    private final Utils utils = new Utils();

    @Autowired
    public LocationViewController(LocationRepo locationRepo, AddressRepo addressRepo) {
        this.locationRepo = locationRepo;
        this.addressRepo = addressRepo;
    }

    public void initialize(){
        initOkBtn();
    }

    public void setLocation(Location location){
        this.location = location;
        nameTF.setText(location.getName());
        maxCapacityTF.setText(location.getMaxCapacity().toString());
        weekDayOpenTF.setText(location.getWeekDayOpenTime().toString());
        weekDayCloseTF.setText(location.getWeekDayCloseTime().toString());
        weekEndOpenTF.setText(location.getWeekendOpenTime().toString());
        weekEndCloseTF.setText(location.getWeekendCloseTime().toString());
        CityTf.setText(location.getAddress().getCity());
        streetTf.setText(location.getAddress().getStreet());
        houseNumberTf.setText(location.getAddress().getHouseNumber());
        postCodeTf.setText(location.getAddress().getPostCode());
    }

    public void setViewOnly(){
        nameTF.setDisable(true);
        maxCapacityTF.setDisable(true);
        weekDayOpenTF.setDisable(true);
        weekDayCloseTF.setDisable(true);
        weekEndOpenTF.setDisable(true);
        weekEndCloseTF.setDisable(true);
        CityTf.setDisable(true);
        streetTf.setDisable(true);
        houseNumberTf.setDisable(true);
        postCodeTf.setDisable(true);
        okBtn.setVisible(false);
    }

    public void initOkBtn(){
        okBtn.setOnAction( e->{
            if(location!=null){
                okBtn.setText("Save");
            }
            Location locationDb = new Location();
            if(nameTF.getText()==null){
                handleError("give valid name");
                return;
            }else locationDb.setName(nameTF.getText());

            if(maxCapacityTF.getText()==null || !utils.isInteger(maxCapacityTF.getText())){
                handleError("give valid max capacity");
                return;
            }else locationDb.setMaxCapacity(Integer.parseInt(maxCapacityTF.getText()));

            if(weekDayOpenTF.getText()==null || !utils.isValidTimeFormat(weekDayOpenTF.getText())){
                handleError("give valid week day open time");
                return;
            }else{
                LocalTime time = utils.formatStringToTime(weekDayOpenTF.getText());
                locationDb.setWeekDayOpenTime(time);
            }

            if(weekDayCloseTF.getText()==null || !utils.isValidTimeFormat(weekDayCloseTF.getText())){
                handleError("give valid week day close time");
                return;
            }else{
                LocalTime time = utils.formatStringToTime(weekDayCloseTF.getText());
                locationDb.setWeekDayCloseTime(time);
            }

            if(weekEndOpenTF.getText()==null || !utils.isValidTimeFormat(weekEndOpenTF.getText())){
                handleError("give valid week end open time");
                return;
            }else{
                LocalTime time = utils.formatStringToTime(weekEndOpenTF.getText());
                locationDb.setWeekendOpenTime(time);
            }

            if(weekEndCloseTF.getText()==null || !utils.isValidTimeFormat(weekEndCloseTF.getText())){
                handleError("give valid week end close time");
                return;
            }else{
                LocalTime time = utils.formatStringToTime(weekEndCloseTF.getText());
                locationDb.setWeekendCloseTime(time);
            }
            Address address = new Address();
            if(CityTf.getText()==null || CityTf.getText().isBlank()){
                handleError("give valid city");
                return;
            }else address.setCity(CityTf.getText());

            if(streetTf.getText()==null || CityTf.getText().isBlank()){
                handleError("give valid street");
                return;
            }else address.setStreet(streetTf.getText());

            if(houseNumberTf.getText()==null || houseNumberTf.getText().isBlank()){
                handleError("give valid house number");
                return;
            }else address.setHouseNumber(houseNumberTf.getText());

            if(postCodeTf.getText()==null || postCodeTf.getText().isBlank()){
                handleError("give valid post code in format 'NN-NNNN'");
                return;
            }else address.setPostCode(postCodeTf.getText());

            locationDb.setAddress(address);
            if(location!=null && location.getName().equals(locationDb.getName())){
                location = locationDb;
                addressRepo.save(address);
                locationRepo.save(location);
            }else{
                addressRepo.save(address);
                locationDb.setCurrentCapacity(0);
                locationRepo.save(locationDb);
            }

            Stage stage = (Stage) okBtn.getScene().getWindow();
            stage.close();
        });
    }

    private boolean checkIfMatchesPostCodePattern(String s){
        return s.matches("\\d{2}-\\d{4}");
    }

    private void handleError(String error){
        utils.showErrorMessage(error);
        nameTF.setText("");
        maxCapacityTF.setText("");
        weekDayOpenTF.setText("");
        weekDayCloseTF.setText("");
        weekEndOpenTF.setText("");
        weekEndCloseTF.setText("");
        CityTf.setText("");
        streetTf.setText("");
        postCodeTf.setText("");
        houseNumberTf.setText("");
    }

}
