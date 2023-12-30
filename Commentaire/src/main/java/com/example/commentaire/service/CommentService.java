package com.example.commentaire.service;

import com.example.commentaire.entities.Commentaire;
import com.example.commentaire.entities.PostComment;
import com.example.commentaire.entities.UserComment;
import com.example.commentaire.models.CommentResponse;
import com.example.commentaire.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String USER_URL = "http://localhost:8888/SERVICE-UTILISATEUR/api/ueser/";
    private final String POST_URL = "http://localhost:8888/SERVICE-POST/api/posts/";

    public List<CommentResponse> findAll() {
        List<Commentaire> comments = commentRepository.findAll();
        ResponseEntity<UserComment[]> userResponse =
                restTemplate.getForEntity(USER_URL, UserComment[].class);
        UserComment[] usersComment = userResponse.getBody();

        ResponseEntity<PostComment[]> postResponse =
                restTemplate.getForEntity(POST_URL, PostComment[].class);
        PostComment[] postsComment = postResponse.getBody();

        return comments.stream().map((Commentaire commentaire) ->
                mapToCommentResponse(commentaire, usersComment, postsComment)).toList();
    }

    private CommentResponse mapToCommentResponse(Commentaire comment, UserComment[] usersComment, PostComment[] postsComment) {
        UserComment foundUser = Arrays.stream(usersComment)
                .filter(userComment -> userComment.getId().equals(comment.getUser_id()))
                .findFirst()
                .orElse(null);

        PostComment foundPost = Arrays.stream(postsComment)
                .filter(postComment -> postComment.getId().equals(comment.getPost_id()))
                .findFirst()
                .orElse(null);

        return CommentResponse.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .user_id(comment.getUser_id())
                .post_id(comment.getPost_id())
                .usercomment(foundUser)
                .postComment(foundPost)
                .build();
    }

    public CommentResponse findById(Long id) throws Exception {
        Commentaire comment = commentRepository.findById(id)
                .orElseThrow(() -> new Exception("Invalid Comment Id"));

        UserComment userComment = restTemplate.getForObject(USER_URL + comment.getUser_id(), UserComment.class);
        PostComment postComment = restTemplate.getForObject(POST_URL + comment.getPost_id(), PostComment.class);

        return CommentResponse.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .user_id(comment.getUser_id())
                .post_id(comment.getPost_id())
                .usercomment(userComment)
                .postComment(postComment)
                .build();
    }

    public CommentResponse addComment(Commentaire comment) throws Exception {
        // Check if the user exists before adding a new comment
        UserComment userComment = restTemplate.getForObject(USER_URL + comment.getUser_id(), UserComment.class);
        if (userComment == null) {
            throw new Exception("User with id " + comment.getUser_id() + " not found");
        }

        // Check if the post exists before adding a new comment
        PostComment postComment = restTemplate.getForObject(POST_URL + comment.getPost_id(), PostComment.class);
        if (postComment == null) {
            throw new Exception("Post with id " + comment.getPost_id() + " not found");
        }

        // Save the new comment
        Commentaire savedComment = commentRepository.save(comment);

        // Return the response with the newly created comment and associated user and post details
        return CommentResponse.builder()
                .id(savedComment.getId())
                .comment(savedComment.getComment())
                .user_id(savedComment.getUser_id())
                .post_id(savedComment.getPost_id())
                .usercomment(userComment)
                .postComment(postComment)
                .build();
    }

    
}
