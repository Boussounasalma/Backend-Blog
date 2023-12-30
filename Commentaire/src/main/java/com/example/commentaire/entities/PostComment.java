package com.example.commentaire.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class PostComment {
    private Long id;
    private String title;
    private String description;
    private Date date_poste;

}
