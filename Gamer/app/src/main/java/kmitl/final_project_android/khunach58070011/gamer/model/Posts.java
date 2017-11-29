package kmitl.final_project_android.khunach58070011.gamer.model;

import android.widget.Button;

public class Posts {
    private String name;
    private String type;
    private String desc;
    private String game;

    public Posts(){

    }
    public Posts(String name, String desc, String type, String game) {
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.game = game;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }
}
