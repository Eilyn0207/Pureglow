package com.pureGlow.pureGlow;

import com.pureGlow.pureGlow.Entities.Role;
import com.pureGlow.pureGlow.Repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Seeders implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public Seeders(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count()==0){
            roleRepository.saveAll(getRoles());
        }
    }

    private List<Role> getRoles(){
        Role admin = new Role();
        admin.setName("ADMINISTRADOR");

        Role trainer = new Role();
        trainer.setName("CAPACITADOR");

        Role seller = new Role();
        seller.setName("VENDEDOR");

        Role client = new Role();
        client.setName("CLIENTE");

        List<Role> roleList = new ArrayList<>();

        roleList.add(admin);
        roleList.add(trainer);
        roleList.add(seller);
        roleList.add(client);

        return roleList;
    }

}
