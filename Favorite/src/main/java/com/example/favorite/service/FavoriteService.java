package com.example.favorite.service;

import com.example.favorite.entities.Favorite;
import com.example.favorite.entities.PostFav;
import com.example.favorite.entities.UserFav;
import com.example.favorite.models.FavoriteResponse;
import com.example.favorite.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String USER_URL = "http://localhost:8888/SERVICE-UTILISATEUR/api/ueser/";
    private final String POST_URL = "http://localhost:8888/SERVICE-POST/api/posts/";

    public List<FavoriteResponse> findAll() {
        List<Favorite> favorites = favoriteRepository.findAll();
        ResponseEntity<UserFav[]> userResponse =
                restTemplate.getForEntity(USER_URL, UserFav[].class);
        UserFav[] usersComment = userResponse.getBody();

        ResponseEntity<PostFav[]> postResponse =
                restTemplate.getForEntity(POST_URL, PostFav[].class);
        PostFav[] postsComment = postResponse.getBody();

        return favorites.stream().map((Favorite favorite) ->
                mapToFavoriteResponse(favorite, usersComment, postsComment)).toList();
    }

    private FavoriteResponse mapToFavoriteResponse(Favorite favorite, UserFav[] usersFav, PostFav[] postsFav) {
        UserFav foundUser = Arrays.stream(usersFav)
                .filter(userFav -> userFav.getId().equals(favorite.getUserfav_id()))
                .findFirst()
                .orElse(null);

        PostFav foundPost = Arrays.stream(postsFav)
                .filter(postFav -> postFav.getId().equals(favorite.getPostfav_id()))
                .findFirst()
                .orElse(null);

        return FavoriteResponse.builder()
                .id(favorite.getId())
                .userfav(foundUser)
                .postfav(foundPost)
                .build();
    }

    public FavoriteResponse findById(Long id) throws Exception {
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new Exception("Invalid Comment Id"));

        UserFav userfav = restTemplate.getForObject(USER_URL + favorite.getUserfav_id(), UserFav.class);
        PostFav postfav = restTemplate.getForObject(POST_URL + favorite.getPostfav_id(), PostFav.class);

        return FavoriteResponse.builder()
                .id(favorite.getId())
                .userfav(userfav)
                .postfav(postfav)
                .build();
    }

    public FavoriteResponse addFavorite(Long userId, Long postId) throws Exception {
        // Check if the user and post exist before creating a favorite relationship
        UserFav userfav = restTemplate.getForObject(USER_URL + userId, UserFav.class);
        if (userfav == null) {
            throw new Exception("User with id " + userId + " not found");
        }

        PostFav postfav = restTemplate.getForObject(POST_URL + postId, PostFav.class);
        if (postfav == null) {
            throw new Exception("Post with id " + postId + " not found");
        }

        // Create a new Favorite entity and save it to the repository
        Favorite newFavorite = new Favorite();
        newFavorite.setUserfav_id(userId);
        newFavorite.setPostfav_id(postId);
        Favorite savedFavorite = favoriteRepository.save(newFavorite);

        // Return the response with the newly created favorite relationship
        return FavoriteResponse.builder()
                .id(savedFavorite.getId())
                .userfav(userfav)
                .postfav(postfav)
                .build();
    }

    public void deleteById(Long id) {
        favoriteRepository.deleteById(id);
    }

}
