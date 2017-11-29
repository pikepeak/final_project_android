package kmitl.final_project_android.khunach58070011.gamer.model;

public class UserInfoSent {
    private String name;
    private String email;
    private String desc;
    private String favgame;
    private String favgroup;
    private String appname;
    private String pic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserInfoSent(String email, String name, String desc, String favgame, String favgroup, String appname, String pic) {
        this.email = email;
        this.name = name;
        this.desc = desc;
        this.favgame = favgame;
        this.favgroup = favgroup;
        this.appname = appname;
        this.pic = pic;
    }

    public UserInfoSent() {
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getFavgroup() {
        return favgroup;
    }

    public void setFavgroup(String favgroup) {
        this.favgroup = favgroup;
    }

    public String getFavgame() {
        return favgame;
    }

    public void setFavgame(String favgame) {
        this.favgame = favgame;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
