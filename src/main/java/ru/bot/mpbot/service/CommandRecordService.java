package ru.bot.mpbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bot.mpbot.entity.Client;
import ru.bot.mpbot.entity.CommandRecord;
import ru.bot.mpbot.exception.NoSuchClientException;
import ru.bot.mpbot.repository.CommandRecordRepository;

@Service
public class CommandRecordService {

    private CommandRecordRepository commandRecordRepository;

    private ClientService clientService;

    @Autowired
    public CommandRecordService(CommandRecordRepository commandRecordRepository,
                                ClientService clientService){
        this.commandRecordRepository=commandRecordRepository;
        this.clientService=clientService;
    }

    /**
     *
     * @param tgId of <font size="5" color="red">SAVED</font> client
     * @param input
     * @param args
     */
    public void saveCommand(Long tgId, String input, String args){
        Client client = clientService.getClientByTgId(tgId);
        if (client == null){
            throw new NoSuchClientException("tgId = "+tgId);
        }
        commandRecordRepository.save(new CommandRecord(null, client, input, args));
    }
}
