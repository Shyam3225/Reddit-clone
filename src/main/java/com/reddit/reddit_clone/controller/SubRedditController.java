package com.reddit.reddit_clone.controller;

import com.reddit.reddit_clone.dto.SubredditDto;
import com.reddit.reddit_clone.service.SubRedditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubRedditController {

    @Autowired
    private final SubRedditService subRedditService;


    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto)
    {
       return ResponseEntity.status(HttpStatus.CREATED).body(subRedditService.save(subredditDto));
    }

    @GetMapping
    public ResponseEntity<Set<SubredditDto>> getAllSubReddits()
    {
       return ResponseEntity.status(HttpStatus.OK).body(subRedditService.getAll());
    }

    @GetMapping("/{id}")
    public   ResponseEntity<SubredditDto> getSubreddit(@PathVariable Long id)
    {
        return ResponseEntity.status(HttpStatus.OK).body(subRedditService.getSubreddit(id));
    }
}
