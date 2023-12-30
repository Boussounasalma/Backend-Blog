package com.example.post.models;


import com.example.post.entities.Userpost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String description;
    private String ville;
    private String pays;
    private String imagepost;
    private Date date_poste;
    private Userpost userpost;
}
