package application.repository;

import application.entity.Right;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RightRepository extends CrudRepository<Right, Long> {

    List<Right> findAllByDescription(String description);
}
