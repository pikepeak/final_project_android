package kmitl.final_project_android.khunach58070011.gamer.validation;

public class validationNull {
    public validationNull() {

    }
    public boolean validationCreateGroupInputIsNull(String name, String desc, String game){
        if (name != null && name.isEmpty()){
            return true;
        }else if (desc != null && desc.isEmpty()){
            return true;
        }else if (game != null && game.isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    public boolean validationJoinGroupInputIsNull(String s){
        if (s != null && s.isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    public boolean validationEditProfileInputIsNull(String name, String desc, String game, String group){
        if (name != null && name.isEmpty()){
            return true;
        }else if (desc != null && desc.isEmpty()){
            return true;
        }else if (game != null && game.isEmpty()){
            return true;
        }else if (group != null && group.isEmpty()) {
            return true;
        }else{
            return false;
        }
    }
    public boolean validationEditGroupInputIsNull(String name, String desc, String game){
        if (name != null && name.isEmpty()){
            return true;
        }else if (desc != null && desc.isEmpty()){
            return true;
        }else if (game != null && game.isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    public boolean validationInvInputIsNull(String s){
        if (s != null && s.isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    public boolean validationListInputIsNull(String s){
        if (s != null && s.isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    public boolean validationPostInputIsNull(String name, String desc, String type, String game){
        if (name != null && name.isEmpty()){
            return true;
        }else if (desc != null && desc.isEmpty()){
            return true;
        }else if (game != null && game.isEmpty()){
            return true;
        }else if (type != null && type.isEmpty()){
            return true;
        }else{
            return false;
        }
    }
}
