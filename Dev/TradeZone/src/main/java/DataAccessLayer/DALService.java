package DataAccessLayer;

import DataAccessLayer.Repositories.MemberRepository;
import DataAccessLayer.Repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DALService {

    @Autowired
    public static MemberRepository memberRepository;

    @Autowired
    public static StoreRepository storeRepository;

    public DALService(){
        super();
    }






}
/*









///////////




package DataAccessLayer.Repositories;

import DataAccessLayer.DTO.DTOStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<DTOStore, String> {

}


 */