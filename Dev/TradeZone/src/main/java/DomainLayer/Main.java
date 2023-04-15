package DomainLayer;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        Member member1 = new Member("aa","pass");
        Member member2 = new Member("bb","pass");
        StoreOwner role1 = new StoreOwner(member1);
        role1.member=member1;
        StoreOwner role2 = new StoreOwner(member2);
        role2.member=member2;
        List<String> haitam = new LinkedList<>();
        haitam.add("haitam1");
        haitam.add("haitam2");

        System.out.println(haitam.get(0));
        ConcurrentHashMap<String, StoreOwner> storeOwners= new ConcurrentHashMap<>();
        storeOwners.put("sadasd",role1);
        storeOwners.put("sadasd",role2);
        storeOwners.put("sadasud",role2);
        List<String> ownersNames= storeOwners.values().stream().map(Role::getUserName).toList();
        System.out.println(ownersNames);
    }
}