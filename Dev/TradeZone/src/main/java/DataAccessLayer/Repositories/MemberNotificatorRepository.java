package DataAccessLayer.Repositories;

import DomainLayer.MemberNotificator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberNotificatorRepository extends JpaRepository<MemberNotificator,Integer> {
}
