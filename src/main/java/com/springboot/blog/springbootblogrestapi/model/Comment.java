package com.springboot.blog.springbootblogrestapi.model;

import com.springboot.blog.springbootblogrestapi.model.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String email;
    private String body;

    // many comments to one post
    // FetchType.LAZY tells Hibernate to only fetch the related entities from
    // the database when you use the relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false) // specify foreign key
    private Post post;
}
