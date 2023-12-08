package mikolajm.project.sportclubui.screenController.calendar;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.Getter;
import mikolaj.project.backendapp.model.Calendar;

import java.time.LocalDate;

/**
 * Create an anchor pane that can store additional data.
 */

@Getter
public class AnchorPaneNode extends AnchorPane {

    // Date associated with this pane
    private LocalDate date;
    private Calendar calendar;

    /**
     * Create a anchor pane node. Date is not assigned in the constructor.
     * @param children children of the anchor pane
     */

    public AnchorPaneNode(Node... children) {
        super(children);
        this.setBorder(new Border(new BorderStroke(Color.BLACK, null, new CornerRadii(0.1), new BorderWidths(5d))));
        // Add action handler for mouse clicked
        this.setOnMouseClicked(e -> {
            System.out.println("This pane's date is: " + date);
            if(calendar!=null) System.out.println("This pane's calendar entity name:  " + calendar.getName());
        });
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
        setBackground(new javafx.scene.layout.Background(
                new javafx.scene.layout.BackgroundFill(Color.GREEN, null, null)));
        Button getActivityBtn = new Button();
        getActivityBtn.setText("=>");
        getActivityBtn.setMaxWidth(35.0);
        getActivityBtn.setMaxHeight(25.0);
        getActivityBtn.setAlignment(Pos.CENTER);
        Label textView = new Label();
        textView.setText(calendar.getName());
        textView.setFont(new Font(22.0));
        textView.setAlignment(Pos.TOP_CENTER);
        VBox vbox = new VBox();
        vbox.setSpacing(15.0);
        vbox.getChildren().add(textView);
        vbox.getChildren().add(getActivityBtn);
        vbox.setLayoutX(20.0);
        vbox.setLayoutY(20.0);
        this.getChildren().add(vbox);
    }
}