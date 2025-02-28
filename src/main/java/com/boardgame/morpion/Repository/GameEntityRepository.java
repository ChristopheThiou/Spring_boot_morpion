package com.boardgame.morpion.repository;

import com.boardgame.morpion.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GameEntityRepository extends JpaRepository<GameEntity, String> {
}