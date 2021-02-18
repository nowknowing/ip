package duke.handler;

import java.time.LocalDateTime;

import duke.Storage;
import duke.Ui;
import duke.tasks.Deadline;
import duke.tasks.Event;
import duke.tasks.Task;
import duke.tasks.TaskList;


public class ScheduleHandler implements CommandHandler {
    private CommandHandler commandHandler;
    private String frequency;
    private int numOfTimes;

    /**
     * Default constructor for schedule handler.
     * @param commandHandler The command to be scheduled.
     * @param frequency The frequency of recurrence.
     * @param numOfTimes The number of times of recurrence.
     */
    public ScheduleHandler(CommandHandler commandHandler, String frequency, int numOfTimes) {
        this.commandHandler = commandHandler;
        this.frequency = frequency;
        this.numOfTimes = numOfTimes;
    }

    @Override
    public String execute(Ui ui, Storage storage, TaskList taskList) {
        String response;
        if (commandHandler instanceof DeadlineHandler) {
            response = deadlineScheduleExecute(ui, storage, taskList);
        } else {
            assert commandHandler instanceof EventHandler;
            response = this.eventScheduleExecute(ui, storage, taskList);
        }
        return response;
    }

    /**
     * Executes the recurring scheduling of deadline tasks.
     * @param ui The ui.
     * @param storage The storage.
     * @param taskList The list of tasks.
     * @return The string response for recurrence tasks scheduled.
     */
    private String deadlineScheduleExecute(Ui ui, Storage storage, TaskList taskList) {
        String response = "";
        Deadline firstTask = ((DeadlineHandler) commandHandler).getDeadlineTask();
        LocalDateTime firstTime = firstTask.getBy();
        String taskDescription = firstTask.getDescription();
        LocalDateTime dateTime = firstTime;
        for (int i = 0; i < numOfTimes; i++) {
            dateTime = dateTime.plusDays(7);
            response += new DeadlineHandler(taskDescription, dateTime).execute(ui, storage, taskList);
        }
        return response;
    }

    /**
     * Executes the recurring scheduling of events.
     * @param ui The ui.
     * @param storage The storage.
     * @param taskList The list of tasks.
     * @return The string response for recurrence tasks scheduled.
     */
    private String eventScheduleExecute(Ui ui, Storage storage, TaskList taskList) {
        String response = "";
        Event firstTask = ((EventHandler) commandHandler).getEventTask();
        LocalDateTime firstTime = firstTask.getAt();
        String taskDescription = firstTask.getDescription();
        LocalDateTime dateTime = firstTime;
        new DeadlineHandler(taskDescription, dateTime).execute(ui, storage, taskList);
        for (int i = 0; i < numOfTimes; i++) {
            dateTime = dateTime.plusDays(7);
            new DeadlineHandler(taskDescription, dateTime).execute(ui, storage, taskList);
        }
        response += scheduleRespond(firstTask, taskList);
        return response;
    }

    private String scheduleRespond(Task toAdd, TaskList taskList) {
        assert (toAdd instanceof Deadline || toAdd instanceof Event);
        String scheduleResponse = "Got it. I've added this recurring task:\n"
                + " " + toAdd + "\n"
                + "for " + numOfTimes + " weeks. \n"
                + "Now you have " + taskList.getNumOfTasks() + " tasks in the list.\n";
        return scheduleResponse;
    }
}
