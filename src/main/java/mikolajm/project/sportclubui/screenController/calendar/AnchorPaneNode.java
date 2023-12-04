package mikolajm.project.sportclubui.screenController.calendar;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
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
    }
}