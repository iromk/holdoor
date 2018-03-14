package srv;

import common.App;

import java.util.logging.Level;

public class TestsSetup {

    public TestsSetup() {

        App.set()
           .environment(App.Environment.TEST)
           .logLevel(Level.ALL)
           .init();
    }

}
