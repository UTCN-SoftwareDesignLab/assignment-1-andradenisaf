package application.repository;

import application.entity.Patient;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PatientRepository extends CrudRepository<Patient, Long> {
    Iterable<Patient> findAllByName(String name);
    Optional<Patient> findByIdentityCardNo(String idCardNo);
    Optional<Patient> findByCnp(String cnp);

}
