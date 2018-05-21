package application.service;

import application.entity.Patient;

import java.util.List;

public interface IPatientService {
    List<Patient> findAll();
    Patient findById(Long id);
    List<Patient> findAllByName(String name);
    Patient findByIdentityCardNo(String identityCardNo);
    Patient findByCNP(String cnp);
    Patient create(Patient patient);
    Patient update(Patient patient);
    boolean delete(Patient patient);
}
