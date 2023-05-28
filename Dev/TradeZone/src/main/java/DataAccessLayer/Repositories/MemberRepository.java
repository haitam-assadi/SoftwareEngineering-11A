package DataAccessLayer.Repositories;

import DataAccessLayer.DTO.DTOMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<DTOMember, String> {

    public static final String FIND_Names = "SELECT \"user_name\" FROM member;";
    @Query(value = FIND_Names)
    public List<String> findNames();

}
