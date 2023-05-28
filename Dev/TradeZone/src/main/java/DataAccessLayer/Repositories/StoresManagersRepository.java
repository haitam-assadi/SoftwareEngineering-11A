package DataAccessLayer.Repositories;

import DataAccessLayer.DTO.DTOStoresManagers;
import DataAccessLayer.DTO.compositeKeys.StoresManagersId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoresManagersRepository extends JpaRepository<DTOStoresManagers, StoresManagersId> {
}
