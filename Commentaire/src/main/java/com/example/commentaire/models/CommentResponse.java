package com.example.commentaire.models;

import com.example.commentaire.entities.PostComment;
import com.example.commentaire.entities.UserComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private  Long id;
    private String comment;
    private Long user_id;
    private Long post_id;
    private UserComment usercomment;
    private PostComment postComment;

}
