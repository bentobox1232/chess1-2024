import REPLS.PreloginUI;
import Server.ServerFacade;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
/*        Server server = new Server();
          var port = server.run(0);
          ServerFacade.SERVER_URL = ServerFacade.SERVER_URL + port; */

        ServerFacade.baseUrl = ServerFacade.baseUrl + "8080";

        PreloginUI repl = new PreloginUI();
        repl.start();

        /*         server.stop();  */
    }
}