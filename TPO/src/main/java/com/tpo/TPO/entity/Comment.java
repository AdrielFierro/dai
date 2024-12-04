package com.tpo.TPO.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
//import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @Column
    private Integer postId;

    @Column
    private Integer userId;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    // Método calculado que devuelve el tiempo transcurrido desde la creación
    @Transient
    public String getTimeAgo() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(timestamp, now);

        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        if (days > 0) {
            return days + (days == 1 ? " day ago" : " days ago");
        } else if (hours > 0) {
            return hours + (hours == 1 ? " hour ago" : " hours ago");
        } else if (minutes > 0) {
            return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
        } else {
            return seconds + (seconds == 1 ? " second ago" : " seconds ago");
        }
    }
}
