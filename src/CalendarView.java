import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.control.DatePicker;
public class CalendarView extends GridPane {
    private Calendar currentDate;
    private Label[][] dayLabels;
    DatePicker datePicker = new DatePicker();
    public CalendarView() {
        currentDate = new GregorianCalendar();
        dayLabels = new Label[6][7];

        // Create labels for each day of the month
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                dayLabels[i][j] = new Label();
                add(dayLabels[i][j], j, i);
            }
        }

        updateCalendar();
    }

    public void updateCalendar() {
        // Clear the calendar
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                dayLabels[i][j].setText("");
            }
        }

        // Set the first day of the month
        currentDate.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK);
        int dayOfMonth = 1;

        // Fill in the days of the month
        for (int i = firstDayOfWeek - 1; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (i == firstDayOfWeek - 1 && j < firstDayOfWeek - 1) {
                    continue;
                }
                if (dayOfMonth > currentDate.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    break;
                }
                dayLabels[i][j].setText(Integer.toString(dayOfMonth));
                dayOfMonth++;
            }
        }
    }

    public void nextMonth() {
        currentDate.add(Calendar.MONTH, 1);
        updateCalendar();
    }

    public void previousMonth() {
        currentDate.add(Calendar.MONTH, -1);
        updateCalendar();
    }
}
