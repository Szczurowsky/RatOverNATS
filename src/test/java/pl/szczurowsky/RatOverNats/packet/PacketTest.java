package pl.szczurowsky.RatOverNats.packet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PacketTest {

    private static class TestObject implements Serializable {
        private final UUID id = UUID.randomUUID();
        private String name = "TestObject";
        private HashMap<String, Integer> testHashMap = new HashMap<>();
    }


    @Test
    public void testPacketSerialization() throws IOException, ClassNotFoundException {
        assertEquals("test", new Packet<String>(new Packet<>("test").serialize()).getPayload());
        assertEquals(new UUID(2, 10), new Packet<UUID>(new Packet<>(new UUID(2, 10)).serialize()).getPayload());
    }

    @Test
    public void advancedPacketSerialization() throws IOException, ClassNotFoundException {
        TestObject testObject = new TestObject();
        testObject.name = "Test";
        testObject.testHashMap.put("test", 1);
        TestObject serializedTestObject = new Packet<TestObject>(new Packet<>(testObject).serialize()).getPayload();
        assertEquals(testObject.id, serializedTestObject.id);
        assertEquals(testObject.name, serializedTestObject.name);
        assertEquals(testObject.testHashMap, serializedTestObject.testHashMap);
    }

}