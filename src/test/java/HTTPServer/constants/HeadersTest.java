package HTTPServer.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeadersTest {
    @Test
    public void getsHeaderAndValue() {
        String expectedValue = "Content-Type: application/json;charset=utf-8";
        assertEquals(expectedValue, Headers.CONTENT_TYPE_JSON.getHeader());
    }

    @Test
    public void getsAllowHeader() {
        String expectedValue = "Allow: ";
        assertEquals(expectedValue, Headers.ALLOW.getHeader());
    }
}