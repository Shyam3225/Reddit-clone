package com.reddit.reddit_clone.service;

import com.reddit.reddit_clone.dto.SubredditDto;
import com.reddit.reddit_clone.exception.SpringRedditException;
import com.reddit.reddit_clone.mapper.SubredditMapper;
import com.reddit.reddit_clone.model.Subreddit;
import com.reddit.reddit_clone.repository.SubredditRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubRedditService {

    private final SubredditRepository subredditRepository;

    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto)
    {
        Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(save.getId());
        return subredditDto;
    }

    private Subreddit mapSubRedditDto(SubredditDto subredditDto)
    {
        return Subreddit.builder().name(subredditDto.getName()).description(subredditDto.getDescription()).build();
    }

    @Transactional
    public Set<SubredditDto> getAll() {
        return subredditRepository.findAll().stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(Collectors.toSet());
    }

    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(()->new SpringRedditException("No Subreddit found with {}id"+id));
        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
