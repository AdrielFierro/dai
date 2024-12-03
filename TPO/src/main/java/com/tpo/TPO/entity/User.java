package com.tpo.TPO.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Entity
@Builder
@AllArgsConstructor
public class User implements UserDetails {

    public User() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String description;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Column
    private String urlImage;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @PrePersist
    public void PrePersist() {
        if (genre == null) {
            genre = Genre.NOTSAID;
        }
    }

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Integer> followersIds = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Integer> followedIds = new HashSet<>();

    @Column
    private String backgroundImage;

    @ManyToMany
    @JoinTable(name = "user_fav_posts", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "post_id"))
    @Column
    private List<Post> favPosts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    public void addFollower(Integer userId) {
        this.followersIds.add(userId);
    }

    public void addFollowed(Integer userId) {
        this.followedIds.add(userId);
    }

    public void removeFollower(Integer userId) {
        this.followersIds.remove(userId);
    }

    public void removeFollowed(Integer userId) {
        this.followedIds.remove(userId);
    }

    // Métodos getter y setter para followedIds
    public Set<Integer> getFollowedIds() {
        return followedIds;
    }

    public void setFollowedIds(Set<Integer> followedIds) {
        this.followedIds = followedIds;
    }

    // Métodos getter y setter para followersIds
    public Set<Integer> getFollowersIds() {
        return followersIds;
    }

    public void setFollowersIds(Set<Integer> followersIds) {
        this.followersIds = followersIds;
    }
}
