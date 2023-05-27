package DataAccessLayer;

import DataAccessLayer.Repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DALService {

    @Autowired
    public static MemberRepository memberRepository;

}
