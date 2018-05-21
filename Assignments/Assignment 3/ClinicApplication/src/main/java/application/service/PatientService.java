package application.service;

import application.entity.Patient;
import application.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PatientService implements IPatientService{

    private PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Patient> findAll() {
        Iterable<Patient> iterable = patientRepository.findAll();
        ArrayList<Patient> patients = new ArrayList<>();
        iterable.forEach(patients::add);
        return patients;
    }

    @Override
    public Patient findById(Long id) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        Patient patient;
        try {
            patient = optionalPatient.get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
        return patient;
    }

    @Override
    public List<Patient> findAllByName(String name) {
        Iterable<Patient> iterable = patientRepository.findAllByName(name);
        ArrayList<Patient> patients = new ArrayList<>();
        iterable.forEach(patients::add);
        return patients;
    }

    @Override
    public Patient findByIdentityCardNo(String identityCardNo) {
        Optional<Patient> optionalPatient = patientRepository.findByIdentityCardNo(identityCardNo);
        Patient patient;
        try {
            patient = optionalPatient.get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
        return patient;
    }

    @Override
    public Patient findByCNP(String cnp) {
        Optional<Patient> optionalPatient = patientRepository.findByCnp(cnp);
        Patient patient;
        try {
            patient = optionalPatient.get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
        return patient;
    }

    @Override
    public Patient create(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient update(Patient patient) {
        if (!patientRepository.existsById(patient.getId())) {
            return null;
        }
        return patientRepository.save(patient);
    }

    @Override
    public boolean delete(Patient patient) {
        if (!patientRepository.existsById(patient.getId())) {
            return false;
        }
        patientRepository.delete(patient);
        return true;
    }

}
