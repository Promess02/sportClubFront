package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import mikolaj.project.backendapp.model.Activity;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ActivityViewController {
    @FXML private ImageView imageView;
    @FXML private Button activityBtn;
    @FXML private Label activityName;
    @FXML private AnchorPane mainView;

    public ActivityViewController() {
    }

    @FXML
    public void initialize(Activity activity){
        activityName.setText(activity.getName());
        Image image = new Image("src/main/resources/images/newsPostSample.jpg");
        imageView.setImage(image);
    }
}
