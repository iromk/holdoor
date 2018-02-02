package server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({StartTest.class, AuthenticationTest.class, ReceiveSampleFileTest.class})

public class ServerTestsSuite {
}
