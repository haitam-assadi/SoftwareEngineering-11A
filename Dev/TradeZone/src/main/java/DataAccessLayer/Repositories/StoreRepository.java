package DataAccessLayer.Repositories;

import DomainLayer.Deal;
import DomainLayer.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface StoreRepository extends JpaRepository<Store,String> {
    @Query(value = "SELECT store_name FROM store",nativeQuery = true)
    public Set<String> getAllStoresNames();

    @Query(value = "SELECT is_active FROM store WHERE store_name = ?",nativeQuery = true)
    public boolean findIsActiveById(String storeName);

    @Query(value = "SELECT store_payment_policies_key FROM store_payment_policies WHERE store_store_name = ?",nativeQuery = true)
    public List<Integer> findPaymentPolicyIdByStoreName(String storeName);

    @Query(value = "SELECT store_discount_policies_key FROM store_discount_policies WHERE store_store_name = ?",nativeQuery = true)
    public List<Integer> findDiscountPolicyIdByStoreName(String storeName);

    @Query(value = "SELECT already_done_contracts_contract_id FROM already_done_contracts WHERE store_store_name = ?",nativeQuery = true)
    public List<Integer> findAlreadyContractsIdByStoreName(String storeName);

    @Query(value = "SELECT new_owners_contracts_contract_id FROM new_owners_contracts WHERE store_store_name = ?",nativeQuery = true)
    public List<Integer> findNewOwnersContractsIdByStoreName(String storeName);

    @Query(value = "SELECT store_deals_id FROM store_deals WHERE store_store_name = ?",nativeQuery = true)
    public List<Long> findStoreDealsIdsByStoreName(String storeName);
}
