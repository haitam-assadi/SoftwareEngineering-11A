package DataAccessLayer.Repositories;

import DomainLayer.Member;
import DomainLayer.SystemManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemManagerRepository extends JpaRepository<SystemManager,Integer> {

    public SystemManager findByMember(Member member);
}
