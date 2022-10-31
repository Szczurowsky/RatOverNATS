package pl.szczurowsky.ratovernats.handler;

import io.nats.client.Message;
import io.nats.client.MessageHandler;
import pl.szczurowsky.ratovernats.packet.Packet;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

/**
 * Handler for subscribing to NATS channels.
 * @author Created by: qwerty
 * @param <T> - type of packet payload
 * @see Packet
 */
public abstract class RatMessageHandler<T extends Serializable> implements MessageHandler {

    /**
     * Packet id of packet to be handled.
     */
    private final int packetId;
    /**
     * NATS channel to be subscribed to.
     */
    private final  String chanelName;
    /**
     * Latch for waiting for message to be received.
     */
    private final CountDownLatch latch;

    /**
     * Constructs handler with given packet id and channel name.
     * @param packetId - packet id of packet to be handled
     * @param chanelName - NATS channel to be subscribed to
     */
    protected RatMessageHandler(int packetId, String chanelName) {
        this.packetId = packetId;
        this.chanelName = chanelName;
        this.latch = new CountDownLatch(1);
    }

    /**
     * Constructs handler with given packet id, channel name and latch.
     * @param packetId - packet id of packet to be handled
     * @param chanelName - NATS channel to be subscribed to
     * @param latchCount - number of messages to be received
     */
    protected RatMessageHandler(int packetId, String chanelName, int latchCount) {
        this.packetId = packetId;
        this.chanelName = chanelName;
        this.latch = new CountDownLatch(latchCount);
    }

    /**
     * Getter for packet id.
     * @return packet id
     */
    public String getChanelName() {
        return chanelName;
    }

    /**
     * Getter for latch.
     * @return latch
     */
    public CountDownLatch getLatch() {
        return latch;
    }

    /**
     * Getter for packet id.
     * @return packet id
     */
    public int getPacketId() {
        return packetId;
    }

    /**
     * Method called when message is decapsulated.
     * @param payload - payload of packet
     * @param message - message received
     */
    protected abstract void onReceive(T payload, Message message);

    /**
     * Method called when message is received
     * @param message - message received
     * @throws InterruptedException - exception thrown when thread is interrupted
     */
    @Override
    public void onMessage(Message message) throws InterruptedException {
        if (message == null) return;
        if (message.getHeaders().isEmpty()) return;
        if (!message.getHeaders().containsKey("packetId")) return;
        int receivedPacketId = Integer.parseInt(message.getHeaders().get("packetId").get(0));
        if (receivedPacketId != this.packetId) {
            return;
        }
        try {
            Packet<T> packet = new Packet<>(message.getData());
            onReceive(packet.getPayload(), message);
            latch.countDown();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
