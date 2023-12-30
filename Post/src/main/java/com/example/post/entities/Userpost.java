package com.example.post.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Userpost {
    private Long id;
    private String nom;
    private String prenom;
    private String password;
    private String username;
    private String photo;
    private String description;
}