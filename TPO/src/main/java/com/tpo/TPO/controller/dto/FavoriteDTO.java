package com.tpo.TPO.controller.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FavoriteDTO {
    private Integer userId;
    private Integer postId;
}
