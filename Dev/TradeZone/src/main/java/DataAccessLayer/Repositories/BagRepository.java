package DataAccessLayer.Repositories;

import DataAccessLayer.DTO.DTOBag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BagRepository extends JpaRepository<DTOBag, Integer> {
}
