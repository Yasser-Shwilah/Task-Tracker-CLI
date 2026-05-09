import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JsonFileHandler {
    private static final String FILE_NAME = "tasks.json";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<Task> loadTasks() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_NAME)));
            if (content.trim().isEmpty()) {
                return new ArrayList<>();
            }
            
            return parseJsonArray(content);
        } catch (IOException e) {
            System.err.println("Error reading tasks file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveTasks(List<Task> tasks) {
        try {
            String json = tasksToJson(tasks);
            Files.write(Paths.get(FILE_NAME), json.getBytes());
        } catch (IOException e) {
            System.err.println("Error saving tasks file: " + e.getMessage());
        }
    }

    private static List<Task> parseJsonArray(String json) {
        List<Task> tasks = new ArrayList<>();
        json = json.trim();
        
        if (!json.startsWith("[") || !json.endsWith("]")) {
            return tasks;
        }
        
        String content = json.substring(1, json.length() - 1).trim();
        if (content.isEmpty()) {
            return tasks;
        }
        
        String[] taskObjects = splitTaskObjects(content);
        
        for (String taskObj : taskObjects) {
            Task task = parseTask(taskObj.trim());
            if (task != null) {
                tasks.add(task);
            }
        }
        
        return tasks;
    }

    private static String[] splitTaskObjects(String content) {
        List<String> objects = new ArrayList<>();
        int braceCount = 0;
        int start = 0;
        
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c == '{') {
                braceCount++;
            } else if (c == '}') {
                braceCount--;
                if (braceCount == 0) {
                    objects.add(content.substring(start, i + 1));
                    start = i + 1;
                    while (start < content.length() && content.charAt(start) == ',') {
                        start++;
                    }
                    i = start - 1;
                }
            }
        }
        
        return objects.toArray(new String[0]);
    }

    private static Task parseTask(String taskJson) {
        try {
            int id = extractIntValue(taskJson, "id");
            String description = extractStringValue(taskJson, "description");
            String status = extractStringValue(taskJson, "status");
            String createdAt = extractStringValue(taskJson, "createdAt");
            String updatedAt = extractStringValue(taskJson, "updatedAt");
            
            return new Task(id, description, status, createdAt, updatedAt);
        } catch (Exception e) {
            System.err.println("Error parsing task: " + e.getMessage());
            return null;
        }
    }

    private static int extractIntValue(String json, String key) {
        String pattern = "\"" + key + "\":";
        int index = json.indexOf(pattern);
        if (index == -1) return 0;
        
        int valueStart = index + pattern.length();
        while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
            valueStart++;
        }
        
        int valueEnd = valueStart;
        while (valueEnd < json.length() && (Character.isDigit(json.charAt(valueEnd)) || json.charAt(valueEnd) == '-')) {
            valueEnd++;
        }
        
        String valueStr = json.substring(valueStart, valueEnd);
        return Integer.parseInt(valueStr);
    }

    private static String extractStringValue(String json, String key) {
        String pattern = "\"" + key + "\":\"";
        int index = json.indexOf(pattern);
        if (index == -1) return "";
        
        int valueStart = index + pattern.length();
        int valueEnd = valueStart;
        
        while (valueEnd < json.length()) {
            char c = json.charAt(valueEnd);
            if (c == '"' && (valueEnd == 0 || json.charAt(valueEnd - 1) != '\\')) {
                break;
            }
            valueEnd++;
        }
        
        return json.substring(valueStart, valueEnd);
    }

    private static String tasksToJson(List<Task> tasks) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        
        for (int i = 0; i < tasks.size(); i++) {
            if (i > 0) {
                json.append(",");
            }
            json.append(taskToJson(tasks.get(i)));
        }
        
        json.append("]");
        return json.toString();
    }

    private static String taskToJson(Task task) {
        return String.format(
            "{\"id\":%d,\"description\":\"%s\",\"status\":\"%s\",\"createdAt\":\"%s\",\"updatedAt\":\"%s\"}",
            task.getId(),
            escapeJson(task.getDescription()),
            task.getStatus(),
            task.getCreatedAt(),
            task.getUpdatedAt()
        );
    }

    private static String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    public static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DATE_FORMATTER);
    }

    public static int getNextId(List<Task> tasks) {
        int maxId = 0;
        for (Task task : tasks) {
            if (task.getId() > maxId) {
                maxId = task.getId();
            }
        }
        return maxId + 1;
    }
}
