package com.reddit.reddit_clone.exception;

public class SubredditNotFoundException extends RuntimeException {
    public SubredditNotFoundException(String string) {
        super(string);
    }
}
