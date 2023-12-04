package mikolajm.project.sportclubui.screenController.calendar;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class CalendarViewController {

    // Get the pane to put the calendar on
    @Getter
    @FXML Pane calendarPane;

}
