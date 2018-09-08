package com.pjcdarker.pattern.chain;

import java.util.Objects;

/**
 * @author pjc
 * @create 2016-09-10
 */
public abstract class RequestHandler {

    private RequestHandler next;

    RequestHandler next(RequestHandler requestHandler) {
        Objects.requireNonNull(requestHandler, " param requestHandler require nonNull ");
        if (this.next == null) {
            this.next = requestHandler;
            return this.next;
        }
        RequestHandler handle = this.next;
        handle.next = requestHandler;
        this.next = handle;
        return this.next;
    }

    RequestHandler getNext() {
        return this.next;
    }

    public abstract void handler(HandleType handleType, Chain chain);

}
