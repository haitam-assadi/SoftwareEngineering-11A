package PresentationLayer.Configs;

import DataAccessLayer.DALService;
import DataAccessLayer.Repositories.MemberRepository;
import DataAccessLayer.Repositories.StoreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EntityScan("DataAccessLayer.DTO")
@EnableJpaRepositories("DataAccessLayer.Repositories")
public class Config {

    @Bean
    public CommandLineRunner demo(MemberRepository memberRepository, StoreRepository storeRepository) {
        return (args) -> {
            System.out.println("member config");
            DALService.memberRepository=memberRepository;
            System.out.println(memberRepository);
            DALService.storeRepository = storeRepository;
            System.out.println(storeRepository);
        };
    }

}
