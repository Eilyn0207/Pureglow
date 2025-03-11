package com.pureGlow.pureGlow.Services;

import com.pureGlow.pureGlow.Entities.Role;
import com.pureGlow.pureGlow.Repositories.RoleRepository;
import com.pureGlow.pureGlow.Services.dao.Idao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements Idao<Role, Long> {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role getById(Long id) {
        return this.roleRepository.findById(id).orElse(null);
    }

    public Role getByName(String name) {
        return this.roleRepository.findByName(name).orElse(null);
    }

    @Transactional
    @Override
    public void save(Role obje) {
        this.roleRepository.save(obje);
    }

    @Transactional
    @Override
    public void delete(Role obje) {
        this.roleRepository.delete(obje);
    }

    @Override
    public List<Role> findAll() {
        return this.roleRepository.findAll();
    }
}

