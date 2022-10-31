package pl.szczurowsky.ratovernats.packet;

import io.nats.client.Message;
import pl.szczurowsky.ratovernats.RatOverNats;

import java.util.concurrent.CompletableFuture;

/**
 * Class representing response packet with encapsulated instance.
 */
public class ResponseMessage {

    /**
     * RatOverNats instance.
     */
    protected final RatOverNats ratOverNats;

    /**
     * Message received from NATS.
     */
    protected final CompletableFuture<Message> message;

    public ResponseMessage(RatOverNats ratOverNats, CompletableFuture<Message> message) {
        this.ratOverNats = ratOverNats;
        this.message = message;
    }
}
