package com.example.commentaire.repository;

import com.example.commentaire.entities.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Commentaire, Long > {
}
