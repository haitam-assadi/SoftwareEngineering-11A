package DataAccessLayer.Repositories;

import DataAccessLayer.DTO.DTOStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<DTOStore, String> {

}