package DomainLayer;

import DTO.ProductDTO;
import DTO.StoreDTO;
import ServiceLayer.SystemService;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) throws Exception {

        LocalDate date = LocalDate.of(2022,10,23);



        // display formatted date
        System.out.printf(date.toString());





    }
}