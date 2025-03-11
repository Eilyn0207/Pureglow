package com.pureGlow.pureGlow.Controllers;

import com.pureGlow.pureGlow.Entities.Role;
import com.pureGlow.pureGlow.Entities.Seller;
import com.pureGlow.pureGlow.Entities.User;
import com.pureGlow.pureGlow.Services.SellerService;
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
@RequestMapping(path = "/seller")
@CrossOrigin(origins = "*")
public class SellerController {

    @Autowired
    private SellerService sellerService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping("/all")
    public ResponseEntity<List<Seller>> getAll(){
        try {
            List<Seller> sellerList = sellerService.findAll();
            if (sellerList == null) {
                return null;
            }
            return new ResponseEntity<>(sellerList, HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al retornar la lista de vendedores");
        }
    }

    @GetMapping("/findId/{id}")
    public ResponseEntity<Seller> getById(@PathVariable Long id){
        try {
            Seller seller = sellerService.getById(id);
            if (seller == null) {
                return null;
            }
            return new ResponseEntity<>(seller, HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al retornar a el vendedor");
        }
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Map<String, Object> data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            Seller seller = new Seller();
            User user = new User();
            Role role = roleService.getByName("VENDEDOR");

            user.setName(dataObject.getString("name"));
            user.setLastName(dataObject.getString("last_name"));
            user.setEmail(dataObject.getString("email"));
            user.setPhoneNumber(dataObject.getBigInteger("phone_number"));
            user.setPassword(dataObject.getString("password"));
            user.setRole(role);

            userService.save(user);

            seller.setAssignedArea(dataObject.getString("assigned_area"));
            seller.setUser(user);

            sellerService.save(seller);
            return new ResponseEntity<>("Vendedor guardado exitosamente.", HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al guardar a el vendedor.");
        }
    }

    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@RequestBody Map<String, Object> data, @PathVariable Long id){
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            Seller seller = sellerService.getById(id);
            User user = userService.findBySeller(seller);

            user.setName(dataObject.getString("name"));
            user.setLastName(dataObject.getString("last_name"));
            user.setEmail(dataObject.getString("email"));
            user.setPhoneNumber(dataObject.getBigInteger("phone_number"));
            user.setPassword(dataObject.getString("password"));

            userService.save(user);

            seller.setAssignedArea(dataObject.getString("assigned_area"));
            seller.setUser(user);

            sellerService.save(seller);
            return new ResponseEntity<>("Vendedor guardado exitosamente.", HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al guardar a el vendedor.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try {
            Seller seller = sellerService.getById(id);
            if (seller == null) {
                return new ResponseEntity<>("No existe este vendedor.", HttpStatus.BAD_REQUEST);
            }
            this.sellerService.delete(seller);
            return new ResponseEntity<>("Vendedor eliminado exitosamente.", HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al eliminar a el vendedor.");
        }
    }
}
