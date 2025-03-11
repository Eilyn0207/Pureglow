package com.pureGlow.pureGlow.Controllers;

import com.pureGlow.pureGlow.Entities.Client;
import com.pureGlow.pureGlow.Entities.Role;
import com.pureGlow.pureGlow.Entities.User;
import com.pureGlow.pureGlow.Services.ClientService;
import com.pureGlow.pureGlow.Services.RoleService;
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
@RequestMapping(path = "/client")
@CrossOrigin(origins = "*")
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping("/all")
    public ResponseEntity<List<Client>> getAll(){
        try {
            List<Client> clientList = clientService.findAll();
            if (clientList == null) {
                return null;
            }
            return new ResponseEntity<>(clientList, HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al retornar la lista de clientes");
        }
    }

    @GetMapping("/findId/{id}")
    public ResponseEntity<Client> getById(@PathVariable Long id){
        try {
            Client client = clientService.getById(id);
            if (client == null) {
                return null;
            }
            return new ResponseEntity<>(client, HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al retornar a el cliente");
        }
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Map<String, Object> data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            Client client = new Client();
            User user = new User();
            Role role = roleService.getByName("CLIENTE");

            user.setName(dataObject.getString("name"));
            user.setLastName(dataObject.getString("last_name"));
            user.setEmail(dataObject.getString("email"));
            user.setPhoneNumber(dataObject.getBigInteger("phone_number"));
            user.setPassword(dataObject.getString("password"));
            user.setRole(role);

            if (userService.findByEmail(user.getEmail()) != null) {
                return new ResponseEntity<>("Ya hay un usuario con este email.", HttpStatus.BAD_REQUEST);
            }

            userService.save(user);

            client.setAddress(dataObject.getString("address"));
            client.setUser(user);

            clientService.save(client);
            return new ResponseEntity<>("Cliente guardado exitosamente.", HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al guardar a el cliente.");
        }
    }

    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@RequestBody Map<String, Object> data, @PathVariable Long id){
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            Client client = clientService.getById(id);
            User user = userService.findByClient(client);

            user.setName(dataObject.getString("name"));
            user.setLastName(dataObject.getString("last_name"));
            user.setEmail(dataObject.getString("email"));
            user.setPhoneNumber(dataObject.getBigInteger("phone_number"));
            user.setPassword(dataObject.getString("password"));

            userService.save(user);

            client.setAddress(dataObject.getString("address"));
            client.setUser(user);

            clientService.save(client);
            return new ResponseEntity<>("Cliente guardado exitosamente.", HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al guardar a el cliente.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try {
            Client client = clientService.getById(id);
            if (client == null) {
                return new ResponseEntity<>("No existe este cliente.", HttpStatus.BAD_REQUEST);
            }
            this.clientService.delete(client);
            return new ResponseEntity<>("Cliente eliminado exitosamente.", HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al eliminar a el cliente.");
        }
    }
}
