package com.example.favorite.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFav {

    private  Long id;
    private String nom;
    private String prenom;
    private String password;
    private String username;
    private String photo;
    private String description;


}
