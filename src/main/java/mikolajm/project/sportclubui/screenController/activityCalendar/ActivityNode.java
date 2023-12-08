package mikolajm.project.sportclubui.screenController.activityCalendar;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.Getter;
import mikolaj.project.backendapp.model.Activity;
import mikolaj.project.backendapp.model.Calendar;

import java.time.LocalDate;

@Getter
public class ActivityNode extends AnchorPane {
    private LocalDate date;
    private Activity activity;

    /**
     * Create a anchor pane node. Date is not assigned in the constructor.
     * @param children children of the anchor pane
     */

    public ActivityNode(Node... children) {
        super(children);
        // Add action handler for mouse clicked
        this.setOnMouseClicked(e -> {
            System.out.println("This pane's date is: " + date);
            if(activity!=null) System.out.println("This pane's calendar entity name:  " + activity.getName());
        });
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
        setBackground(new javafx.scene.layout.Background(
                new javafx.scene.layout.BackgroundFill(Color.GREEN, null, null)));
        Button getActivityBtn = new Button();
        getActivityBtn.setText("Sign up");
        getActivityBtn.setFont(new Font(8.0));
        getActivityBtn.setMaxWidth(65.0);
        getActivityBtn.setMaxHeight(17.0);
        getActivityBtn.setAlignment(Pos.CENTER);
        Label textView = new Label();
        textView.setText(activity.getName());
        textView.setFont(new Font(16.0));
        Label memberLimit  = new Label();
        String limit = activity.getCurrentMembers() + "/" + activity.getMemberLimit();
        memberLimit.setText(limit);
        memberLimit.setFont(new Font(14.0));
        memberLimit.setAlignment(Pos.BOTTOM_CENTER);
        memberLimit.setPadding(new Insets(0,0,5,0));
        textView.setAlignment(Pos.TOP_CENTER);
        VBox vbox = new VBox();
        vbox.setSpacing(10.0);
        vbox.getChildren().add(textView);
        vbox.getChildren().add(getActivityBtn);
        vbox.getChildren().add(memberLimit);
        vbox.setLayoutX(20.0);
        if(activity.getCurrentMembers().equals(activity.getMemberLimit())) getActivityBtn.setVisible(false);
        vbox.setLayoutY(20.0);
        this.getChildren().add(vbox);
    }
}
