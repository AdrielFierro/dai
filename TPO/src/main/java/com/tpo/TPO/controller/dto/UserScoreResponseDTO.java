package com.tpo.TPO.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class UserScoreResponseDTO {
    private int score;
    private int cantComment;
    private int cantPost;
}
