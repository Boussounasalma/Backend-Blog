package com.example.post.controller;

import com.example.post.entities.Post;
import com.example.post.models.PostResponse;
import com.example.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;




    @RestController
    @RequestMapping("/api/posts")
    public class PostController {

        @Autowired
        private PostService postService;

        @GetMapping
        public ResponseEntity<List<PostResponse>> getAllPosts() {
            List<PostResponse> posts = postService.findAll();
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }

        @GetMapping("/{id}")
        public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
            try {
                PostResponse post = postService.findById(id);
                return new ResponseEntity<>(post, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        @PostMapping("/add")
        public ResponseEntity<PostResponse> addPost(@RequestBody Post post) {
            try {
                PostResponse addedPost = postService.addPost(post);
                return new ResponseEntity<>(addedPost, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        @PutMapping("/{id}")
        public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
            try {
                PostResponse updated = postService.updatePost(id, updatedPost);
                return new ResponseEntity<>(updated, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        // Additional methods for creating, updating, and deleting posts can be added as needed.

    }


