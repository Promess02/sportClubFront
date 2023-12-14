package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Getter;

public class WarningViewController {
    @FXML private ImageView exclamationImg;
    @FXML private Label warningMsg;
    @FXML @Getter private Button okBtn;
    @FXML private Button cancelBtn;

    public WarningViewController() {
    }

    public void initialize(String warning){
        Image image = new Image("/images/exclamationMark.png");
        exclamationImg.setImage(image);
        this.warningMsg.setText(warning);
        initCancelBtn();
    }
    private void initCancelBtn(){
        cancelBtn.setOnAction(e->{
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        });
    }
}
