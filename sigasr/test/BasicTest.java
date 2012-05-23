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
	public void criarObjetosBasicos() {
	//	new SrItemConfiguracao("Micro").save();
		SrItemConfiguracao itemConfig = SrItemConfiguracao.find("byDescricao",
				"Micro").first();
		assertNotNull(itemConfig);
		assertEquals("Micro", itemConfig.descrItemConfiguracao);
	}
	
	@Test
	public void fullTest(){
		Fixtures.loadModels("data.yml");
	}

}
