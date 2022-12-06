package com.springboot.blog.springbootblogrestapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

// Lombok -> generates getters for all fields, a useful toString method, and hashCode and equals implementations
// that check all non-transient fields. Will also generate setters for all non-final fields, as well as a
// constructor

// if you want to make any field as unique then you can specify inside this unique constraints property

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "posts",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})}
)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "content", nullable = false)
    private String content;

    // set doesn't allow duplicates
    // cascade all -> applicates to all things not only parent
    // we use mappedBy beacuse post is owning relationship
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> commentSet = new HashSet<>();

}
