package kmitl.final_project_android.khunach58070011.gamer.model;

public class GamerGroup {
    private String name;
    private String desc;
    private String favgame;
    private String leader;

    public GamerGroup(String name, String desc, String favgame, String leader) {
        this.name = name;
        this.desc = desc;
        this.favgame = favgame;
        this.leader = leader;
    }

    public GamerGroup() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFavgame() {
        return favgame;
    }

    public void setFavgame(String favgame) {
        this.favgame = favgame;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }
}
