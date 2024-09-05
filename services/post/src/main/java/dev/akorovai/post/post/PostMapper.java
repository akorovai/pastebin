package dev.akorovai.post.post;

import dev.akorovai.post.hash.PostHashResponse;
import dev.akorovai.post.redis.PostCache;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "hash", ignore = true)
    @Mapping(target = "s3Url", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    PostResponse postRequestToPostResponse(PostRequest request);

    @Mapping(target = "s3Url", ignore = true)
    PostResponse postHashResponseToPostResponse(PostHashResponse response);

    PostCache postResponseToPostCache(PostResponse response);

    @Mapping(target = "s3Url", source = "presignedUrl")
    PostResponse toPostResponse(PostHashResponse response, String userId, String presignedUrl);



    default String map(UUID value) {
        return value.toString();
    }
}