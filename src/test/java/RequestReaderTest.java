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
}
