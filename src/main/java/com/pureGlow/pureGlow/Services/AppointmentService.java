package com.pureGlow.pureGlow.Services;

import com.pureGlow.pureGlow.Entities.Appointment;
import com.pureGlow.pureGlow.Repositories.AppointmentRepository;
import com.pureGlow.pureGlow.Services.dao.Idao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService implements Idao<Appointment, Long> {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public Appointment getById(Long id) {
        return this.appointmentRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void save(Appointment obje) {
        this.appointmentRepository.save(obje);
    }

    @Transactional
    @Override
    public void delete(Appointment obje) {
        this.appointmentRepository.delete(obje);
    }

    @Override
    public List<Appointment> findAll() {
        return this.appointmentRepository.findAll();
    }
}

