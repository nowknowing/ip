import java.util.*;
import java.util.stream.Collectors;

public class Duke {
    private static final String rightIndent = "  ";
    private static final String textSpacer = rightIndent + "|" + " ";
    private static final String responseBoxTop = rightIndent + "_________________________________________\n";
    private static final String responseBoxBottom = rightIndent + "|______________________________________\n"
            + rightIndent + "                                        \\|\n";
    private static Scanner sc = new Scanner(System.in);
    private static boolean exit = false;
    private static List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        displayIntro();
        while (exit == false && sc.hasNext()) {
            processInput(sc.nextLine());
        }
        sc.close();
    }

    private static void processInput(String userInput) {
        String keyword_UC = userInput.toUpperCase().split(" ", -1)[0];
        Queries query = Queries.ADD;
        String response ="";

        if (Queries.containsValue(keyword_UC)) {
            query = Queries.valueOf(keyword_UC);
        }

        switch (query) {
            case BYE:
                response = "Bye. Hope to see you again soon!";
                respond(response);
                exit = true;
                break;

            case ADD:
                Task toAdd;
                if (keyword_UC.equals("TODO")) {
                    toAdd = new Todo(userInput.split(" ", 2)[1]);
                } else if (keyword_UC.equals("DEADLINE")) {
                    String[] info = userInput.split(" ", 2)[1].split("/by");
                    toAdd = new Deadline(info[0], info[1]);
                } else if (keyword_UC.equals("EVENT")) {
                    String[] info = userInput.split(" ", 2)[1].split("/at");
                    toAdd = new Event(info[0], info[1]);
                } else {
                    toAdd = new Task("");
                }
                tasks.add(toAdd);
                response = "Got it. I've added this task:\n"
                        + " " + toAdd + "\n"
                        + "Now you have " + tasks.size() + " tasks in the list.\n";
                respond(response);
                break;

            case DONE:
                int taskNum = Integer.parseInt(userInput.split(" ")[1]);
                Task task = tasks.get(taskNum - 1);
                Task updatedTask = task.markDone();
                tasks.set(taskNum - 1, updatedTask);
                response = "Nice! I've marked this task as done: \n"
                        + " " + updatedTask + "\n";
                respond(response);
                break;

            case LIST:
                for (int i = 0; i < tasks.size(); i++) {
                    Task t = tasks.get(i);
                    response += Integer.toString(i + 1) + "."
                            + t + "\n";
                }
                respond(response);
                break;

            default:
                respond(response);
                break;
        }
    }

    private static void displayIntro() {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        String response = "Hello from\n"
                + logo + "\n"
                + "What can I do for you?\n";
        respond(response);
    }

    private static void respond(String response) {
        String indentedResponse = Arrays.stream(response.split("\n"))
                .map(line -> textSpacer + line)
                .collect(Collectors.joining("\n")) + "\n";
        String output = responseBoxTop
                + indentedResponse
                + responseBoxBottom;

        System.out.println(output);
    }

}

