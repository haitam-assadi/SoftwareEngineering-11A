package PresentationLayer.Configs;

import DataAccessLayer.DALService;
import DataAccessLayer.Repositories.*;
//import DataAccessLayer.Repositories.MemberRepository;
//import DataAccessLayer.Repositories.StockRepository;
//import DataAccessLayer.Repositories.StoreRepository;
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
    public CommandLineRunner demo(MemberRepository memberRepository,
                                  StoreRepository storeRepository,
                                  StockRepository stockRepository,
                                  ProductRepository productRepository,
                                  CategoryRepository categoryRepository,
                                  StoresOwnersRepository storesOwnersRepository,
                                  StoresManagersRepository storesManagersRepository,
                                  BagRepository bagRepository,
                                  CartRepository cartRepository) {
        return (args) -> {
            System.out.println("member config");
            DALService.memberRepository=memberRepository;
            System.out.println(memberRepository);
            DALService.storeRepository = storeRepository;
            System.out.println(storeRepository);
            DALService.stockRepository = stockRepository;
            System.out.println(stockRepository);
            DALService.productRepository = productRepository;
            System.out.println(productRepository);
            DALService.categoryRepository = categoryRepository;
            System.out.println(categoryRepository);
            DALService.storesOwnersRepository =storesOwnersRepository;
            System.out.println(storesOwnersRepository);
            DALService.storesManagersRepository = storesManagersRepository;
            System.out.println(storesManagersRepository);
            DALService.bagRepository = bagRepository;
            System.out.println(bagRepository);
            DALService.cartRepository = cartRepository;
            System.out.println(cartRepository);
        };
    }

}
