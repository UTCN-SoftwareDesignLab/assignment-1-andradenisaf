package application.controller;

import application.entity.Consultation;
import application.entity.Patient;
import application.service.IConsultationService;
import application.service.IPatientService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class PatientController {

    @GetMapping(value = "/")
    public String patientPage(Model model) {
        return "";
    }


    private IPatientService patientService;
    private IConsultationService consultationService;

    @Autowired
    public PatientController(IPatientService patientService, IConsultationService consultationService) {
        this.patientService = patientService;
        this.consultationService = consultationService;
    }

    @RequestMapping(path = "/patients", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPatients(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "id_card_number", required = false) String idCardNumber,
                              @RequestParam(value = "cnp", required = false) String cnp) {
        List<Patient> patients = new ArrayList<>();
        if (idCardNumber != null) {
            Patient patient = patientService.findByIdentityCardNo(idCardNumber);
            if (patient != null) {
                patients.add(patient);
            }
        } else if (cnp != null) {
            Patient patient = patientService.findByCNP(cnp);
            if (patient != null) {
                patients.add(patient);
            }
        } else if (name != null) {
            patients = patientService.findAllByName(name);
        } else {
            patients = patientService.findAll();
        }

        JSONArray jsonArray = new JSONArray();

        for (Patient patient : patients) {
            JSONObject patientObject = patient.toJSON();
            jsonArray.add(patientObject);
        }

        JSONObject responseJSON = new JSONObject();
        responseJSON.put("patients", jsonArray);

        System.out.println("JSON: " + responseJSON.toJSONString());
        return responseJSON.toJSONString();
    }

    @RequestMapping(path = "/patients", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createPatient(@RequestBody String requestJSON) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) parser.parse(requestJSON);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Patient patient = new Patient();
        patient.setName((String) jsonObject.get("name"));
        patient.setIdentityCardNo((String) jsonObject.get("identityCardNo"));
        patient.setCNP((String) jsonObject.get("cnp"));

        String dateString = (String) jsonObject.get("date");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            patient.setDateOfBirth(date);
        }

        patient.setAddress((String) jsonObject.get("address"));

        patient = patientService.create(patient);
        return new ResponseEntity(patient.toJSON().toJSONString(), HttpStatus.CREATED);
    }

    @RequestMapping(path = "/patients/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updatePatient(@PathVariable("id") Long id, @RequestBody String requestJSON) {
        Patient patient = patientService.findById(id);
        if (patient == null) {
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

        String name = (String) jsonObject.get("name");
        if (name != null) {
            patient.setName(name);
        }
        String identityCardNo = (String) jsonObject.get("identityCardNo");
        if (identityCardNo != null) {
            patient.setIdentityCardNo(identityCardNo);
        }
        String cnp = (String) jsonObject.get("cnp");
        if (cnp != null) {
            patient.setCNP(cnp);
        }

        String dateString = (String) jsonObject.get("date");
        if (dateString != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;
            try {
                date = dateFormat.parse(dateString);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            if (date != null) {
                patient.setDateOfBirth(date);
            }
        }

        patient.setAddress((String) jsonObject.get("address"));

        patient = patientService.update(patient);
        return new ResponseEntity(patient.toJSON().toJSONString(), HttpStatus.OK);
    }

    @RequestMapping(path = "patients/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deletePatient(@PathVariable("id") Long id) {
        Patient patient = patientService.findById(id);
        if (patient == null) {
            return new ResponseEntity("{\"message\":\"Patient not found.\"}", HttpStatus.NOT_FOUND);
        }
        boolean success = patientService.delete(patient);
        if (!success) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity(HttpStatus.OK);
        }
    }

}