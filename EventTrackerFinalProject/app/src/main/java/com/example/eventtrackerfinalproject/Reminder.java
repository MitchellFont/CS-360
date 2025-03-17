public class Reminder {
    private String title;
    private String date;
    private String description;
    private String time;

    public Reminder(String title, String date, String description, String time) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.time = time;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }
}
