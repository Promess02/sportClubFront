package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import mikolaj.project.backendapp.DTO.ServiceResponse;
import mikolaj.project.backendapp.model.User;
import mikolaj.project.backendapp.service.Impl.ServiceMessages;
import mikolaj.project.backendapp.service.RegistrationService;
import mikolajm.project.sportclubui.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegisterViewController {
    @FXML private TextField userName;
    @FXML private TextField userSurname;
    @FXML private TextField email;
    @FXML private TextField userPhoneNumber;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confPasswordField;
    @FXML private Button confirmBtn;

    private final RegistrationService registrationService;

    @Autowired
    public RegisterViewController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }
    public void initialize(){
        initConfirmBtn();
    }

    private void initConfirmBtn(){
        confirmBtn.setOnAction( e-> {
            User user = new User();
            user.setName(userName.getText());
            user.setSurname(userSurname.getText());
            user.setEmail(email.getText());
            user.setPhoneNumber(userPhoneNumber.getText());
            user.setPassword(passwordField.getText());
            if(!verifyUser(user)) return;
            ServiceResponse<User> serviceResponse = registrationService.saveUser(user);
            if(serviceResponse.getMessage().equals(ServiceMessages.EMAIL_EXISTS)){
                showErrorMsg("account with email exists");
                return;
            }
            showErrorMsg("account created");
            Stage stage = (Stage) confirmBtn.getScene().getWindow();
            stage.close();
        });
    }

    private boolean verifyUser(User user){
        if(user.getName().isEmpty()) {
            showErrorMsg("please provide user name");
            return false;
        }
        if(user.getSurname().isEmpty()) {
            showErrorMsg("please provide user surname");
            return false;
        }
        if(user.getEmail().isEmpty()) {
            showErrorMsg("please provide user email");
            return false;
        }
        if(user.getPhoneNumber().isEmpty()) {
            showErrorMsg("please provide user phoneNumber");
            return false;
        }
        if(user.getPassword().length()<8 || user.getPassword().length()>32){
            showErrorMsg("password characters not in range of 8-32");
            return false;
        }
        if(!passwordField.getText().equals(confPasswordField.getText())){
            showErrorMsg("password 1 doesn't match to pass 2");
            return false;
        }
        return true;
    }

    private void showErrorMsg(String error){
        Utils utils = new Utils();
        utils.showErrorMessage(error);
        userName.setText("");
        userSurname.setText("");
        email.setText("");
        userPhoneNumber.setText("");
        passwordField.setText("");
        confPasswordField.setText("");
    }

}
