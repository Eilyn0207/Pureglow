package com.pureGlow.pureGlow.Controllers;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.BorderRadius;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.pureGlow.pureGlow.Entities.*;
import com.pureGlow.pureGlow.Services.*;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.*;

@RestController
@RequestMapping(path = "/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAll() {
        try {
            List<User> userList = userService.findAll();
            List<Map<String, Object>> responseList = new ArrayList<>();

            for (User user : userList) {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("id", user.getId());
                userMap.put("name", user.getName());
                userMap.put("lastName", user.getLastName());
                userMap.put("email", user.getEmail());
                userMap.put("phoneNumber", user.getPhoneNumber());
                userMap.put("role", user.getRole() != null ? user.getRole().getName() : "Sin rol");
                responseList.add(userMap);
            }
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Error al retornar la lista de usuarios");
        }
    }

    @GetMapping("/export-pdf")
    public ResponseEntity<byte[]> exportUsersToPDF() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // **Fondo Corporativo**
            ClassPathResource resource = new ClassPathResource("static/img/fondo_pdf.jpg");
            if (resource.exists()) {
                Image background = new Image(ImageDataFactory.create(resource.getURL()));
                background.setHorizontalAlignment(HorizontalAlignment.CENTER); // Centrar la imagen
                background.setFixedPosition(0, 0);
                background.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
                document.add(background);
            } else {
                System.err.println("La imagen fondo_pdf.jpg no se encontró en la ruta: static/img/fondo_pdf.jpg");
            }

            // **Título del PDF**
            document.add(new Paragraph("Lista de Usuarios")
                    .setBold()
                    .setFontSize(24)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20)
                    .setFontColor(ColorConstants.BLACK));

            // **Tabla de Usuarios**
            Table table = new Table(5);
            table.setWidth(UnitValue.createPercentValue(100));
            table.setMarginBottom(20);
            table.setBorder(Border.NO_BORDER);
            table.setBorderRadius(new BorderRadius(50));

            // **Encabezados de la tabla**
            String[] headers = {"Nombre", "Apellido", "Correo", "Teléfono", "Rol"};
            for (String header : headers) {
                Cell cell = new Cell()
                        .add(new Paragraph(header).setBold().setFontSize(12))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .setTextAlignment(TextAlignment.CENTER);
                table.addCell(cell);
            }

            // **Obtener Usuarios**
            List<User> users = userService.findAll();
            for (User user : users) {
                table.addCell(new Cell().add(new Paragraph(user.getName())).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(user.getLastName())).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(user.getEmail())).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(user.getPhoneNumber()))).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(user.getRole().getName())).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(ColorConstants.WHITE));
            }

            document.add(table);
            document.close();

            // **Configurar la respuesta con el PDF**
            byte[] pdfBytes = outputStream.toByteArray();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_PDF);
            httpHeaders.setContentDisposition(ContentDisposition.attachment().filename("Usuarios.pdf").build());

            return new ResponseEntity<>(pdfBytes, httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/findId/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id){
        try {
            User user = userService.getById(id);
            if (user == null) {
                return null;
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al retornar a el usuario");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody Map<String, Object> data) {
        JSONObject jsonObject = new JSONObject(data);
        LoginResponse response = userService.login(jsonObject.getString("email"), jsonObject.getString("password"));
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Map<String, Object> data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            User user = new User();
            Role role = roleService.getById(dataObject.getJSONObject("rol").getLong("id"));

            user.setName(dataObject.getString("name"));
            user.setLastName(dataObject.getString("last_name"));
            user.setEmail(dataObject.getString("email"));
            user.setPhoneNumber(dataObject.getBigInteger("phone_number"));
            user.setPassword(dataObject.getString("password"));
            user.setRole(role);
            if (userService.findByEmail(user.getEmail()) != null) {
                return new ResponseEntity<>("Ya hay un usuario guardado con este email.", HttpStatus.OK);
            }
            userService.save(user);

            if (role.getName().equals("CAPACITADOR")) {
                Trainer trainer = new Trainer();
                trainer.setUser(user);
                trainerService.save(trainer);
            } else if (role.getName().equals("VENDEDOR")) {
                Seller seller = new Seller();
                seller.setUser(user);
                seller.setAssignedArea(dataObject.getString("area_asignada"));
                sellerService.save(seller);
            } else if (role.getName().equals("CLIENTE")) {
                Client client = new Client();
                client.setUser(user);
                client.setAddress(dataObject.getString("address"));
                clientService.save(client);
            }

            return new ResponseEntity<>("usuarios guardado exitosamente.", HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al guardar a el usuario."+ e.getMessage());
        }
    }

    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@RequestBody Map<String, Object> data, @PathVariable Long id){
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            User user = userService.getById(id);

            if (user == null) {
                return new ResponseEntity<>("Usuario no encontrado.", HttpStatus.NOT_FOUND);
            }

            Role role = roleService.getById(dataObject.getJSONObject("rol").getLong("id"));

            user.setName(dataObject.getString("name"));
            user.setLastName(dataObject.getString("last_name"));
            user.setEmail(dataObject.getString("email"));
            user.setPhoneNumber(dataObject.getBigInteger("phone_number"));
            user.setPassword(dataObject.getString("password"));
            user.setRole(role);

            userService.save(user); // Guardar los cambios del usuario

            // Manejar las entidades asociadas según el rol
            if (role.getName().equals("CAPACITADOR")) {
                Trainer trainer = trainerService.findByUser(user);
                if (trainer == null) {
                    trainer = new Trainer();
                    trainer.setUser(user);
                }
                trainerService.save(trainer);
            } else if (role.getName().equals("VENDEDOR")) {
                Seller seller = sellerService.findByUser(user);
                if (seller == null) {
                    seller = new Seller();
                    seller.setUser(user);
                }
                seller.setAssignedArea(dataObject.getString("area_asignada"));
                sellerService.save(seller);
            } else if (role.getName().equals("CLIENTE")) {
                Client client = clientService.findByUser(user);
                if (client == null) {
                    client = new Client();
                    client.setUser(user);
                }
                client.setAddress(dataObject.getString("address"));
                clientService.save(client);
            }

            return new ResponseEntity<>("Usuario actualizado exitosamente.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar el usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try {
            User user = userService.getById(id); // Obtener el usuario existente

            if (user == null) {
                return new ResponseEntity<>("Usuario no encontrado.", HttpStatus.NOT_FOUND);
            }

            Trainer trainer = trainerService.findByUser(user);
            if (trainer != null) {
                if (trainerService.existsAppointment(trainer)) {
                    return new ResponseEntity<>("Citas relacionadas con el usuario.", HttpStatus.BAD_REQUEST);
                }
                trainerService.delete(trainer);
            }
            Seller seller = sellerService.findByUser(user);
            if (seller != null) {
                if (sellerService.existsSales(seller)) {
                    return new ResponseEntity<>("Ventas relacionadas con el usuario.", HttpStatus.BAD_REQUEST);
                }
                sellerService.delete(seller);
            }
            Client client = clientService.findByUser(user);
            if (client != null) {
                if (clientService.existsSales(client)) {
                    return new ResponseEntity<>("Ventas relacionadas con el usuario.", HttpStatus.BAD_REQUEST);
                }
                if (clientService.existsAppointment(client)) {
                    return new ResponseEntity<>("Citas relacionadas con el usuario.", HttpStatus.BAD_REQUEST);
                }
                clientService.delete(client);
            }

            userService.delete(user); // Eliminar el usuario

            return new ResponseEntity<>("Usuario eliminado exitosamente.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar el usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
