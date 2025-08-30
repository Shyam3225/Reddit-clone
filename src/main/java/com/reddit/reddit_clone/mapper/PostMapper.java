package com.reddit.reddit_clone.mapper;

import com.reddit.reddit_clone.dto.PostRequest;
import com.reddit.reddit_clone.dto.PostResponse;
import com.reddit.reddit_clone.model.Post;
import com.reddit.reddit_clone.model.Subreddit;
import com.reddit.reddit_clone.model.User;
import com.reddit.reddit_clone.repository.CommentRepository;
import com.reddit.reddit_clone.repository.VoteRepository;
import com.reddit.reddit_clone.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface PostMapper {

//    @Autowired
//    private CommentRepository commentRepository;
//    @Autowired
//    private VoteRepository voteRepository;
//    @Autowired
//    private AuthService authService;


    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.userName")
    PostResponse mapToDto(Post post);
//    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
//    @Mapping(target = "duration", expression = "java(getDuration(post))")
//    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
//    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")


//    Integer commentCount(Post post) {
//        return commentRepository.findByPost(post).size();
//    }
//
//    String getDuration(Post post) {
//        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
//    }
//
//    boolean isPostUpVoted(Post post) {
//        return checkVoteType(post, UPVOTE);
//    }
//
//    boolean isPostDownVoted(Post post) {
//        return checkVoteType(post, DOWNVOTE);
//    }
//
//    private boolean checkVoteType(Post post, VoteType voteType) {
//        if (authService.isLoggedIn()) {
//            Optional<Vote> voteForPostByUser =
//                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
//                            authService.getCurrentUser());
//            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
//                    .isPresent();
//        }
//        return false;
//    }

}