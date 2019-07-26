package Spring2;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResponseGeneratorTest {

    @Test
    public void testZeroLengthString() {
        String request = "";
        
        ResponseGenerator generator = new ResponseGenerator(request);
        assertNotNull(generator);

        assertNotNull(generator.response());
        assertEquals("����� �Է��� �ּ���.\r\n", generator.response());

        assertFalse(generator.isClose());
    }

    @Test
    public void testHi() {
        String request = "hi";
        
        ResponseGenerator generator = new ResponseGenerator(request);
        assertNotNull(generator);

        assertNotNull(generator.response());
        assertEquals("�Է��Ͻ� ����� '" + request + "' �Դϱ�?\r\n", generator.response());
        
        assertFalse(generator.isClose());
    }

    @Test
    public void testBye() {
        String request = "bye";
        
        ResponseGenerator generator = new ResponseGenerator(request);
        assertNotNull(generator);

        assertNotNull(generator.response());
        assertEquals("���� �Ϸ� �Ǽ���!\r\n", generator.response());
        assertTrue(generator.isClose());
    }
    
}