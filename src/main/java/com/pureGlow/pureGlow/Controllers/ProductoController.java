package com.pureGlow.pureGlow.Controllers;

import com.pureGlow.pureGlow.Entities.Client;
import com.pureGlow.pureGlow.Entities.Product;
import com.pureGlow.pureGlow.Services.ClientService;
import com.pureGlow.pureGlow.Services.ProductService;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/product")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAll(){
        try {
            List<Product> productList = productService.findAll();
            if (productList == null) {
                return null;
            }
            return new ResponseEntity<>(productList, HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al retornar la lista de productos");
        }
    }

    @GetMapping("/findId/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id){
        try {
            Product product = productService.getById(id);
            if (product == null) {
                return null;
            }
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al retornar a el producto");
        }
    }

    @GetMapping("/carrito/{idClient}")
    public ResponseEntity<List<Product>> getByCarrito(@PathVariable Long idClient){
        try {
            Client client = clientService.getById(idClient);

            List<Product> productList = client.getProducts();
            return new ResponseEntity<>(productList, HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al retornar a los productos que tiene en el carrito");
        }
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Map<String, Object> data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            Product product = new Product();

            product.setName(dataObject.getString("name"));
            product.setDescription(dataObject.getString("description"));
            product.setPrice(dataObject.getFloat("price"));

            // Obtener la cadena Base64 de la imagen
            String base64Image = dataObject.getString("imagen");

            // Decodificar la cadena Base64 a un arreglo de bytes
            byte[] decodedImage = Base64.getDecoder().decode(base64Image);

            // Asignar los datos de la imagen al producto
            product.setImagen(decodedImage);

            productService.save(product);
            return new ResponseEntity<>("Producto guardado exitosamente.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir la traza de la excepción para depuración
            throw new RuntimeException("Error al guardar el producto: " + e.getMessage());
        }
    }

    @Transactional
    @PostMapping("/addCarrito")
    public ResponseEntity<String> addCarrito(@RequestBody Map<String, Object> data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            Product productToAdd = productService.getById(dataObject.getJSONObject("product").getLong("id"));
            Client client = clientService.getById(dataObject.getJSONObject("client").getLong("id"));

            List<Product> productList = client.getProducts();

            // Validar si el producto ya existe en la lista
            boolean productExists = productList.stream()
                    .anyMatch(product -> product.getId().equals(productToAdd.getId()));

            if (productExists) {
                return new ResponseEntity<>("El producto ya está en el carrito.", HttpStatus.BAD_REQUEST);
            }

            productList.add(productToAdd);
            client.setProducts(productList);
            clientService.save(client);

            return new ResponseEntity<>("Producto guardado en el carrito exitosamente.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error al guardar el producto: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @DeleteMapping("/deleteCarrito")
    public ResponseEntity<String> deleteCarrito(@RequestBody Map<String, Object> data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            Product productToAdd = productService.getById(dataObject.getJSONObject("product").getLong("id"));
            Client client = clientService.getById(dataObject.getJSONObject("client").getLong("id"));

            List<Product> productList = client.getProducts();

            // Validar si el producto ya existe en la lista
            boolean productExists = productList.stream()
                    .anyMatch(product -> product.getId().equals(productToAdd.getId()));

            if (!productExists) {
                return new ResponseEntity<>("El producto no está en el carrito.", HttpStatus.BAD_REQUEST);
            }

            productList.remove(productToAdd);
            client.setProducts(productList);
            clientService.save(client);

            return new ResponseEntity<>("Producto eliminado del carrito exitosamente.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error al eliminar el producto: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@RequestBody Map<String, Object> data, @PathVariable Long id){
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            Product product = productService.getById(id);

            product.setName(dataObject.getString("name"));
            product.setDescription(dataObject.getString("description"));
            product.setPrice(dataObject.getFloat("price"));

            // Obtener la cadena Base64 de la imagen
            String base64Image = dataObject.getString("imagen");

            // Decodificar la cadena Base64 a un arreglo de bytes
            byte[] decodedImage = Base64.getDecoder().decode(base64Image);

            // Asignar los datos de la imagen al producto
            product.setImagen(decodedImage);

            productService.save(product);
            return new ResponseEntity<>("Producto guardado exitosamente.", HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al guardar el producto.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try {
            Product product = productService.getById(id);
            if (product == null) {
                return new ResponseEntity<>("No existe este producto.", HttpStatus.BAD_REQUEST);
            }
            if (productService.existsSaleWithProduct(product)) {
                System.out.println("Este producto no se puede eliminar porque tiene ventas asociadas.");
                return new ResponseEntity<>("Este producto no se puede eliminar porque tiene ventas asociadas.", HttpStatus.BAD_REQUEST);
            }
            this.productService.delete(product);
            return new ResponseEntity<>("Producto eliminado exitosamente.", HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al eliminar el producto.");
        }
    }

    @PostMapping("/uploadExcel")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return new ResponseEntity<>("El archivo está vacío.", HttpStatus.BAD_REQUEST);
            }

            // Procesar el archivo Excel
            List<Product> products = productService.processExcel(file);

            // Guardar los productos en la base de datos
            productService.saveAll(products);

            return new ResponseEntity<>("Productos cargados exitosamente.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error al procesar el archivo: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
