package com.example.favorite.models;


import com.example.favorite.entities.PostFav;
import com.example.favorite.entities.UserFav;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteResponse {
    private  Long id;
    private UserFav userfav;
    private PostFav postfav;

}
