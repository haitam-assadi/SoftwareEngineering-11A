package DataAccessLayer.Repositories;

import DataAccessLayer.DTO.DTOMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<DTOMember, String> {
}
