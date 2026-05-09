public class TaskManager {
    private int taskId;
    private String description;
    private String status;
    private String createdAt;
    private String updatedAt;

    public  TaskManager(int taskId, String description, String status, String createdAt, String updatedAt) {
        this.taskId = taskId;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public int getTaskId() {
        return taskId;
    }
    public  String getDescription() {
        return description;
    }
    public   String getStatus() {
        return status;
    }
   public void setDescription(String description) {
        this.description = description;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
