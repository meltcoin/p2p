package io.meltcoin.p2p.types;

public interface Middleware {

    MiddlewareResponse onMessage(MiddlewareResponse middlewareResponse);
}
