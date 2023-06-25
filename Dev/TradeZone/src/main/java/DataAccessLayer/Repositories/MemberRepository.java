package DataAccessLayer.Repositories;

import DomainLayer.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface MemberRepository extends JpaRepository<Member,String> {
    @Query(value = "SELECT user_name FROM member",nativeQuery = true)
    public Set<String> getAllMemberNames();

    @Query(value = "SELECT user_name FROM member WHERE is_online = true",nativeQuery = true)
    public Set<String> getAllOnlineMembersNames();

    @Query(value = "SELECT user_deals_id FROM user_deals WHERE member_user_name = ?",nativeQuery = true)
    public List<Long> findMemberDealsIdsByUserName(String userName);
}
