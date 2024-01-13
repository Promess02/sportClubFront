package mikolajm.project.sportclubui.screenController.UtilityScreens;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;

@Getter
public class ErrorMessageController {
    @FXML private ImageView exclamationImg;
    @FXML private Label errorMsg;
    private String error;

    public ErrorMessageController() {

    }
    public void setError(String error) {
        Image image = new Image("/images/exclamationMark.png");
        exclamationImg.setImage(image);
        this.error = error;
        errorMsg.setText(error);
    }
}
