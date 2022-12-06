package com.springboot.blog.springbootblogrestapi.Dtos;

import lombok.Data;

@Data
public class CommentDto {

    private long id;
    private String name;
    private String email;
    private String body;
}
