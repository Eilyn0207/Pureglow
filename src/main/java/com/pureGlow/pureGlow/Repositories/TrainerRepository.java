package com.pureGlow.pureGlow.Repositories;

import com.pureGlow.pureGlow.Entities.Trainer;
import com.pureGlow.pureGlow.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByUser(User user);

    boolean existsByAppointments_Trainer(Trainer trainer);
}
