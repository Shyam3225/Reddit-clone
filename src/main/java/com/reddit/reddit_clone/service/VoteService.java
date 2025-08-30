package com.reddit.reddit_clone.service;

import com.reddit.reddit_clone.dto.VoteDto;
import com.reddit.reddit_clone.exception.PostNotFoundException;
import com.reddit.reddit_clone.exception.SpringRedditException;
import com.reddit.reddit_clone.model.Post;
import com.reddit.reddit_clone.model.Vote;
import com.reddit.reddit_clone.repository.PostRepository;
import com.reddit.reddit_clone.repository.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.reddit.reddit_clone.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;


    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(voteDto.toString()));
        Optional<Vote> optionalVote = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if(optionalVote.isPresent() && optionalVote.get().getVoteType().equals(voteDto.getVoteType()))
        {
            throw new SpringRedditException("You have already "+ voteDto.getVoteType()+ "d for this post");
        }

        if(UPVOTE.equals(voteDto.getVoteType()))
        {
            post.setVoteCount(post.getVoteCount()+1);
        }
        else
        {
            post.setVoteCount(post.getVoteCount()-1);
        }

        voteRepository.save(mapToVote(voteDto,post));
        postRepository.save(post);

    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder().voteType(voteDto.getVoteType()).post(post).user(authService.getCurrentUser()).build();
    }
}
