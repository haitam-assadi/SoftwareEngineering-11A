package DataAccessLayer.Repositories;

import DataAccessLayer.DTO.DTOCategory;
import DataAccessLayer.DTO.DTOMember;
import DataAccessLayer.DTO.DTOProduct;
import DataAccessLayer.DTO.DTOStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<DTOStore, String> {

    List<DTOStore> findByDtoMember(DTOMember dtoMember);
}