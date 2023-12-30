package com.example.post.service;

import com.example.post.entities.Post;
import com.example.post.entities.Userpost;
import com.example.post.models.PostResponse;
import com.example.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RestTemplate restTemplate;
    private final String URL="http://localhost:8888/SERVICE-UTILISATEUR";

    public List<PostResponse> findAll() {
        List<Post> posts = postRepository.findAll();
        ResponseEntity<Userpost []> response =
                restTemplate.getForEntity(this.URL+"/api/ueser/", Userpost [].class);
        Userpost[] userspost = response.getBody();
        return posts.stream().map((Post post) ->
                mapToPostResponse(post, userspost)).toList ();
}
    private PostResponse mapToPostResponse(Post post, Userpost[] userspost) {
        Userpost foundUser = Arrays.stream(userspost)
                .filter(userpost -> userpost.getId().equals(post.getUser_id()))
                .findFirst ()
                . orElse(null);

        return PostResponse. builder ()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .ville(post.getVille())
                .pays(post.getPays())
                .imagepost(post.getImagepost())
                .date_poste(post.getDate_poste())
                .userpost(foundUser)
                .build();

}
    public PostResponse findById(Long id) throws Exception {
        Post post = postRepository.findById(id).orElseThrow(() -> new
                Exception("Invalid Post Id"));
        Userpost userpost =
                restTemplate.getForObject(this.URL + "/api/ueser/" + post.getUser_id(), Userpost.class);
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .ville(post.getVille())
                .pays(post.getPays())
                .imagepost(post.getImagepost())
                .date_poste(post.getDate_poste())
                .userpost(userpost)
                .build();
    }

    public PostResponse addPost(Post post) throws Exception {
        // Check if the user exists before adding a new post
        Userpost userpost = restTemplate.getForObject(this.URL+"/api/ueser/" + post.getUser_id(), Userpost.class);
        if (userpost == null) {
            throw new Exception("User with id " + post.getUser_id() + " not found");
        }

        // Save the new post
        Post savedPost = postRepository.save(post);

        // Return the response with the newly created post and user details
        return PostResponse.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .description(savedPost.getDescription())
                .ville(savedPost.getVille())
                .pays(savedPost.getPays())
                .imagepost(savedPost.getImagepost())
                .date_poste(savedPost.getDate_poste())
                .userpost(userpost)
                .build();
    }

    public PostResponse updatePost(Long postId, Post updatedPost) throws Exception {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new Exception("Post not found with id: " + postId));

        // Check if the user exists before updating the post
        Userpost userpost = restTemplate.getForObject(this.URL + "/api/ueser/" + updatedPost.getUser_id(), Userpost.class);
        if (userpost == null) {
            throw new Exception("User with id " + updatedPost.getUser_id() + " not found");
        }

        // Update the existing post with new details
        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setDescription(updatedPost.getDescription());
        existingPost.setVille(updatedPost.getVille());
        existingPost.setPays(updatedPost.getPays());
        existingPost.setImagepost(updatedPost.getImagepost());
        existingPost.setDate_poste(updatedPost.getDate_poste());
        existingPost.setUser_id(updatedPost.getUser_id());

        // Save the updated post
        Post updated = postRepository.save(existingPost);

        // Return the response with the updated post and user details
        return PostResponse.builder()
                .id(updated.getId())
                .title(updated.getTitle())
                .description(updated.getDescription())
                .ville(updated.getVille())
                .pays(updated.getPays())
                .imagepost(updated.getImagepost())
                .date_poste(updated.getDate_poste())
                .userpost(userpost)
                .build();
    }
}
