package holdoor.client;

public class Main {

    public static void main(String[] args) {
        UserSession userSession = new UserSession();

        userSession.establish();
        userSession.send("doc");
    }

}
