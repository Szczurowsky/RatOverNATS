package pl.szczurowsky.ratovernats;

import io.nats.client.Options;
import pl.szczurowsky.ratovernats.handler.RatMessageHandler;

import java.util.*;
import java.util.stream.Collectors;

public final class RatOverNatsBuilder {

    private List<String> uri;
    private Options options;
    private final List<RatMessageHandler<?>> ratMessageHandlers = new ArrayList<>();

    /**
     * Set the NATS server URI.
     * @param uri - nats uri
     * @return RatOverNatsBuilder
     */
    public RatOverNatsBuilder uri(String... uri) {
        this.uri = Arrays.stream(uri).collect(Collectors.toList());
        return this;
    }

    /**
     * Set the NATS options.
     * @param options - nats options
     * @return RatOverNatsBuilder
     */
    public RatOverNatsBuilder options(Options options) {
        this.options = options;
        return this;
    }

    /**
     * Register a RatMessageHandler.
     * @param ratMessageHandler - rat message handler
     * @return RatOverNatsBuilder
     */
    public RatOverNatsBuilder registerHandler(RatMessageHandler<?>... ratMessageHandler) {
        ratMessageHandlers.addAll(Arrays.asList(ratMessageHandler));
        return this;
    }

    /**
     * Build a RatOverNats instance.
     * @return RatOverNats
     */
    public RatOverNats build() {
        return new RatOverNats(uri, options, ratMessageHandlers);
    }

}
