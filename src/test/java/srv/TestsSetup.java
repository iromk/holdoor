package srv;

import common.App;
import common.Mode;

import java.util.logging.Level;

public class TestsSetup {

    public TestsSetup() {

        App.config("UnitTester")
           .set(Mode.TEST)
           .set(Level.ALL)
           .init();
    }

}
