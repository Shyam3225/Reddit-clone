package com.reddit.reddit_clone.mapper;

import com.reddit.reddit_clone.dto.SubredditDto;
import com.reddit.reddit_clone.model.Post;
import com.reddit.reddit_clone.model.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "noOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> numberOfPosts)
    {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts",ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto);
}
