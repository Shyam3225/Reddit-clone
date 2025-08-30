package com.reddit.reddit_clone.service;

import com.reddit.reddit_clone.dto.PostRequest;
import com.reddit.reddit_clone.dto.PostResponse;
import com.reddit.reddit_clone.exception.PostNotFoundException;
import com.reddit.reddit_clone.exception.SpringRedditException;
import com.reddit.reddit_clone.exception.SubredditNotFoundException;
import com.reddit.reddit_clone.mapper.PostMapper;
import com.reddit.reddit_clone.model.Post;
import com.reddit.reddit_clone.model.Subreddit;
import com.reddit.reddit_clone.model.User;
import com.reddit.reddit_clone.repository.PostRepository;
import com.reddit.reddit_clone.repository.SubredditRepository;
import com.reddit.reddit_clone.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final PostMapper postMapper;
    private final UserRepository userRepository;

    public Post save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SpringRedditException(postRequest.getSubredditName() + "Not found"));

        User currentUser = authService.getCurrentUser();
        return postMapper.map(postRequest,subreddit,currentUser);
    }

    @Transactional
    public PostResponse getPost(Long id)
    {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new PostNotFoundException(id.toString()));

        return postMapper.mapToDto(post);
    }

    @Transactional
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
      Subreddit subreddit = subredditRepository.findById(subredditId)
              .orElseThrow(()-> new SubredditNotFoundException(subredditId.toString()));
      List<Post> posts = postRepository.findAllBySubreddit(subreddit);

      return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByUsername(String name) {
        User user = userRepository.findByUserName(name)
                .orElseThrow(()-> new UsernameNotFoundException(name));

        return postRepository.findByUser(user)
                .stream().map(postMapper::mapToDto)
                .collect(Collectors.toList());

    }
}
