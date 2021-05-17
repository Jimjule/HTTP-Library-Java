package HTTPServer.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CodesTest {
    @Test
    public void returns200OK() {
        String expected = "200 OK";
        assertEquals(expected, Codes._200.getCode());
    }
}