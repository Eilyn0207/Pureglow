package com.pureGlow.pureGlow.Controllers;

import com.pureGlow.pureGlow.Entities.Role;
import com.pureGlow.pureGlow.Services.RoleService;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/role")
@CrossOrigin(origins = "*")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/all")
    public ResponseEntity<List<Role>> getAll(){
        try {
            List<Role> roleList = roleService.findAll();
            if (roleList == null) {
                return null;
            }
            return new ResponseEntity<>(roleList, HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al retornar la lista de roles");
        }
    }

    @GetMapping("/findId/{id}")
    public ResponseEntity<Role> getById(@PathVariable Long id){
        try {
            Role role = roleService.getById(id);
            if (role == null) {
                return null;
            }
            return new ResponseEntity<>(role, HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al retornar a el rol");
        }
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Map<String, Object> data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            Role role = new Role();

            role.setName(dataObject.getString("name"));

            roleService.save(role);
            return new ResponseEntity<>("Rol guardado exitosamente.", HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al guardar el rol.");
        }
    }

    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@RequestBody Map<String, Object> data, @PathVariable Long id){
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            Role role = roleService.getById(id);

            role.setName(dataObject.getString("name"));

            roleService.save(role);
            return new ResponseEntity<>("Rol guardado exitosamente.", HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al guardar el rol.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try {
            Role role = roleService.getById(id);
            if (role == null) {
                return new ResponseEntity<>("No existe este rol.", HttpStatus.BAD_REQUEST);
            }
            this.roleService.delete(role);
            return new ResponseEntity<>("Rol eliminado exitosamente.", HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al eliminar el rol.");
        }
    }
}
