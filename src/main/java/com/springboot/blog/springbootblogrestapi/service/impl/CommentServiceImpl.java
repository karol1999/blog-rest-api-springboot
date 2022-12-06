package com.springboot.blog.springbootblogrestapi.service.impl;

import com.springboot.blog.springbootblogrestapi.Dtos.CommentDto;
import com.springboot.blog.springbootblogrestapi.exception.BlogAPIException;
import com.springboot.blog.springbootblogrestapi.exception.ResourceNotFoundException;
import com.springboot.blog.springbootblogrestapi.model.Comment;
import com.springboot.blog.springbootblogrestapi.model.Post;
import com.springboot.blog.springbootblogrestapi.repository.CommentRepository;
import com.springboot.blog.springbootblogrestapi.repository.PostRepository;
import com.springboot.blog.springbootblogrestapi.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// service class and it is ready for autodetection while component scanning
// and we will inject this class or we autowire this class in other class
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);

        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // set post to comment entity
        comment.setPost(post);

        // save comment entity to db
        // save method returns saved entity
        Comment newComment = commentRepository.save(comment);

        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        // retrieve comments by postId
        List<Comment> comments = commentRepository.findByPostId(postId);

        // convert list of comment entities to list of comments dto's
        return comments.stream().map(comment -> mapToDTO(comment))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {

        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", commentId)
        );

        if(!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment doesnt not belong to post");
        }

        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, long commentId, CommentDto commentRequest) {

        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", commentId)
        );

        // check if comment matches with post
        if(!comment.getPost().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);

        return mapToDTO(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {

        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", commentId)
        );

        // check if comment matches with post
        if(!comment.getPost().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }

        // not delete anything
        commentRepository.delete(comment);


    }

    private CommentDto mapToDTO(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());

        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        return comment;
    }
}
