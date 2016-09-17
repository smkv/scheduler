package ee.smkv.scheduler.model;

public class Task {
    Long id;
    Type type;
    String command;

    public Task(Long id, Type type, String command) {
        this.id = id;
        this.type = type;
        this.command = command;
    }

    public Long getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", type=" + type +
                ", command='" + command + '\'' +
                '}';
    }
}
