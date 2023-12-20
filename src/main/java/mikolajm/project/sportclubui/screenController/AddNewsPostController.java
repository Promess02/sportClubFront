package mikolajm.project.sportclubui.screenController;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mikolaj.project.backendapp.DTO.NewsPostForm;
import mikolaj.project.backendapp.model.Activity;
import mikolaj.project.backendapp.model.Location;
import mikolaj.project.backendapp.model.MembershipType;
import mikolaj.project.backendapp.repo.ActivityRepo;
import mikolaj.project.backendapp.repo.LocationRepo;
import mikolaj.project.backendapp.repo.MembershipTypeRepo;
import javafx.collections.*;
import mikolaj.project.backendapp.service.NewsPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AddNewsPostController {
    public TextField tileField;
    public ChoiceBox<Location> locationCheckBox;
    public ChoiceBox<Activity> activityCheckBox;
    public ChoiceBox<MembershipType> membershipCheckBox;
    public TextArea contentArea;
    public Button submitBtn;

    private final MembershipTypeRepo membershipTypeRepo;
    private final ActivityRepo activityRepo;
    private final LocationRepo locationRepo;
    private final NewsPostService newsPostService;

    @Autowired
    public AddNewsPostController(MembershipTypeRepo membershipTypeRepo, ActivityRepo activityRepo,
                                 LocationRepo locationRepo, NewsPostService newsPostService) {
        this.membershipTypeRepo = membershipTypeRepo;
        this.activityRepo = activityRepo;
        this.locationRepo = locationRepo;
        this.newsPostService = newsPostService;
    }

    public void initialize(){
       initCheckBoxes();
       initSubmitBtn();
    }
    private void initCheckBoxes(){
        List<Location> locationList = locationRepo.findAll();
        List<Activity> activityList = activityRepo.findAll();
        List<MembershipType> membershipTypeList = membershipTypeRepo.findAll();
        Set<Activity> set = new HashSet<>(activityList.stream()
                .collect(Collectors.toMap(Activity::getName, activity -> activity, (a, b) -> a))
                .values());
        List<Activity> distinctActivities = set.stream().toList();

        ObservableList<MembershipType> membershipItems = FXCollections.observableArrayList();
        membershipItems.addAll(membershipTypeList);
        membershipCheckBox.setItems(membershipItems);

        ObservableList<Activity> activityItems = FXCollections.observableArrayList();
        activityItems.addAll(distinctActivities);
        activityCheckBox.setItems(activityItems);

        ObservableList<Location> locationItems = FXCollections.observableArrayList();
        locationItems.addAll(locationList);
        locationCheckBox.setItems(locationItems);
    }

    private void initSubmitBtn(){
        submitBtn.setOnAction( e->{
            NewsPostForm newsPostForm = new NewsPostForm();
            if(activityCheckBox.getValue()!=null) newsPostForm.setActivityId(activityCheckBox.getValue().getId());
            if(membershipCheckBox.getValue()!=null) newsPostForm.setMembershipTypeId(membershipCheckBox.getValue().getId());
            if(locationCheckBox.getValue()!=null) newsPostForm.setLocationId(locationCheckBox.getValue().getId());
            newsPostForm.setContent(contentArea.getText());
            newsPostForm.setTitle(tileField.getText());
            newsPostForm.setImageUrl("paddedImageUrl");
            newsPostService.addNewsPost(newsPostForm);
            Stage stage = (Stage) submitBtn.getScene().getWindow();
            stage.close();
        });
    }
}
