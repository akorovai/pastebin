package dev.akorovai.post.redis;

import lombok.Builder;

@Builder
public record RatingObject (String number, String hash){

}
