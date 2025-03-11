package com.pureGlow.pureGlow.Controllers;

import com.pureGlow.pureGlow.Entities.Appointment;
import com.pureGlow.pureGlow.Entities.Client;
import com.pureGlow.pureGlow.Entities.Trainer;
import com.pureGlow.pureGlow.Services.AppointmentService;
import com.pureGlow.pureGlow.Services.ClientService;
import com.pureGlow.pureGlow.Services.TrainerService;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/appointment")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TrainerService trainerService;

    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> getAll(){
        try {
            List<Appointment> appointmentList = appointmentService.findAll();
            if (appointmentList == null) {
                return null;
            }
            return new ResponseEntity<>(appointmentList, HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al retornar la lista de citas");
        }
    }

    @GetMapping("/allClient/{idClient}")
    public ResponseEntity<List<Appointment>> getAllClient(@PathVariable Long idClient) {
        try {
            List<Appointment> appointmentList = appointmentService.findAll();

            if (appointmentList == null || appointmentList.isEmpty()) {
                return ResponseEntity.noContent().build(); // Devuelve 204 si no hay citas
            }

            // Filtrar las citas por el id del cliente
            List<Appointment> filteredAppointments = appointmentList.stream()
                    .filter(appointment -> appointment.getClient().getId().equals(idClient))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(filteredAppointments);
        } catch (Exception e) {
            throw new RuntimeException("Error al retornar la lista de citas", e);
        }
    }


    @GetMapping("/findId/{id}")
    public ResponseEntity<Appointment> getById(@PathVariable Long id){
        try {
            Appointment appointment = appointmentService.getById(id);
            if (appointment == null) {
                return null;
            }
            return new ResponseEntity<>(appointment, HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al retornar la cita");
        }
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Map<String, Object> data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            Appointment appointment = new Appointment();
            Client client = clientService.getById(dataObject.getJSONObject("client").getLong("id"));
            Trainer trainer = trainerService.getById(dataObject.getJSONObject("trainer").getLong("id"));

            appointment.setDescription(dataObject.getString("description"));
            appointment.setDateTime(LocalDateTime.parse(dataObject.getString("dateTime")));
            appointment.setClient(client);
            appointment.setTrainer(trainer);

            appointmentService.save(appointment);
            return new ResponseEntity<>("Cita guardada exitosamente.", HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al guardar la cita.");
        }
    }

    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@RequestBody Map<String, Object> data, @PathVariable Long id){
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            Appointment appointment = appointmentService.getById(id);
            Client client = clientService.getById(dataObject.getJSONObject("client").getLong("id"));
            Trainer trainer = trainerService.getById(dataObject.getJSONObject("trainer").getLong("id"));

            appointment.setDescription(dataObject.getString("description"));
            appointment.setDateTime(LocalDateTime.parse(dataObject.getString("dateTime")));
            appointment.setClient(client);
            appointment.setTrainer(trainer);

            appointmentService.save(appointment);
            return new ResponseEntity<>("Cita guardada exitosamente.", HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al guardar la cita." + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try {
            Appointment appointment = appointmentService.getById(id);
            if (appointment == null) {
                return new ResponseEntity<>("No existe esta cita.", HttpStatus.BAD_REQUEST);
            }
            this.appointmentService.delete(appointment);
            return new ResponseEntity<>("Cita eliminado exitosamente.", HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException("Error al eliminar la cita.");
        }
    }
}
