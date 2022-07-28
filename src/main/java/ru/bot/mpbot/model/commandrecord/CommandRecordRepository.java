package ru.bot.mpbot.model.commandrecord;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandRecordRepository extends JpaRepository<CommandRecord, Long> {
}
