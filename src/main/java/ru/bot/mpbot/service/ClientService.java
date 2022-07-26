package ru.bot.mpbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bot.mpbot.entity.Client;
import ru.bot.mpbot.exception.NoSuchClientException;
import ru.bot.mpbot.repository.ClientRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository){
        this.clientRepository=clientRepository;
    }

    public Client getClientByTgId(Long tgId){
        Optional<Client> clientOptional = clientRepository.findClientByTgId(tgId);
        return clientOptional.orElse(null);
    }

    public void createClient(Client client){
        clientRepository.save(client);
    }
    public void updateClientWBKey(Long tgId, String wbKey){
        Client client = getClientByTgId(tgId);
        if (client==null){
            throw new NoSuchClientException("tgId = "+tgId);
        }
        client.setWbKey(wbKey);
        clientRepository.save(client);
    }
    public void updateClientOznKey(Long tgId, String oznKey){
        Client client = getClientByTgId(tgId);
        if (client==null){
            throw new NoSuchClientException("tgId = "+tgId);
        }
        client.setOznKey(oznKey);
        clientRepository.save(client);
    }
    public void updateClientOznId(Long tgId, String oznId){
        Client client = getClientByTgId(tgId);
        if (client==null){
            throw new NoSuchClientException("tgId = "+tgId);
        }
        client.setOznId(oznId);
        clientRepository.save(client);
    }

    public void updateUsage(Long tgId){
        Client client = getClientByTgId(tgId);
        client.setUsageDate(LocalDate.now());
        clientRepository.save(client);
    }
}
