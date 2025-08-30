package com.reddit.reddit_clone.repository;

import com.reddit.reddit_clone.model.Post;
import com.reddit.reddit_clone.model.Subreddit;
import com.reddit.reddit_clone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    
  List<Post> findByUser(User user);

    List<Post> findAllBySubreddit(Subreddit subreddit);
}
