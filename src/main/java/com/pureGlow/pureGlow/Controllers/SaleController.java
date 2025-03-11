package com.pureGlow.pureGlow.Controllers;

import com.pureGlow.pureGlow.Entities.*;
import com.pureGlow.pureGlow.Services.*;
import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/sale")
@CrossOrigin(origins = "*")
public class SaleController {

    @Autowired
    private SaleService saleService;
    @Autowired
    private SaleDetailService saleDetailService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/all")
    public ResponseEntity<List<Sale>> getAll(){
        try {
            List<Sale> saleList = saleService.findAll();
            if (saleList == null) {
                return null;
            }
            return new ResponseEntity<>(saleList, HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al retornar la lista de ventas");
        }
    }

    @GetMapping("/allClient/{idClient}")
    public ResponseEntity<List<Sale>> getAllClient(@PathVariable Long idClient) {
        try {
            List<Sale> saleList = saleService.findAll();

            if (saleList == null || saleList.isEmpty()) {
                return ResponseEntity.noContent().build(); // Devuelve 204 si no hay ventas
            }

            // Filtrar las ventas por el id del cliente
            List<Sale> filteredAppointments = saleList.stream()
                    .filter(sale -> sale.getClient().getId().equals(idClient))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(filteredAppointments);
        } catch (Exception e) {
            throw new RuntimeException("Error al retornar la lista de ventas", e);
        }
    }

    @GetMapping("/allSeller/{idSeller}")
    public ResponseEntity<List<Sale>> getAllSeller(@PathVariable Long idSeller) {
        try {
            List<Sale> saleList = saleService.findAll();

            if (saleList == null || saleList.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            // Filtrar las ventas por el id del vendedor
            List<Sale> filteredAppointments = saleList.stream()
                    .filter(sale -> sale.getSeller().getId().equals(idSeller))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(filteredAppointments);
        } catch (Exception e) {
            throw new RuntimeException("Error al retornar la lista de ventas", e);
        }
    }

    @GetMapping("/findId/{id}")
    public ResponseEntity<Sale> getById(@PathVariable Long id){
        try {
            Sale sale = saleService.getById(id);
            if (sale == null) {
                return null;
            }
            return new ResponseEntity<>(sale, HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al retornar a la venta");
        }
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Map<String, Object> data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            Sale sale = new Sale();
            Client client = clientService.getById(dataObject.getJSONObject("client").getLong("id"));
            if (client == null) {
                return new ResponseEntity<>("No esta registrado este cliente.", HttpStatus.BAD_REQUEST);
            }
            Seller seller = sellerService.getById(dataObject.getJSONObject("seller").getLong("id"));
            if (seller == null) {
                return new ResponseEntity<>("No esta registrado este vendedor.", HttpStatus.BAD_REQUEST);
            }
            sale.setClient(client);
            sale.setSeller(seller);
            sale.setTotal(0);
            saleService.save(sale);

            JSONArray productArray = dataObject.getJSONArray("products");

            float total = 0;

            for (int i = 0; i < productArray.length(); i++) {
                JSONObject detailObject = productArray.getJSONObject(i);

                SaleDetail saleDetail = new SaleDetail();
                Product product = productService.getById(detailObject.getJSONObject("product").getLong("id"));

                saleDetail.setProduct(product);
                saleDetail.setSale(sale);
                saleDetail.setAmount(detailObject.getJSONObject("product").getInt("amount"));
                saleDetail.setSubTotal(product.getPrice()*saleDetail.getAmount());

                total += saleDetail.getSubTotal();
                saleDetailService.save(saleDetail);
            }

            sale.setTotal((int) total);
            saleService.save(sale);

            return new ResponseEntity<>("Venta registrada exitosamente.", HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al guardar venta." + e.getMessage());
        }
    }

    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@RequestBody Map<String, Object> data, @PathVariable Long id) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            Sale sale = saleService.getById(id);

            Client client = clientService.getById(dataObject.getJSONObject("client").getLong("id"));
            if (client == null) {
                return new ResponseEntity<>("No esta registrado este cliente.", HttpStatus.BAD_REQUEST);
            }
            Seller seller = sellerService.getById(dataObject.getJSONObject("seller").getLong("id"));
            if (seller == null) {
                return new ResponseEntity<>("No esta registrado este vendedor.", HttpStatus.BAD_REQUEST);
            }
            sale.setClient(client);
            sale.setSeller(seller);

            // Eliminar SaleDetail existentes
            saleDetailService.deleteBySale(sale);

            JSONArray productArray = dataObject.getJSONArray("products");

            float total = 0;

            for (int i = 0; i < productArray.length(); i++) {
                JSONObject detailObject = productArray.getJSONObject(i);

                SaleDetail saleDetail = new SaleDetail();
                Product product = productService.getById(detailObject.getJSONObject("product").getLong("id"));

                saleDetail.setProduct(product);
                saleDetail.setSale(sale);
                saleDetail.setAmount(detailObject.getJSONObject("product").getInt("amount"));
                saleDetail.setSubTotal(product.getPrice()*saleDetail.getAmount());

                total += saleDetail.getSubTotal();
                saleDetailService.save(saleDetail);
            }

            sale.setTotal((int) total);
            saleService.save(sale);

            return new ResponseEntity<>("Venta actualizada exitosamente.", HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar venta." + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try {
            Sale sale = saleService.getById(id);
            if (sale == null) {
                return new ResponseEntity<>("No existe esta venta.", HttpStatus.BAD_REQUEST);
            }
            saleService.delete(sale);
            return new ResponseEntity<>("Venta eliminada exitosamente.", HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al eliminar la venta.");
        }
    }
}
