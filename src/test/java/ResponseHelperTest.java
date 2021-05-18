import HTTPServer.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseHelperTest {
    Route route;

    @BeforeEach
    public void init() {
        route = new TestRoute();
    }

    @Test
    public void testSetsResponse404() {
        Response response = new Response();
        ResponseHelper.setRouteNotFound(response);
        assertEquals("HTTP/1.1 404 Not Found\r\n\r\n", response.getStringResponse());
    }

    @Test
    public void testNullReturns404() {
        Response response = new Response();
        ResponseHelper.checkRouteNotFound(response, null);
        assertEquals("HTTP/1.1 404 Not Found\r\n\r\n", response.getStringResponse());
    }

    @Test
    public void testSetResponseHeaders() {
        String expectedHeaders = "Content-Type: text/html;charset=utf-8\r\n" +
                "Allow: GET, HEAD\r\n";
        Response response = new Response();
        ResponseHelper.setResponseHeaders(response, route);
        assertEquals(expectedHeaders, response.getHeaders());
    }

    @Test
    public void testCheckRouteParamsFound() {
        String expectedResponse = "HTTP/1.1 200 OK\r\n\r\n";
        Response response = new Response();
        response.setParams(ResponseHelper.getResponseCode("GET", route));
//        ResponseHelper.setResponseHeaders(response, route);
        route.performRequest("GET", response, "", "");
        ResponseHelper.checkRouteParamsFound(response, route);
        assertEquals(expectedResponse, response.getStringResponse());
    }

    @Test
    public void testGetResponseCode200() {
        String responseCode = ResponseHelper.getResponseCode("GET", route);
        assertEquals(Codes._200.getCode(), responseCode);
    }

    @Test
    public void testGetResponseCode405() {
        String responseCode = ResponseHelper.getResponseCode("POST", route);
        assertEquals(Codes._405.getCode(), responseCode);
    }

    private class TestRoute implements Route {
        private String body = null;
        private ArrayList<String> headers = new ArrayList<>();
        private final List<String> allow = Arrays.asList("GET", "HEAD");
        private boolean routeIsFound;

        @Override
        public String getBody() {
            return body;
        }

        @Override
        public ArrayList<String> getHeaders() {
            headers.add(Headers.CONTENT_TYPE_HTML.getHeader());
            headers.add(formatAllow());
            return headers;
        }

        @Override
        public String formatAllow() {
            String allowHeader = Headers.ALLOW.getHeader();
            allowHeader += String.join(", ", allow);
            return allowHeader;
        }

        @Override
        public List<String> getAllow() {
            return allow;
        }

        @Override
        public void performRequest(String requestType, Response response, String body, String path) {
            this.body = body;
            routeIsFound = true;
        }

        @Override
        public boolean getRouteIsFound() {
            return this.routeIsFound;
        }
    }
}
