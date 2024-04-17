package AjaClassGetSet;

public class Option {
    private String id;
    private String name;

    public Option() {
        // Diperlukan konstruktor kosong untuk Firebase Realtime Database
    }

    public Option(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}


