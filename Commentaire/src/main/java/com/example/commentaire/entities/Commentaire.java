package com.example.commentaire.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Commentaire {
    @Id
    @GeneratedValue
     private  Long id;
     private String comment;
     private Long user_id;
     private Long post_id;




}
