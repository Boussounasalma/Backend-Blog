package com.example.favorite.controller;

import com.example.favorite.entities.Favorite;
import com.example.favorite.models.FavoriteResponse;
import com.example.favorite.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public List<FavoriteResponse> getAllComments() {
        return favoriteService.findAll();
    }

    @GetMapping("/{id}")
    public FavoriteResponse getCommentById(@PathVariable Long id) throws Exception {
        return favoriteService.findById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<FavoriteResponse> addFavorite(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        Long postId = request.get("postId");

        try {
            FavoriteResponse addedFavorite = favoriteService.addFavorite(userId, postId);
            return new ResponseEntity<>(addedFavorite, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
