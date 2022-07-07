package pl.szczurowsky.RatOverNats;

import io.nats.client.*;
import io.nats.client.impl.Headers;
import io.nats.client.impl.NatsMessage;
import pl.szczurowsky.RatOverNats.handler.RatMessageHandler;
import pl.szczurowsky.RatOverNats.packet.Packet;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class RatOverNats {

    private final Connection connection;

    public RatOverNats(List<String> uri, Options options, List<RatMessageHandler<?>> ratMessageHandlers) {
        try {
            this.connection = connect(uri, options);
            subscribe(ratMessageHandlers);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Publish message to NATS.
     * @param chanelName channel name
     * @param packet packet to publish
     */
    public void publish(String chanelName, Packet<?> packet) {
        try {
            Message message = NatsMessage.builder()
                    .subject(chanelName)
                    .headers(new Headers().add("packetId", String.valueOf(packet.getPacketId())))
                    .data(packet.serialize())
                    .build();
            connection.publish(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void subscribe(List<RatMessageHandler<?>> ratMessageHandlers) {
        for (RatMessageHandler<?> ratMessageHandler : ratMessageHandlers) {
            subscribeAsync(ratMessageHandler);
        }
    }

    /**
     * Subscribe to NATS channel in same thread.
     * @param messageHandler message handler
     */
    public void subscribeSync(RatMessageHandler<?> messageHandler) {
        Dispatcher dispatcher = connection.createDispatcher(messageHandler);
        dispatcher.subscribe(messageHandler.getChanelName(), messageHandler);
    }

    private void subscribeAsync(RatMessageHandler<?> messageHandler) {
        String channelName = messageHandler.getChanelName();
        Executor executor = Executors.newCachedThreadPool(r -> new Thread(r, "nats-subscriber-" + channelName));
        executor.execute(() -> {
            Dispatcher dispatcher = connection.createDispatcher(messageHandler);
            dispatcher.subscribe(channelName, messageHandler);
            try {
                messageHandler.getLatch().await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Connection connect(List<String> uri, Options options) throws IOException, InterruptedException {
        if (uri != null)
            return Nats.connect(String.join(",", uri));
        else
            return Nats.connect(options);
    }

}
