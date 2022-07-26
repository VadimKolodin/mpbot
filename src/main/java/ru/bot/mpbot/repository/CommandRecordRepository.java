package ru.bot.mpbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bot.mpbot.entity.CommandRecord;

public interface CommandRecordRepository extends JpaRepository<CommandRecord, Long> {
}
