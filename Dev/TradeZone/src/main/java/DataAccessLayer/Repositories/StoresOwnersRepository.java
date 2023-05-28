package DataAccessLayer.Repositories;

import DataAccessLayer.DTO.DTOStoresOwners;
import DataAccessLayer.DTO.compositeKeys.StoresOwnersId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoresOwnersRepository extends JpaRepository<DTOStoresOwners, StoresOwnersId> {
}
