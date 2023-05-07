package DomainLayer.BagConstraints;

import DomainLayer.Product;
import DomainLayer.User;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;

import java.time.LocalTime;


public class ProductBagConstraint implements BagConstraint {


    BagConstraintType productPPType;
    Product product;

    //MaxTimeAtDay:
    LocalTime localTime;


    //RangeOfDays:
    LocalDate fromDate;
    LocalDate toDate;


    //MinimumAge:
    int minAge;

    public ProductBagConstraint(Product product, int hour, int minute){
        this.product=product;
        this.productPPType = BagConstraintType.MaxTimeAtDay;
        this.localTime = LocalTime.of(hour,minute,0);
    }
    public ProductBagConstraint(Product product, int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay){
        this.product=product;
        this.productPPType = BagConstraintType.RangeOfDays;
        this.fromDate = LocalDate.of(fromYear,fromMonth,fromDay);
        this.toDate = LocalDate.of(toYear,toMonth,toDay);
    }
    public ProductBagConstraint(Product product, int age){
        this.product=product;
        this.productPPType = BagConstraintType.MinimumAge;
        this.minAge = age;
    }
    @Override
    public boolean checkConstraint(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent){
        switch (this.productPPType){
            case MaxTimeAtDay:
                return checkMaxTimeAtDayConstraint(bagContent);
            case RangeOfDays:
                return checkRangeOfDaysConstraint(bagContent);
            default:
                return true;
        }
    }
    public boolean checkMaxTimeAtDayConstraint(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent){
        if(LocalTime.now().isBefore(this.localTime))
            return true;

        if (bagContent.containsKey(product.getName()))
            return false;

        return true;
    }
    public boolean checkRangeOfDaysConstraint(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent) {
        if(LocalDate.now().isBefore(this.fromDate) || LocalDate.now().isAfter(this.toDate))
            return true;

        if (bagContent.containsKey(product.getName()))
            return false;

        return true;
    }
    public boolean checkMinAgeConstraint(User user, ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent){
        int userAge=100;
        if(userAge >= this.minAge)
            return true;

        if (bagContent.containsKey(product.getName()))
            return false;

        return true;
    }
}
