package srv.clientserver;

import client.UserSession;
import org.junit.Assert;
import org.junit.Test;
import srv.ServerTestSetup;
import srv.data.Name;
import srv.data.User;

public class RegisteringUserTest extends ServerTestSetup {

    @Test
    public void RegisterUserWithCorrectCredentialsTest() {
        UserSession userSession = UserSession.get();

        final Name givenName = new Name("Geralt","Of Rivia");
        final String givenLogin = "gerald";
        final String uid = givenLogin;
        final String givenPassword = "DHHbd1553";
        userSession.registerUser(givenName, givenLogin, givenPassword);
        final User registeredUser = User.findByUid(uid);
        Assert.assertNotNull("Assert that the User entity were found", registeredUser);
        Assert.assertEquals("Assert that stored name the same as a given", givenName, registeredUser.getName());
    }
}
