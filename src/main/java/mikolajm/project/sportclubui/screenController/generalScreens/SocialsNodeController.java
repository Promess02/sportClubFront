package mikolajm.project.sportclubui.screenController.generalScreens;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import mikolaj.project.backendapp.model.SocialMedia;

public class SocialsNodeController {
    @FXML @Getter
    private AnchorPane fullView;
    @FXML private Label nameLabel;
    @FXML private Label urlLabel;
    @FXML private Label locationLabel;
    @FXML private ImageView imageView;

    public void setSocial(SocialMedia socialMedia){
        nameLabel.setText(socialMedia.getName());
        urlLabel.setText(socialMedia.getUrl());
        if(socialMedia.getLocation()!=null) locationLabel.setText(socialMedia.getLocation().getName());
        Image image;
        if(socialMedia.getIconUrl()!=null) image = new Image(socialMedia.getIconUrl());
        else image = new Image("/images/blankFile.png");
        imageView.setImage(image);
    }
}
