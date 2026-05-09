import java.util.List;

public class TaskTrackerCLI {
    
    public static void main(String[] args) {
        if (args.length == 0) {
            showUsage();
            return;
        }
        
        String command = args[0];
        
        try {
            switch (command) {
                case "add":
                    handleAdd(args);
                    break;
                case "update":
                    handleUpdate(args);
                    break;
                case "delete":
                    handleDelete(args);
                    break;
                case "mark-in-progress":
                    handleMarkInProgress(args);
                    break;
                case "mark-done":
                    handleMarkDone(args);
                    break;
                case "list":
                    handleList(args);
                    break;
                default:
                    System.err.println("Unknown command: " + command);
                    showUsage();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static void handleAdd(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: task-cli add \"<description>\"");
            return;
        }
        
        String description = args[1];
        List<Task> tasks = JsonFileHandler.loadTasks();
        int newId = JsonFileHandler.getNextId(tasks);
        String timestamp = JsonFileHandler.getCurrentTimestamp();
        
        Task newTask = new Task(newId, description, "todo", timestamp, timestamp);
        tasks.add(newTask);
        JsonFileHandler.saveTasks(tasks);
        
        System.out.println("Task added successfully (ID: " + newId + ")");
    }
    
    private static void handleUpdate(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: task-cli update <id> \"<description>\"");
            return;
        }
        
        try {
            int taskId = Integer.parseInt(args[1]);
            String newDescription = args[2];
            List<Task> tasks = JsonFileHandler.loadTasks();
            
            Task task = findTaskById(tasks, taskId);
            if (task == null) {
                System.err.println("Task with ID " + taskId + " not found");
                return;
            }
            
            task.setDescription(newDescription);
            task.setUpdatedAt(JsonFileHandler.getCurrentTimestamp());
            JsonFileHandler.saveTasks(tasks);
            
            System.out.println("Task updated successfully");
        } catch (NumberFormatException e) {
            System.err.println("Invalid task ID: " + args[1]);
        }
    }
    
    private static void handleDelete(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: task-cli delete <id>");
            return;
        }
        
        try {
            int taskId = Integer.parseInt(args[1]);
            List<Task> tasks = JsonFileHandler.loadTasks();
            
            Task task = findTaskById(tasks, taskId);
            if (task == null) {
                System.err.println("Task with ID " + taskId + " not found");
                return;
            }
            
            tasks.remove(task);
            JsonFileHandler.saveTasks(tasks);
            
            System.out.println("Task deleted successfully");
        } catch (NumberFormatException e) {
            System.err.println("Invalid task ID: " + args[1]);
        }
    }
    
    private static void handleMarkInProgress(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: task-cli mark-in-progress <id>");
            return;
        }
        
        try {
            int taskId = Integer.parseInt(args[1]);
            List<Task> tasks = JsonFileHandler.loadTasks();
            
            Task task = findTaskById(tasks, taskId);
            if (task == null) {
                System.err.println("Task with ID " + taskId + " not found");
                return;
            }
            
            task.setStatus("in-progress");
            task.setUpdatedAt(JsonFileHandler.getCurrentTimestamp());
            JsonFileHandler.saveTasks(tasks);
            
            System.out.println("Task marked as in-progress");
        } catch (NumberFormatException e) {
            System.err.println("Invalid task ID: " + args[1]);
        }
    }
    
    private static void handleMarkDone(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: task-cli mark-done <id>");
            return;
        }
        
        try {
            int taskId = Integer.parseInt(args[1]);
            List<Task> tasks = JsonFileHandler.loadTasks();
            
            Task task = findTaskById(tasks, taskId);
            if (task == null) {
                System.err.println("Task with ID " + taskId + " not found");
                return;
            }
            
            task.setStatus("done");
            task.setUpdatedAt(JsonFileHandler.getCurrentTimestamp());
            JsonFileHandler.saveTasks(tasks);
            
            System.out.println("Task marked as done");
        } catch (NumberFormatException e) {
            System.err.println("Invalid task ID: " + args[1]);
        }
    }
    
    private static void handleList(String[] args) {
        List<Task> tasks = JsonFileHandler.loadTasks();
        
        if (args.length == 1) {
            listAllTasks(tasks);
        } else {
            String status = args[1];
            listTasksByStatus(tasks, status);
        }
    }
    
    private static void listAllTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found");
            return;
        }
        
        System.out.println("All Tasks:");
        for (Task task : tasks) {
            displayTask(task);
        }
    }
    
    private static void listTasksByStatus(List<Task> tasks, String status) {
        boolean found = false;
        
        for (Task task : tasks) {
            if (task.getStatus().equals(status)) {
                if (!found) {
                    System.out.println("Tasks with status '" + status + "':");
                    found = true;
                }
                displayTask(task);
            }
        }
        
        if (!found) {
            System.out.println("No tasks found with status '" + status + "'");
        }
    }
    
    private static void displayTask(Task task) {
        System.out.println(task.getId() + ". " + task.getDescription() + " [" + task.getStatus() + "]");
    }
    
    private static Task findTaskById(List<Task> tasks, int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }
    
    private static void showUsage() {
        System.out.println("Task Tracker CLI - Usage:");
        System.out.println("  task-cli add \"<description>\"");
        System.out.println("  task-cli update <id> \"<description>\"");
        System.out.println("  task-cli delete <id>");
        System.out.println("  task-cli mark-in-progress <id>");
        System.out.println("  task-cli mark-done <id>");
        System.out.println("  task-cli list");
        System.out.println("  task-cli list <status> (todo, in-progress, done)");
    }
}
