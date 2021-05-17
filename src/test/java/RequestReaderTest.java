import HTTPServer.RequestReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestReaderTest {
    private String headRequest = "GET /head_request HTTP/1.1";
    private String optionsRequest = "OPTIONS /method_options2 HTTP/1.1\n";

    @Test
    public void returnsParamsGET() {
        String parameters = headRequest;
        assertEquals("GET", RequestReader.getRequestMethod(parameters));
    }

    @Test
    public void returnsParamsOPTIONS() {
        String parameters = optionsRequest;
        assertEquals("OPTIONS", RequestReader.getRequestMethod(parameters));
    }

    @Test
    public void returnsParamsAddress() {
        String parameters = optionsRequest;
        assertEquals("/method_options2", RequestReader.getRequestAddress(parameters));
    }

    @Test
    public void readsSingleLineBody() {
        String input = "POST /test HTTP/1.1\r\n" +
                "Host: thing.example\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Content-Length: 27\r\n" +
                "\r\n\r\n" +
                "field1=value1&field2=value2";
        String expectedResponse = "field1=value1&field2=value2";
        assertEquals(expectedResponse, RequestReader.getBody(input));
    }

    @Test
    public void getsAddressFromFullRequest() {
        String input = "POST /test HTTP/1.1\r\n" +
                "Host: thing.example\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Content-Length: 27\r\n" +
                "\r\n\r\n" +
                "field1=value1&field2=value2";
        String expectedResponse = "/test";
        assertEquals(expectedResponse, RequestReader.getRequestAddress(input));
    }

    @Test
    public void readsMultilineJSON() {
        String input = "POST /pokemon/id/25 HTTP/1.1\r\n" +
                "Content-Type: application/json\r\n" +
                "User-Agent: PostmanRuntime/7.28.0\r\n" +
                "Accept: */*\r\n" +
                "Postman-Token: 8f627a29-9785-4ca7-8527-44ef2f4ae343\r\n" +
                "Host: localhost:5000\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Connection: keep-alive\r\n" +
                "Content-Length: 39\r\n" +
                "\r\n\r\n" +
                "{\r\n" +
                "    \"id\": 25,\r\n" +
                "    \"name\": \"Pikachu\"\r\n" +
                "}";
        String expectedResponse = "{\r\n" +
                "    \"id\": 25,\r\n" +
                "    \"name\": \"Pikachu\"\r\n" +
                "}";
        assertEquals(expectedResponse, RequestReader.getBody(input));
    }

    @Test
    public void getsPostmanAddress() {
        String input = "POST /pokemon/id/25 HTTP/1.1\r\n" +
                "Content-Type: application/json\r\n" +
                "User-Agent: PostmanRuntime/7.28.0\r\n" +
                "Accept: */*\r\n" +
                "Postman-Token: 8f627a29-9785-4ca7-8527-44ef2f4ae343\r\n" +
                "Host: localhost:5000\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Connection: keep-alive\r\n" +
                "Content-Length: 39\r\n" +
                "\r\n\r\n" +
                "{\r\n" +
                "    \"id\": 25,\r\n" +
                "    \"name\": \"Pikachu\"\r\n" +
                "}";
        String expectedResponse = "/pokemon/id/25";
        assertEquals(expectedResponse, RequestReader.getRequestAddress(input));
    }
}
