package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import mikolaj.project.backendapp.model.NewsPost;
import org.springframework.stereotype.Component;

@Component
@Getter
public class NewsPostViewController {
    @FXML private Label dateLabel;
    @FXML private Label articleName;
    @FXML private ImageView imageView;
    @FXML private AnchorPane mainView;

    public NewsPostViewController() {
    }

    @FXML
    public void initialize(NewsPost newsPost){
        dateLabel.setText(newsPost.getDateOfPosting().toString());
        articleName.setText(newsPost.getName());
        Image sampleimage = new Image("src/main/resources/images/newsPostSample.jpg");
        imageView.setImage(sampleimage);
    }
}
