package duke;

import duke.exceptions.DukeException;
import duke.exceptions.DukeIDKException;
import duke.exceptions.DukeInvalidDesException;
import duke.exceptions.DukeMissingDesException;
import duke.handler.Parser;
import duke.handler.Queries;
import duke.tasks.*;
import duke.tasks.Event;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Duke {
    private boolean exit = false;
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    private Duke(String storagePath) {
        storage = new Storage(storagePath);
        tasks = storage.load();
        ui = new Ui();
    }

    public static void main(String[] args) throws IOException {
        new Duke("data/tasks.txt").run();
    }

    public void run() {
        ui.displayIntro();
        while (exit == false) {
            processInput(ui.readCommand());
        }
        ui.close();
    }


    private void processInput(String userInput) {
        String keyword_UC = userInput.toUpperCase().split(" ", -1)[0];
        Queries query = Queries.ADD;
        String response = "";

        if (Queries.containsValue(keyword_UC)) {
            query = Queries.valueOf(keyword_UC);
        }

        try {
            switch (query) {
            case BYE:
                response = "Bye. Hope to see you again soon!";
                ui.respond(response);
                exit = true;
                break;

            case ADD:
                Task toAdd;
                try {
                    if (keyword_UC.equals("TODO")) {
                        toAdd = new Todo(userInput.split(" ", 2)[1]);
                    } else if (keyword_UC.equals("DEADLINE")) {
                        String[] info = userInput.split(" ", 2);
                        if (info[1].contains("/by")) {
                            info = info[1].split("/by");
                        } else {
                            throw new DukeInvalidDesException(keyword_UC);
                        }
                        toAdd = new Deadline(info[0], info[1]);
                    } else if (keyword_UC.equals("EVENT")) {
                        String[] info = userInput.split(" ", 2);
                        if (info[1].contains("/at")) {
                            info = info[1].split(("/at"));
                        } else {
                            throw new DukeInvalidDesException(keyword_UC);
                        }
                        toAdd = new Event(info[0], info[1]);
                    } else {
                        throw new DukeIDKException();
                    }
                    tasks.addTask(toAdd);
                    response = "Got it. I've added this task:\n"
                            + " " + toAdd + "\n"
                            + "Now you have " + tasks.getNumOfTasks() + " tasks in the list.\n";
                    ui.respond(response);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new DukeMissingDesException(keyword_UC);
                }
                break;

            case DONE:
                try {
                    int taskNum = Integer.parseInt(userInput.split(" ")[1]);
                    tasks.markDone(taskNum);
                    Task taskUpdated = tasks.getTask(taskNum);
                    response = "Nice! I've marked this task as done: \n"
                            + " " + taskUpdated + "\n";
                    ui.respond(response);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new DukeMissingDesException("DONE");
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    throw new DukeInvalidDesException("DONE");
                }
                break;

            case DELETE:
                try {
                    int taskNum = Integer.parseInt(userInput.split(" ")[1]);
                    Task task = tasks.getTask(taskNum);
                    tasks.removeTask(taskNum);
                    response = "Noted. I've removed this task: \n"
                            + " " + task + "\n";
                    ui.respond(response);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new DukeMissingDesException("DELETE");
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    throw new DukeInvalidDesException("DELETE");
                }
                break;
            case LIST:
                response += tasks.toString();
                ui.respond(response);
                break;
            }
        } catch (DukeException e) {
            String output = e.getMessage();
            ui.respond(output);
        }

    }


}
