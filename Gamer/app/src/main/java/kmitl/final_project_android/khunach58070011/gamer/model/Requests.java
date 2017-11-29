package kmitl.final_project_android.khunach58070011.gamer.model;

public class Requests {
    private String name;
    private String email;

    public Requests() {
    }

    public Requests(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
