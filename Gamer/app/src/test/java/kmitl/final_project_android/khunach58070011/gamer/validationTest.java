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
}
