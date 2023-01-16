import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private List<IndividualTask> tasks;

    public TaskList() {
        tasks = new ArrayList<>();
    }

    public void addTask(IndividualTask task) {
        tasks.add(task);
    }

    public void removeTask(IndividualTask task) {
        tasks.remove(task);
    }

    public List<IndividualTask> getTasks() {
        return tasks;
    }
    public List<IndividualTask> getCompletedTasks() {
        List<IndividualTask> completedTasks = new ArrayList<>();
        for (IndividualTask task : tasks) {
            if (task.isCompleted()) {
                completedTasks.add(task);
            }
        }
        return completedTasks;
    }
    public List<IndividualTask> getIncompletedTasks() {
        List<IndividualTask> incompletedTasks = new ArrayList<>();
        for (IndividualTask task : tasks) {
            if (!task.isCompleted()) {
                incompletedTasks.add(task);
            }
        }
        return incompletedTasks;
    }
}
