package mikolajm.project.sportclubui.screenController.activityCalendar;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;
import mikolaj.project.backendapp.model.Activity;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CalendarView {
    private ArrayList<ActivityNode> allCalendarDays = new ArrayList<>(35);
    @Getter
    private VBox view;
    private Text calendarTitle;
    private YearMonth currentYearMonth;
    private Map<LocalDate, Activity> mapOfCalendar;
    private GridPane calendar;
    private Text[] dayNames;
    private GridPane dayLabels;
    private HBox titleBar;
    private Button nextMonth;
    private Button previousMonth;
    private List<Activity> activityMap;

    public CalendarView(YearMonth yearMonth, List<Activity> activityList, List<Activity> activityMap) {
        this.activityMap = activityMap;
        currentYearMonth = yearMonth;
        mapOfCalendar = activityList.stream()
                .collect(Collectors.toMap(Activity::getDate, activity -> activity));
        initCalendar();
        refreshView();
        populateCalendar(currentYearMonth);
    }

    private void initCalendar(){
        calendar = new GridPane();
        calendar.setPrefSize(800, 600);
        calendar.setGridLinesVisible(true);
        dayNames = new Text[]{ new Text("Sunday"), new Text("Monday"), new Text("Tuesday"),
                new Text("Wednesday"), new Text("Thursday"), new Text("Friday"),
                new Text("Saturday") };
        dayLabels = new GridPane();
        dayLabels.setPrefWidth(600);
        int col = 0;
        for (Text txt : dayNames) {
            ActivityNode ap = new ActivityNode();
            ap.setPrefSize(200, 10);
            ap.setBottomAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            dayLabels.add(ap, col++, 0);
        }
        calendarTitle = new Text("Activities Calendar");
        previousMonth = new Button("<<");
        previousMonth.setOnAction(e -> previousMonth());
        nextMonth = new Button(">>");
        nextMonth.setOnAction(e -> nextMonth());
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setAlignment(Pos.CENTER);
        refreshBtn.setPadding(new Insets(0,0,0,15));
        refreshBtn.setOnAction( e -> {
            refreshView();
            populateCalendar(currentYearMonth);
        });
        titleBar = new HBox(previousMonth, calendarTitle, nextMonth, refreshBtn);
        titleBar.setAlignment(Pos.CENTER);
        // Populate calendar with the appropriate day numbers
        populateCalendar(currentYearMonth);
        // Create the calendar view
        view = new VBox(titleBar, dayLabels, calendar);
        view.setSpacing(10.0);
    }
    public void refreshView(){
        allCalendarDays = new ArrayList<>(35);
        calendar.getChildren().clear();
        calendar.setPrefSize(800, 600);
        calendar.setGridLinesVisible(true);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                ActivityNode ap = new ActivityNode();
                ap.setPrefSize(200,200);
                calendar.add(ap,j,i);
                allCalendarDays.add(ap);
            }
        }
    }
    /**
     * Set the days of the calendar to correspond to the appropriate date
     * @param yearMonth year and month of month to render
     */
    public void populateCalendar(YearMonth yearMonth) {
        // Get the date we want to start with on the calendar
        LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        // Dial back the day until it is SUNDAY (unless the month starts on a sunday)
        while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY") ) {
            calendarDate = calendarDate.minusDays(1);
        }
        // Populate the calendar with day numbers
        for (ActivityNode ap : allCalendarDays) {
            if (ap.getChildren().size() != 0) {
                ap.getChildren().remove(0);
            }
            Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));
            ap.setDate(calendarDate);
            ap.setTopAnchor(txt, 5.0);
            ap.setLeftAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            if(mapOfCalendar.containsKey(calendarDate)){
                boolean isSigned;
                Activity activity = mapOfCalendar.get(calendarDate);
                isSigned = activityMap.contains(activity);
               // isSigned = activityMap.keySet().stream().filter(act -> act.equals(activity)).toList().isEmpty();
                ap.setActivity(activity, isSigned);
            }

            calendarDate = calendarDate.plusDays(1);
        }
        // Change the title of the calendar
        calendarTitle.setText(yearMonth.getMonth().toString() + " " + String.valueOf(yearMonth.getYear()));
    }

    /**
     * Move the month back by one. Repopulate the calendar with the correct dates.
     */
    private void previousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        refreshView();
        populateCalendar(currentYearMonth);
    }

    /**
     * Move the month forward by one. Repopulate the calendar with the correct dates.
     */
    private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        refreshView();
        populateCalendar(currentYearMonth);
    }

    public void setAllCalendarDays(ArrayList<ActivityNode> allCalendarDays) {
        this.allCalendarDays = allCalendarDays;
    }
}
