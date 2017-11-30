package kmitl.final_project_android.khunach58070011.gamer;

import org.junit.Test;

import kmitl.final_project_android.khunach58070011.gamer.validation.validationNull;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by khunach on 11/30/2017.
 */
public class validationTest {
    @Test
    public void CreateGroupAllInputIsNull(){
        validationNull validationNull = new validationNull();
        assertTrue("`true` when all input is null", validationNull.validationCreateGroupInputIsNull("","",""));
    }
    @Test
    public void CreateGroupSomeInputIsNull(){
        validationNull validationNull = new validationNull();
        assertTrue("`true` when some input is null", validationNull.validationCreateGroupInputIsNull("asdas","dads",""));
    }
    @Test
    public void CreateGroupAllInputIsNotNull(){
        validationNull validationNull = new validationNull();
        assertFalse("`false` when some input is null", validationNull.validationCreateGroupInputIsNull("asdas","dads","sdsaaaaaaaaaaaaaaadsdasd"));
    }
    @Test
    public void JoinGroupInputIsNull(){
        validationNull validationNull = new validationNull();
        assertTrue("`false` when some input is null", validationNull.validationJoinGroupInputIsNull(""));
    }
    @Test
    public void JoinGroupInputIsNotNull(){
        validationNull validationNull = new validationNull();
        assertFalse("`false` when some input is null", validationNull.validationJoinGroupInputIsNull("dasdas"));
    }
    @Test
    public void EditGroupInputIsNull(){
        validationNull validationNull = new validationNull();
        assertTrue("`false` when some input is null", validationNull.validationEditGroupInputIsNull("peak", "hello", ""));
    }
    @Test
    public void EditGroupInputIsNotNull(){
        validationNull validationNull = new validationNull();
        assertFalse("`false` when some input is null", validationNull.validationEditGroupInputIsNull("peak", "hello", "overwatch"));
    }
    @Test
    public void EditProfileInputIsNull(){
        validationNull validationNull = new validationNull();
        assertTrue("`false` when some input is null", validationNull.validationEditProfileInputIsNull("peak", "", "overwatch", "aaa"));
    }
    @Test
    public void EditProfileInputIsNotNull(){
        validationNull validationNull = new validationNull();
        assertFalse("`false` when some input is null", validationNull.validationEditProfileInputIsNull("peak", "hello", "overwatch", "aaa"));
    }
    @Test
    public void InviteInputIsNull(){
        validationNull validationNull = new validationNull();
        assertTrue("`false` when some input is null", validationNull.validationInvInputIsNull(""));
    }
    @Test
    public void InviteInputIsNotNull(){
        validationNull validationNull = new validationNull();
        assertFalse("`false` when some input is null", validationNull.validationInvInputIsNull("pea"));
    }
    @Test
    public void ListInputIsNull(){
        validationNull validationNull = new validationNull();
        assertTrue("`false` when some input is null", validationNull.validationListInputIsNull(""));
    }
    @Test
    public void ListInputIsNotNull(){
        validationNull validationNull = new validationNull();
        assertFalse("`false` when some input is null", validationNull.validationListInputIsNull("overwatch"));
    }
    @Test
    public void PostInputIsNull(){
        validationNull validationNull = new validationNull();
        assertTrue("`false` when some input is null", validationNull.validationPostInputIsNull("", "","",""));
    }
    @Test
    public void PostInputIsNotNull(){
        validationNull validationNull = new validationNull();
        assertFalse("`false` when some input is null", validationNull.validationPostInputIsNull("overwatch","overwatch","overwatch","overwatch"));
    }
}
