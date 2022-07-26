package ru.bot.mpbot.model.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository  extends JpaRepository<Client, Long> {

    @Query("SELECT s from Client s WHERE s.tgId= ?1")
    Optional<Client> findClientByTgId(Long tgId);
}
