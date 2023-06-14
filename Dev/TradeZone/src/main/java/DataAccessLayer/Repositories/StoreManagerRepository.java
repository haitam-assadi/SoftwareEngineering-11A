package DataAccessLayer.Repositories;

import DataAccessLayer.CompositeKeys.RolesId;
import DomainLayer.StoreManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreManagerRepository extends JpaRepository<StoreManager, RolesId> {
}
