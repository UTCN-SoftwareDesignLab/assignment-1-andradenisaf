package application.service;

import application.entity.Consultation;
import application.entity.Patient;
import application.repository.ConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ConsultationService implements IConsultationService{

    private ConsultationRepository consultationRepository;

    @Autowired
    public ConsultationService(ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
    }

    @Override
    public List<Consultation> findAll() {
        Iterable<Consultation> iterable = consultationRepository.findAll();
        ArrayList<Consultation> consultations = new ArrayList<>();
        iterable.forEach(consultations::add);
        return consultations;
    }

    @Override
    public Consultation findById(Long id) {
        Optional<Consultation> optionalConsultation = consultationRepository.findById(id);
        Consultation consultation;
        try {
            consultation = optionalConsultation.get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
        return consultation;
    }

    @Override
    public List<Consultation> findAllByPatient(Patient patient) {
        Iterable<Consultation> iterable = consultationRepository.findAllByPatient(patient);
        ArrayList<Consultation> consultations = new ArrayList<>();
        iterable.forEach(consultations::add);
        return consultations;
    }

    @Override
    public Consultation create(Consultation consultation) {
        return consultationRepository.save(consultation);
    }

    @Override
    public Consultation update(Consultation consultation) {
        if (!consultationRepository.existsById(consultation.getId())) {
            return null;
        }
        return consultationRepository.save(consultation);
    }

    @Override
    public boolean delete(Consultation consultation) {
        if (!consultationRepository.existsById(consultation.getId())) {
            return false;
        }
        consultationRepository.delete(consultation);
        return true;
    }
}
