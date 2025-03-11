package com.pureGlow.pureGlow.Services;

import com.pureGlow.pureGlow.Entities.*;
import com.pureGlow.pureGlow.Repositories.UserRepository;
import com.pureGlow.pureGlow.Services.dao.Idao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements Idao<User, Long> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getById(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void save(User obje) {
        this.userRepository.save(obje);
    }

    @Transactional
    @Override
    public void delete(User obje) {
        this.userRepository.delete(obje);
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public User findByClient (Client client){
        return userRepository.findByClient(client).orElse(null);
    }

    public User findBySeller (Seller seller){
        return userRepository.findBySeller(seller).orElse(null);
    }

    public User findByTrainer (Trainer trainer){
        return userRepository.findByTrainer(trainer).orElse(null);
    }

    public User findByEmail (String email){
        return userRepository.findByEmail(email).orElse(null);
    }

    public LoginResponse login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                return new LoginResponse(true, "Inicio de sesión exitoso.", user.getRole().getName(), user);
            } else {
                return new LoginResponse(false, "Contraseña incorrecta.", null, null);
            }
        }
        return new LoginResponse(false, "Usuario no encontrado.", null, null);
    }
}

