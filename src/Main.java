import java.awt.*;
import java.io.*;
import java.lang.reflect.Type;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private TaskList taskList;
    private CalendarView calendarView;
    private ListView<IndividualTask> taskListView;
    private ObservableList<IndividualTask> taskObservableList;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        taskList = new TaskList();
        calendarView = new CalendarView();
        taskObservableList = FXCollections.observableArrayList();
        File file = new File("tasks.json");
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                Gson gson = new Gson();
                Type taskListType = new TypeToken<ArrayList<IndividualTask>>() {}.getType();
                ArrayList tasks = gson.fromJson(br, taskListType);
                taskList.getTasks().addAll(tasks);
                taskObservableList.addAll(tasks);
                br.close();
            } catch (Exception e) {
                System.out.println("Error loading save file!");
                e.printStackTrace();
            }
        }


        taskListView = new ListView<>(taskObservableList);
        taskListView.setPrefSize(200, 200);

        TextField taskNameField = new TextField();
        taskNameField.setPromptText("Task name");

        TextField taskDescriptionField = new TextField();
        taskDescriptionField.setPromptText("Task description");

        DatePicker dueDatePicker = new DatePicker();
        dueDatePicker.setPromptText("Due by 11:59 PM");

        Button addTaskButton = new Button("Add task");
        addTaskButton.setOnAction(e -> {

            String taskName = taskNameField.getText();
            String taskDescription = taskDescriptionField.getText();

            LocalDate dueDate= dueDatePicker.getValue();
           ;
            LocalDateTime endOfDay = dueDate.atTime(23,59,59);
            Date date = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());

            IndividualTask task = new IndividualTask(taskName, taskDescription, date);

            taskList.addTask(task);
            taskObservableList.add(task);
            taskNameField.clear();
            taskDescriptionField.clear();
            dueDatePicker.getEditor().clear();
            Gson gson = new Gson();
            String json = gson.toJson(taskList.getTasks());
            try {
                FileWriter fileWriter = new FileWriter("tasks.json");
                fileWriter.write(json);
                fileWriter.close();
            } catch (Exception error) {
                System.out.println("Error adding task");
                error.printStackTrace();
            }
        });


        Button removeTaskButton = new Button("Remove task");
        removeTaskButton.setOnAction(e -> {
            IndividualTask selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                taskList.removeTask(selectedTask);
                taskObservableList.remove(selectedTask);
            }
        });
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Calendar");
        datePicker.setOnAction(e -> {
            LocalDate selectedDate = datePicker.getValue();

            updateTaskList(selectedDate);
        });

        HBox taskControls = new HBox(10);

        VBox labels = new VBox(10);

        labels.getChildren().addAll(taskNameField, taskDescriptionField, dueDatePicker);
        taskControls.getChildren().addAll(labels, addTaskButton, removeTaskButton);

        taskControls.setPadding(new Insets(10));
        taskControls.setAlignment(Pos.CENTER);

        VBox taskListAndControls = new VBox(10);
        taskListAndControls.getChildren().addAll(taskListView, taskControls);

        BorderPane root = new BorderPane();
        root.setLeft(datePicker);
        root.setCenter(taskListAndControls);
        root.setPadding(new Insets(10));
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("To-Do List");
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            Gson gson = new Gson();
            String json = gson.toJson(taskList.getTasks());
            try {
                FileWriter fileWriter = new FileWriter("tasks.json");
                fileWriter.write(json);
                fileWriter.close();
            } catch (Exception ex) {
                System.out.println("Error saving tasks on exit");
                ex.printStackTrace();
            }
        });
    }

    private void updateTaskList(LocalDate selectedDate) {
        taskObservableList.clear();
        for (IndividualTask task : taskList.getTasks()) {
            if (task.getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(selectedDate)) {
                taskObservableList.add(task);
            }
        }
    }

}
