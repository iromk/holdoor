package client;

public class Main {

    public static void main(String[] args) {
        UserSession userSession = UserSession.get();

        userSession.establish();
        userSession.send("./data/sample/Starter Set - Characters.pdf");
    }

}
