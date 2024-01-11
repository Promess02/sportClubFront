package mikolajm.project.sportclubui.screenController.generalScreens;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import mikolaj.project.backendapp.service.EmailService;
import mikolajm.project.sportclubui.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class MailWindow {
    @FXML private TextField titleField;
    @FXML private  TextArea contentArea;
    @FXML private  Button submitBtn;
    @Setter
    private String targetEmail;
    private final EmailService emailService;
    @Autowired
    public MailWindow(EmailService emailService) {
        this.emailService = emailService;
    }
    public void initialize(){
        initBtn();
    }
    private void initBtn(){
        submitBtn.setOnAction( e-> {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(targetEmail);
            if(titleField.getText().isBlank()){
                handleError("title field is blank");
                return;
            }else mailMessage.setSubject(titleField.getText());
            if(contentArea.getText().isBlank()){
                handleError("content area is blank");
                return;
            }else mailMessage.setText(contentArea.getText());
            emailService.sendEmail(mailMessage);
            Stage stage = (Stage) submitBtn.getScene().getWindow();
            stage.close();
        });
    }
    private void handleError(String error){
        Utils utils = new Utils();
        utils.showErrorMessage(error);
    }
}
