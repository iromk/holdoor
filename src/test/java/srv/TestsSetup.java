package srv;

import common.core.App;
import common.core.Mode;
import org.junit.BeforeClass;

import java.util.logging.Level;

public class TestsSetup {

    @BeforeClass
    public static void setUpClass() {
        new TestsSetup();
    }


    public TestsSetup() {

        App.config("UnitTester")
           .set(Mode.TEST)
           .set(Level.ALL)
           .context(true)
           .verbose(true)
           .init();
    }

}
