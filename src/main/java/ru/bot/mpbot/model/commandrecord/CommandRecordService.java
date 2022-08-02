package ru.bot.mpbot.model.commandrecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bot.mpbot.model.client.Client;
import ru.bot.mpbot.model.client.ClientService;
import ru.bot.mpbot.model.client.NoSuchClientException;

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
        commandRecordRepository.save(new CommandRecord(null, client, input));
    }
}
