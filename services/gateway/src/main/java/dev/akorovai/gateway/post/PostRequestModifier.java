package dev.akorovai.gateway.post;


import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class PostRequestModifier implements RewriteFunction<PostRequest, PostRequest> {
    @Override
    public Mono<PostRequest> apply(ServerWebExchange exchange, PostRequest body) {

        return Mono.just(body);
    }
}