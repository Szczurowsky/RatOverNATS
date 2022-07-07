package pl.szczurowsky.RatOverNats.packet;

import java.io.*;

/**
 * Base class for all packets send by NATS.
 * @param <T> Type of packet payload
 */
public class Packet<T extends Serializable> {

    /**
     * Packet payload
     */
    protected final T payload;
    /**
     * Packet id
     */
    protected int packetId;

    /**
     * Constructor for not-serialized payload
     * @param payload Packet payload
     */
    public Packet(T payload) {
        this.payload = payload;
    }

    /**
     * Constructor for serialized payload
     * @param payload Serialized packet
     * @throws IOException If payload is not serializable
     * @throws ClassNotFoundException If class of payload is not found
     */
    public Packet(byte[] payload) throws IOException, ClassNotFoundException {
        this.payload = this.deserialize(payload);
    }

    /**
     * Setter for packet id
     * @param packetId Packet id
     * @return This packet
     */
    public Packet<T> setPacketId(int packetId) {
        this.packetId = packetId;
        return this;
    }

    /**
     * Getter for packet id
     * @return Packet id
     */
    public int getPacketId() {
        return packetId;
    }

    /**
     * Getter for packet payload
     * @return Packet payload
     */
    public T getPayload() {
        return payload;
    }

    /**
     * Serialize packet to byte array
     * @return Serialized packet
     * @throws IOException If payload is not serializable
     */
    public byte[] serialize() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(payload);
        oos.flush();
        oos.close();
        return baos.toByteArray();
    }

    /**
     * Deserialize packet from byte array
     * @param serialized Serialized packet
     * @return Deserialized packet
     * @throws IOException If payload is not serializable
     * @throws ClassNotFoundException If class of payload is not found
     */
    protected T deserialize(byte[] serialized) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
        ObjectInputStream ois = new ObjectInputStream(bais);
        T payload = (T) ois.readObject();
        ois.close();
        return payload;
    }

}
