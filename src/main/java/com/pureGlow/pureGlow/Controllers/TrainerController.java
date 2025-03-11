package com.pureGlow.pureGlow.Controllers;

import com.pureGlow.pureGlow.Entities.Role;
import com.pureGlow.pureGlow.Entities.Trainer;
import com.pureGlow.pureGlow.Entities.User;
import com.pureGlow.pureGlow.Services.RoleService;
import com.pureGlow.pureGlow.Services.TrainerService;
import com.pureGlow.pureGlow.Services.UserService;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/trainer")
@CrossOrigin(origins = "*")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping("/all")
    public ResponseEntity<List<Trainer>> getAll(){
        try {
            List<Trainer> trainerList = trainerService.findAll();
            if (trainerList == null) {
                return null;
            }
            return new ResponseEntity<>(trainerList, HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al retornar la lista de capacitadores");
        }
    }

    @GetMapping("/findId/{id}")
    public ResponseEntity<Trainer> getById(@PathVariable Long id){
        try {
            Trainer trainer = trainerService.getById(id);
            if (trainer == null) {
                return null;
            }
            return new ResponseEntity<>(trainer, HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al retornar a el capacitador");
        }
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Map<String, Object> data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            Trainer trainer = new Trainer();
            User user = new User();
            Role role = roleService.getByName("CAPACITADOR");

            user.setName(dataObject.getString("name"));
            user.setLastName(dataObject.getString("last_name"));
            user.setEmail(dataObject.getString("email"));
            user.setPhoneNumber(dataObject.getBigInteger("phone_number"));
            user.setPassword(dataObject.getString("password"));
            user.setRole(role);

            userService.save(user);
            trainer.setUser(user);

            trainerService.save(trainer);
            return new ResponseEntity<>("Capacitador guardado exitosamente.", HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al guardar a el capacitador.");
        }
    }

    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@RequestBody Map<String, Object> data, @PathVariable Long id){
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            Trainer trainer = trainerService.getById(id);
            User user = userService.findByTrainer(trainer);

            user.setName(dataObject.getString("name"));
            user.setLastName(dataObject.getString("last_name"));
            user.setEmail(dataObject.getString("email"));
            user.setPhoneNumber(dataObject.getBigInteger("phone_number"));
            user.setPassword(dataObject.getString("password"));

            userService.save(user);
            trainer.setUser(user);
            trainerService.save(trainer);
            return new ResponseEntity<>("Capacitador guardado exitosamente.", HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al guardar a el capacitador.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try {
            Trainer trainer = trainerService.getById(id);
            if (trainer == null) {
                return new ResponseEntity<>("No existe este capacitador.", HttpStatus.BAD_REQUEST);
            }
            this.trainerService.delete(trainer);
            return new ResponseEntity<>("Capacitador eliminado exitosamente.", HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al eliminar a el capacitador.");
        }
    }
}
