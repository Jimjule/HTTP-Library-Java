import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestReaderTest {
    private String headRequest = "GET /head_request HTTP/1.1";
    private String optionsRequest = "OPTIONS /method_options2 HTTP/1.1\n";

    @Test
    public void returnsParamsGET() {
        String parameters = headRequest;
        assertEquals("GET", RequestReader.findRequestMethod(parameters));
    }

    @Test
    public void returnsParamsOPTIONS() {
        String parameters = optionsRequest;
        assertEquals("OPTIONS", RequestReader.findRequestMethod(parameters));
    }

    @Test
    public void returnsParamsAddress() {
        String parameters = optionsRequest;
        assertEquals("/method_options2", RequestReader.findRequestAddress(parameters));
    }

    @Test
    public void readsSingleLineBody() {
        String input = "POST /test HTTP/1.1\n" +
                "Host: thing.example\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Content-Length: 27\n" +
                "\r\n" +
                "field1=value1&field2=value2";
        String expectedResponse = "field1=value1&field2=value2";
        assertEquals(expectedResponse, RequestReader.getBody(input));
    }

    @Test
    public void readsMultilineJSON() {
        String input = "POST /pokemon/id/25 HTTP/1.1\n" +
                "Content-Type: application/json\n" +
                "User-Agent: PostmanRuntime/7.28.0\n" +
                "Accept: */*\n" +
                "Postman-Token: 8f627a29-9785-4ca7-8527-44ef2f4ae343\n" +
                "Host: localhost:5000\n" +
                "Accept-Encoding: gzip, deflate, br\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 39\n" +
                "\r\n" +
                "{\n" +
                "    \"id\": 25,\n" +
                "    \"name\": \"Pikachu\"\n" +
                "}";
        String expectedResponse = "{\n" +
                "    \"id\": 25,\n" +
                "    \"name\": \"Pikachu\"\n" +
                "}";
        assertEquals(expectedResponse, RequestReader.getBody(input));
    }
}
