package HTTPServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private InputStream in;
    private ByteArrayOutputStream out;
    private String request;
    private Router router;

    public ClientHandler(Socket socket, Router router) {
        this.clientSocket = socket;
        this.router = router;
    }

    public void run() {
        try {
            in = clientSocket.getInputStream();
            out = new ByteArrayOutputStream();
            request = RequestReader.getRequest(in);

            String parameters = RequestReader.getRequestParams(request);
            String parametersMethod = RequestReader.getRequestMethod(parameters);
            String parametersPath = RequestReader.getRequestAddress(parameters);

            String body = RequestReader.getBody(request);

            Response response = new Response();
            Route route = router.getRoute(parametersPath);

            ResponseHelper.responseHandler(parametersMethod, parametersPath, body, response, route);

            out.write(response.printHeaders());
            out.write(response.printBody());
            out.write(response.printFile());

            out.writeTo(clientSocket.getOutputStream());
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

