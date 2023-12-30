package com.example.commentaire.controller;


import com.example.commentaire.entities.Commentaire;
import com.example.commentaire.models.CommentResponse;
import com.example.commentaire.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commentaires")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentResponse> getAllComments() {
        return commentService.findAll();
    }

    @GetMapping("/{id}")
    public CommentResponse getCommentById(@PathVariable Long id) throws Exception {
        return commentService.findById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<CommentResponse> addComment(@RequestBody Commentaire comment) {
        try {
            CommentResponse addedComment = commentService.addComment(comment);
            return new ResponseEntity<>(addedComment, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
