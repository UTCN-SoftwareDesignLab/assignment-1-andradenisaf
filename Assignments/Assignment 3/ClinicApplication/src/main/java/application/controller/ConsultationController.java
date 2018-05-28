package application.controller;

import application.entity.Consultation;
import application.entity.Patient;
import application.entity.User;
import application.service.IConsultationService;
import application.service.IPatientService;
import application.service.IUserService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class ConsultationController {

    private IConsultationService consultationService;
    private IUserService userService;
    private IPatientService patientService;
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ConsultationController(IConsultationService consultationService, IUserService userService, IPatientService patientService, SimpMessagingTemplate messagingTemplate) {
        this.consultationService = consultationService;
        this.userService = userService;
        this.patientService = patientService;
        this.messagingTemplate = messagingTemplate;
    }

    @RequestMapping(path = "/consultations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getConsultations() {
        List<Consultation> consultations = consultationService.findAll();

        JSONArray jsonArray = new JSONArray();

        for (Consultation consultation : consultations) {
            JSONObject consultationObject = consultation.toJSON();
            jsonArray.add(consultationObject);
        }

        JSONObject responseJSON = new JSONObject();
        responseJSON.put("consultations", jsonArray);

        System.out.println(responseJSON.toJSONString());

        return responseJSON.toJSONString();
    }

    @RequestMapping(path = "/consultations", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createConsultation(@RequestBody String requestJSON) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) parser.parse(requestJSON);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Consultation consultation = new Consultation();
        consultation.setDescription((String) jsonObject.get("description"));

        String dateString = (String) jsonObject.get("date");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            consultation.setDate(date);
        }

        Long doctorId = (Long) jsonObject.get("doctor");
        User doctor = userService.findById(doctorId);
        if (doctor != null) {
            consultation.setDoctor(doctor);
        } else {
            return new ResponseEntity("{\"message\":\"Role not found.\"}", HttpStatus.NOT_FOUND);
        }

        Long patientId = (Long) jsonObject.get("patient");
        Patient patient = patientService.findById(patientId);
        if (patient != null) {
            consultation.setPatient(patient);
        } else {
            return new ResponseEntity("{\"message\":\"Role not found.\"}", HttpStatus.NOT_FOUND);
        }

        consultation = consultationService.create(consultation);
        return new ResponseEntity(consultation.toJSON().toJSONString(), HttpStatus.CREATED);
    }

    @RequestMapping(path = "/consultations/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateConsultation(@PathVariable("id") Long id, @RequestBody String requestJSON) {
        System.out.println("My id: " + id);
        Consultation consultation = consultationService.findById(id);
        System.out.println("My cons: " + consultation);
        if (consultation == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) parser.parse(requestJSON);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        String dateString = (String) jsonObject.get("date");
        if (dateString != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date date = null;
            try {
                date = dateFormat.parse(dateString);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            if (date != null) {
                consultation.setDate(date);
            }
        }

        Long patientId = (Long) jsonObject.get("patient_id");
        if (patientId != null) {
            Patient patient = patientService.findById(patientId);
            if (patient != null) {
                consultation.setPatient(patient);
            } else {
                return new ResponseEntity("{\"message\":\"Patient not found.\"}", HttpStatus.NOT_FOUND);
            }
        }

        Long doctorId = (Long) jsonObject.get("doctor_id");
        if (doctorId != null) {
            User doctor = userService.findById(doctorId);
            if (doctor != null) {
                consultation.setDoctor(doctor);
            } else {
                return new ResponseEntity("{\"message\":\"Doctor not found.\"}", HttpStatus.NOT_FOUND);
            }
        }

        String description = (String) jsonObject.get("description");
        if (description != null) {
            consultation.setDescription(description);
        }

        consultation = consultationService.update(consultation);
        return new ResponseEntity(consultation.toJSON().toJSONString(), HttpStatus.OK);
    }

    @RequestMapping(path = "/consultations/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteConsultation(@PathVariable("id") Long id) {
        Consultation consultation = consultationService.findById(id);
        if (consultation == null) {
            return new ResponseEntity("{\"message\":\"Consultation not found.\"}", HttpStatus.NOT_FOUND);
        }
        boolean success = consultationService.delete(consultation);
        if (!success) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    @RequestMapping(path = "/notify/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity notifyDoctor(@PathVariable("id") Long doctorId, @RequestBody String jsonString) {
        User doctor = userService.findById(doctorId);
        if (doctor == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Long patientId = (Long) jsonObject.get("patient");
        Patient patient = patientService.findById(patientId);
        if (patient == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        String dateString = (String) jsonObject.get("date");

        String message = "You have an appointment with " + patient.getName() + " - " + dateString + ".";
        System.out.println("To user: " + doctor.getUsername());
        messagingTemplate.convertAndSendToUser(doctor.getUsername(), "/notification", message);

        return new ResponseEntity(HttpStatus.OK);
    }

}
