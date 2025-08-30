package com.reddit.reddit_clone.service;

import com.reddit.reddit_clone.dto.CommentsDto;
import com.reddit.reddit_clone.exception.PostNotFoundException;
import com.reddit.reddit_clone.mapper.CommentMapper;
import com.reddit.reddit_clone.model.Comment;
import com.reddit.reddit_clone.model.NotificationEmail;
import com.reddit.reddit_clone.model.Post;
import com.reddit.reddit_clone.model.User;
import com.reddit.reddit_clone.repository.CommentRepository;
import com.reddit.reddit_clone.repository.PostRepository;
import com.reddit.reddit_clone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final MailService mailService;
    private final MailContentBuilder mailContentBuilder;

    private static final String POST_URL = "";
    private final AuthService authService;
    private final CommentMapper commentMapper;
    
    public void save(CommentsDto commentsDto)
    {
        Post post = postRepository.findById(commentsDto.getPostId()).orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId() + "Not found"));
        User currentUser = authService.getCurrentUser();
        Comment comment = commentMapper.map(commentsDto, post, currentUser);

        commentRepository.save(comment);

        String message = mailContentBuilder.build(post.getUser().getUserName()+" posted a comment on your post"+ POST_URL );
        sendCommentNotification(message, post.getUser());


    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUserName()+" Commented on your post", user.getEmail(), message));
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post).stream().map(commentMapper::mapToDto).collect(toList());


    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException(userName));

        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());

    }
}
