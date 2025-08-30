package com.reddit.reddit_clone.dto;

import com.reddit.reddit_clone.model.VoteType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteDto {
    private VoteType voteType;
    private Long postId;
}
