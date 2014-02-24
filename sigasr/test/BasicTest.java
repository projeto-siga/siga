import org.junit.Test;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

	@Test
	public void aVeryImportantThingToTest() {
		assertEquals(2, 1 + 1);
	}
	
	@Test
	public void fullTest(){
		Fixtures.loadModels("data.yml");
	}

}
