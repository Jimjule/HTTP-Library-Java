package HTTPServer;

import java.io.IOException;
import java.net.ServerSocket;

public class HTTPServer {
    private ServerSocket serverSocket;
    private Router router;

    public HTTPServer(int port, Router router) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.router = router;
    }

    public void start() {
        while (true) {
            try {
                new ClientHandler(this.serverSocket.accept(), router).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
