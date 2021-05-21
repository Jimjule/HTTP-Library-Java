package HTTPServer;

import java.io.IOException;
import java.io.InputStream;

public class RequestReader {
    public static String getRequest(InputStream in) throws IOException {
        int readIn;
        StringBuilder input = new StringBuilder();
        while ((readIn = in.read()) != -1 && in.available() != 0) {
            input.append((char) readIn);
        }
        input.append((char) readIn);

        return input.toString();
    }

    public static String getRequestParams(String request) {
        return request.split("\r\n")[0];
    }

    public static String getRequestMethod(String parameters) {
        return parameters.split(" ")[0];
    }

    public static String getRequestAddress(String parameters) {
        return parameters.split(" ")[1];
    }

    public static String getBody(String request) {
        StringBuilder body = new StringBuilder();
        String[] inputLines;
        if (request.contains("Content-Length")) {
            inputLines = request.split("\r\n\r\n");
            body.append(inputLines[inputLines.length - 1].trim());
        }
        return body.toString();
    }
}
