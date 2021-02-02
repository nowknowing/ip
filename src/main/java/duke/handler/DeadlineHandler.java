package duke.handler;

import duke.Storage;
import duke.Ui;
import duke.tasks.Deadline;
import duke.tasks.TaskList;

import java.time.LocalDateTime;

public class DeadlineHandler implements CommandHandler {
    private Deadline toAdd;
    private String response;

    public DeadlineHandler(String deadlineDes, LocalDateTime dateTimeBy) {
        toAdd = new Deadline(deadlineDes, dateTimeBy);
    }
    @Override
    public void execute(Ui ui, Storage storage, TaskList taskList) {
        taskList.addTask(toAdd);
        response = "Got it. I've added this task:\n"
                + " " + toAdd + "\n"
                + "Now you have " + taskList.getNumOfTasks() + " tasks in the list.\n";
        ui.respond(response);
        storage.addTask(toAdd);
    }
}
