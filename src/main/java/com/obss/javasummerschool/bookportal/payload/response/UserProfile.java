package com.obss.javasummerschool.bookportal.payload.response;


import java.time.Instant;

public class UserProfile {

    private Long id;
    private String username;
    private String name;
    private Instant joinedAt;
    private Long likedCount;
    private Long readCount;

    public UserProfile(Long id, String username, String name, Instant joinedAt, Long likedCount, Long readCount) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.joinedAt = joinedAt;
        this.likedCount = likedCount;
        this.readCount = readCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Long getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(Long likedCount) {
        this.likedCount = likedCount;
    }

    public Long getReadCount() {
        return readCount;
    }

    public void setReadCount(Long readCount) {
        this.readCount = readCount;
    }
}
