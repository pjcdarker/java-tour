package com.pjcdarker.pattern.chain;

import java.util.Optional;

/**
 * @author pjc
 * @create 2016-09-10
 */
public class Chain {

    public static final Chain INSTANCE = new Chain();
    private RequestHandler requestHandle;

    private Chain() {
        requestHandle = DefaultRequest.INSTANCE;
    }

    public Chain next(RequestHandler requestHandle) {
        if (this.requestHandle == null) {
            this.requestHandle = requestHandle;
        } else {
            this.requestHandle.next(requestHandle);
        }
        return this;
    }

    public void handler(HandleType handleType, Chain chain) {
        Optional<RequestHandler> requestHandler = getRequestHandler();
        requestHandler.ifPresent(request -> {
            request.handler(handleType, chain);
        });
    }

    private Optional<RequestHandler> getRequestHandler() {
        if (this.requestHandle == null) {
            return Optional.empty();
        }
        Optional<RequestHandler> nextRequestHandler = getNextRequestHandler();
        if (nextRequestHandler.isPresent()) {
            return nextRequestHandler;
        }
        RequestHandler currentRequestHandler = this.requestHandle;
        this.requestHandle = null;
        return Optional.of(currentRequestHandler);
    }

    private Optional<RequestHandler> getNextRequestHandler() {
        RequestHandler next = this.requestHandle.getNext();
        return Optional.ofNullable(next);
    }
}