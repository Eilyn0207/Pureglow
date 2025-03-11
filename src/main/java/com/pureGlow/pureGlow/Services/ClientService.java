package com.pureGlow.pureGlow.Services;

import com.pureGlow.pureGlow.Entities.Client;
import com.pureGlow.pureGlow.Entities.User;
import com.pureGlow.pureGlow.Repositories.ClientRepository;
import com.pureGlow.pureGlow.Services.dao.Idao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService implements Idao<Client, Long> {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Client getById(Long id) {
        return this.clientRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void save(Client obje) {
        this.clientRepository.save(obje);
    }

    @Transactional
    @Override
    public void delete(Client obje) {
        this.clientRepository.delete(obje);
    }

    @Override
    public List<Client> findAll() {
        return this.clientRepository.findAll();
    }

    public Client findByUser(User user){
        return clientRepository.findByUser(user).orElse(null);
    }

    public boolean existsSales(Client client){
        return clientRepository.existsBySales_Client(client);
    }

    public boolean existsAppointment(Client client){
        return clientRepository.existsByAppointments_Client(client);
    }
}

