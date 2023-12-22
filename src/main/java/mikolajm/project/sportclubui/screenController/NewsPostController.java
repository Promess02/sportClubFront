package mikolajm.project.sportclubui.screenController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import mikolaj.project.backendapp.model.NewsPost;

@Getter
public class NewsPostController {
    @FXML private TextArea articleName;
    @FXML private Label postDate;
    @FXML private ImageView articleImage;
    @FXML private Button locationBtn;
    @FXML private Button membershipBtn;
    @FXML private Button activityBtn;
    @FXML private TextArea articleContent;
    private NewsPost newsPost;
    public NewsPostController() {
    }

    public void setNewsPost(NewsPost newsPost) {
        this.newsPost = newsPost;
        articleName.setText(newsPost.getName());
        articleContent.setText(newsPost.getContent());
        postDate.setText("date: " + newsPost.getDateOfPosting().toString());
        Image image;
        if(newsPost.getImageUrl()==null)image = new Image("/images/newsPostSample.jpg");
        else image = new Image(newsPost.getImageUrl());
        articleImage.setImage(image);
        if(newsPost.getActivity()==null) activityBtn.setVisible(false);
        else initActivityBtn();
        if(newsPost.getLocation()==null) locationBtn.setVisible(false);
        else initLocationBtn();
        if(newsPost.getMembershipType()==null) membershipBtn.setVisible(false);
        else initMembershipBtn();
        articleContent.setEditable(false);
        articleName.setEditable(false);
    }



    private void initActivityBtn(){}
    private void initLocationBtn(){}
    private void initMembershipBtn(){}
}
