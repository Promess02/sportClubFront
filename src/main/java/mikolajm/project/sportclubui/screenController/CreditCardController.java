package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import mikolaj.project.backendapp.model.CreditCard;
import mikolaj.project.backendapp.service.CreditCardService;
import mikolajm.project.sportclubui.CurrentSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Component
public class CreditCardController {
    @FXML private TextField numberTF;
    @FXML private TextField cvvTF;
    @FXML private TextField expDateTF;
    @FXML private TextField bankTF;
    @FXML private Button confirmBtn;

    private final CurrentSessionUser currentSessionUser;
    private final CreditCardService creditCardService;
    private CreditCard creditCard;

    @Autowired
    public CreditCardController(CurrentSessionUser currentSessionUser,CreditCardService creditCardService ) {
        this.currentSessionUser = currentSessionUser;
        this.creditCardService = creditCardService;
    }

    public void initialize(){
        confirmBtn.setOnAction( e->{
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            creditCard = new CreditCard(Long.parseLong(numberTF.getText()), Integer.parseInt(cvvTF.getText()),
                    LocalDate.parse(expDateTF.getText(), formatter), bankTF.getText());
            creditCardService.saveCreditCardForUser(creditCard, currentSessionUser.getUser());
            currentSessionUser.loadCreditCard();
            Stage stage = (Stage) confirmBtn.getScene().getWindow();
            stage.close();
        });
    }

}
