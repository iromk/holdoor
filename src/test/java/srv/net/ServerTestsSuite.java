package srv.net;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({SessionManagerTest.class, AuthenticationTest.class, ReceiveSampleFileTest.class})

public class ServerTestsSuite {
}
