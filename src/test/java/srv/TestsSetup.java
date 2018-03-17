package srv;

import common.core.App;
import common.core.Mode;

import java.util.logging.Level;

public class TestsSetup {

    public TestsSetup() {

        App.config("UnitTester")
           .set(Mode.TEST)
           .set(Level.ALL)
           .context(true)
           .verbose(true)
           .init();
    }

}
