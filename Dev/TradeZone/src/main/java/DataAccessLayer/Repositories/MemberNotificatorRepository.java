package DataAccessLayer.Repositories;

import DomainLayer.MemberNotificator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberNotificatorRepository extends JpaRepository<MemberNotificator,Integer> {


    @Query(value = "SELECT id FROM member_notificator WHERE member_name = ?", nativeQuery = true)
    public List<Integer> findIdsByMemberName(String memberName);

}
