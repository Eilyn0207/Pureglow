package com.pureGlow.pureGlow.Services;

import com.pureGlow.pureGlow.Entities.Trainer;
import com.pureGlow.pureGlow.Entities.User;
import com.pureGlow.pureGlow.Repositories.TrainerRepository;
import com.pureGlow.pureGlow.Services.dao.Idao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService implements Idao<Trainer, Long> {

    @Autowired
    private TrainerRepository trainerRepository;

    @Override
    public Trainer getById(Long id) {
        return this.trainerRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void save(Trainer obje) {
        this.trainerRepository.save(obje);
    }

    @Transactional
    @Override
    public void delete(Trainer obje) {
        this.trainerRepository.delete(obje);
    }

    @Override
    public List<Trainer> findAll() {
        return this.trainerRepository.findAll();
    }

    public Trainer findByUser(User user){
        return trainerRepository.findByUser(user).orElse(null);
    }

    public boolean existsAppointment(Trainer trainer){
        return trainerRepository.existsByAppointments_Trainer(trainer);
    }
}

