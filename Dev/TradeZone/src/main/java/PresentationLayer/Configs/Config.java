package PresentationLayer.Configs;//package PresentationLayer.Configs;

import DataAccessLayer.DALService;
import DataAccessLayer.Repositories.*;
import DataAccessLayer.Repositories.BagConstraints.*;
import DataAccessLayer.Repositories.DiscountPolicies.*;
import DomainLayer.BagConstraints.AllContentBagConstraint;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("DomainLayer")
@EnableJpaRepositories("DataAccessLayer.Repositories")
public class Config {

    @Bean
    public CommandLineRunner demo(MemberRepository memberRepository,
                                  StoreRepository storeRepository,
                                  StockRepository stockRepository,
                                  ProductRepository productRepository,
                                  CategoryRepository categoryRepository,
                                  StoreOwnerRepository storesOwnersRepository,
                                  StoreManagerRepository storesManagersRepository,
                                  BagRepository bagRepository,
                                  CartRepository cartRepository,
                                  StoreFounderRepository storeFounderRepository,
                                  SystemManagerRepository systemManagerRepository,
                                  BagConstraintRepository bagConstraintRepository,
                                  AllContentBagConstraintRepository allContentBagConstraintRepository,
                                  BagConstraintAndRepository bagConstraintAndRepository,
                                  BagConstraintOnlyIfRepository bagConstraintOnlyIfRepository,
                                  BagConstraintOrRepository bagConstraintOrRepository,
                                  CategoryBagConstraintRepository categoryBagConstraintRepository,
                                  PositiveBagConstraintRepository positiveBagConstraintRepository,
                                  ProductBagConstraintRepository productBagConstraintRepository,
                                  DiscountPolicyRepository discountPolicyRepository,
                                  AdditionDiscountPolicyRepository additionDiscountPolicyRepository,
                                  AllStoreDiscountPolicyRepository allStoreDiscountPolicyRepository,
                                  CategoryDiscountPolicyRepository categoryDiscountPolicyRepository,
                                  MaxValDiscountPolicyRepository maxValDiscountPolicyRepository,
                                  ProductDiscountPolicyRepository productDiscountPolicyRepository,
                                  DealRepository dealRepository,
                                  OwnerContractRepository ownerContractRepository) {
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
            DALService.storeFounderRepository = storeFounderRepository;
            System.out.println(storeFounderRepository);
            DALService.systemManagerRepository = systemManagerRepository;
            System.out.println(systemManagerRepository);
            DALService.bagConstraintRepository = bagConstraintRepository;
            System.out.println(bagConstraintRepository);
            DALService.allContentBagConstraintRepository = allContentBagConstraintRepository;
            System.out.println(allContentBagConstraintRepository);
            DALService.bagConstraintAndRepository = bagConstraintAndRepository;
            System.out.println(bagConstraintAndRepository);
            DALService.bagConstraintOnlyIfRepository = bagConstraintOnlyIfRepository;
            System.out.println(bagConstraintOnlyIfRepository);
            DALService.bagConstraintOrRepository = bagConstraintOrRepository;
            System.out.println(bagConstraintOrRepository);
            DALService.categoryBagConstraintRepository = categoryBagConstraintRepository;
            System.out.println(categoryBagConstraintRepository);
            DALService.positiveBagConstraintRepository = positiveBagConstraintRepository;
            System.out.println(positiveBagConstraintRepository);
            DALService.productBagConstraintRepository = productBagConstraintRepository;
            System.out.println(productBagConstraintRepository);
            DALService.discountPolicyRepository = discountPolicyRepository;
            System.out.println(discountPolicyRepository);
            DALService.additionDiscountPolicyRepository = additionDiscountPolicyRepository;
            System.out.println(additionDiscountPolicyRepository);
            DALService.allStoreDiscountPolicyRepository = allStoreDiscountPolicyRepository;
            System.out.println(allStoreDiscountPolicyRepository);
            DALService.categoryDiscountPolicyRepository = categoryDiscountPolicyRepository;
            System.out.println(categoryDiscountPolicyRepository);
            DALService.maxValDiscountPolicyRepository = maxValDiscountPolicyRepository;
            System.out.println(maxValDiscountPolicyRepository);
            DALService.productDiscountPolicyRepository = productDiscountPolicyRepository;
            System.out.println(productDiscountPolicyRepository);
            DALService.dealRepository = dealRepository;
            System.out.println(dealRepository);
            DALService.ownerContractRepository = ownerContractRepository;
            System.out.println(ownerContractRepository);
        };
    }

}
