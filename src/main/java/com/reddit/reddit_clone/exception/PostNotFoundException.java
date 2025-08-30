package com.reddit.reddit_clone.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String string) {
        super(string);
    }
}
