package DomainLayer.BagConstraints;

import DomainLayer.Category;
import DomainLayer.Product;
import DomainLayer.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalTime;

@Entity
@Table
@PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id"),
        @PrimaryKeyJoinColumn(name = "storeName", referencedColumnName = "storeName")
})
public class CategoryBagConstraint extends BagConstraint implements Serializable {

    @Enumerated(EnumType.STRING)
    @Column(name = "categoryPPType")
    BagConstraintType categoryPPType;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "categoryName", referencedColumnName = "categoryName"),
            @JoinColumn(name = "categoryStoreName", referencedColumnName = "storeName")
    })
    Category category;

    //MaxTimeAtDay:
    LocalTime localTime;


    //RangeOfDays:
    LocalDate fromDate;
    LocalDate toDate;


    //MinimumAge:
    int minAge;


    public CategoryBagConstraint(Category category, int hour, int minute){
        this.category=category;
        this.categoryPPType = BagConstraintType.MaxTimeAtDay;
        this.localTime = LocalTime.of(hour,minute,0);
    }
    public CategoryBagConstraint(Category category, int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay){
        this.category=category;
        this.categoryPPType = BagConstraintType.RangeOfDays;
        this.fromDate = LocalDate.of(fromYear,fromMonth,fromDay);
        this.toDate = LocalDate.of(toYear,toMonth,toDay);
    }
    public CategoryBagConstraint(Category category, int age){
        this.category=category;
        this.categoryPPType = BagConstraintType.MinimumAge;
        this.minAge = age;
    }

    @Override
    public boolean checkConstraint(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent){
        switch (this.categoryPPType){
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
        for (ConcurrentHashMap<Product, Integer> productTuple: bagContent.values()){
            Product currentProduct = productTuple.keys().nextElement();
            if(currentProduct.haveCategory(this.category))
                return false;
        }
        return true;
    }
    public boolean checkRangeOfDaysConstraint(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent){
        if(LocalDate.now().isBefore(this.fromDate) || LocalDate.now().isAfter(this.toDate))
            return true;

        for (ConcurrentHashMap<Product, Integer> productTuple: bagContent.values()){
            Product currentProduct = productTuple.keys().nextElement();
            if(currentProduct.haveCategory(this.category))
                return false;
        }
        return true;
    }
    public boolean checkMinAgeConstraint(User user, ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent){
        int userAge=100;
        if(userAge >= this.minAge)
            return true;

        for (ConcurrentHashMap<Product, Integer> productTuple: bagContent.values()){
            Product currentProduct = productTuple.keys().nextElement();
            if(currentProduct.haveCategory(this.category))
                return false;
        }
        return true;
    }

    public String toString(){
        String st = "";
        if(categoryPPType == BagConstraintType.MaxTimeAtDay){
            st = "category "+ category.getName()+" is not allowed after " +this.localTime.toString()+" .";
        }else{
            st = "category "+ category.getName()+" is not allowed between " +this.fromDate.toString()+" and "+this.toDate.toString()+" .";
        }
        return st;
    }
}
