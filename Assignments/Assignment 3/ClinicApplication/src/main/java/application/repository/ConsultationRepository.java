package application.repository;

import application.entity.Consultation;
import application.entity.Patient;
import org.springframework.data.repository.CrudRepository;

public interface ConsultationRepository extends CrudRepository<Consultation, Long>{
    Iterable<Consultation> findAllByPatient(Patient patient);

}
