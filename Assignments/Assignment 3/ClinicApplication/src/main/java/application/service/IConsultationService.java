package application.service;

import application.entity.Consultation;
import application.entity.Patient;

import java.util.List;

public interface IConsultationService {
    List<Consultation> findAll();
    Consultation findById(Long id);
    List<Consultation> findAllByPatient(Patient patient);
    Consultation create(Consultation consultation);
    Consultation update(Consultation consultation);
    boolean delete(Consultation consultation);
}
