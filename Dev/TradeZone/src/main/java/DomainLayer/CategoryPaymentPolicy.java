package DomainLayer;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;

import java.time.LocalTime;
enum CategoryPPType {
    MaxTimeAtDay, // after 23:00 alcohol not allowed
    RangeOfDays, // from 23/4/2023 to 15/9/2023 alcohol not allowed (כולל)
    MinimumAge // age < 16 alcohol not allowed
}

public class CategoryPaymentPolicy implements PaymentPolicy{

    CategoryPPType categoryPPType;
    Category category;

    //MaxTimeAtDay:
    LocalTime localTime;


    //RangeOfDays:
    LocalDate fromDate;
    LocalDate toDate;


    //MinimumAge:
    int minAge;


    public CategoryPaymentPolicy(Category category, int hour, int minute){
        this.category=category;
        this.categoryPPType = CategoryPPType.MaxTimeAtDay;
        this.localTime = LocalTime.of(hour,minute,0);
    }
    public CategoryPaymentPolicy(Category category, int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay){
        this.category=category;
        this.categoryPPType = CategoryPPType.RangeOfDays;
        this.fromDate = LocalDate.of(fromYear,fromMonth,fromDay);
        this.toDate = LocalDate.of(toYear,toMonth,toDay);
    }
    public CategoryPaymentPolicy(Category category, int age){
        this.category=category;
        this.categoryPPType = CategoryPPType.MinimumAge;
        this.minAge = age;
    }

    @Override
    public boolean validatePolicy(User user, ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent) throws Exception {
        switch (this.categoryPPType){
            case MinimumAge:
                return validateMinAgePolicy(user, bagContent);
            case MaxTimeAtDay:
                return validateMaxTimeAtDayPolicy(bagContent);
            case RangeOfDays:
                return validateRangeOfDaysPolicy(bagContent);
            default:
                return true;
        }
    }
    public boolean validateMaxTimeAtDayPolicy(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent) throws Exception {
        if(LocalTime.now().isBefore(this.localTime))
            return true;
        for (ConcurrentHashMap<Product, Integer> productTuple: bagContent.values()){
            Product currentProduct = productTuple.keys().nextElement();
            if(currentProduct.haveCategory(this.category))
                throw new Exception(this.category.getCategoryName()+" category are not allowed after: "+
                        this.localTime.toString()+" ,and you have "+currentProduct.getName()+ " in cart");
        }
        return true;
    }
    public boolean validateRangeOfDaysPolicy(ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent) throws Exception {
        if(LocalDate.now().isBefore(this.fromDate) || LocalDate.now().isAfter(this.toDate))
            return true;

        for (ConcurrentHashMap<Product, Integer> productTuple: bagContent.values()){
            Product currentProduct = productTuple.keys().nextElement();
            if(currentProduct.haveCategory(this.category))
                throw new Exception(this.category.getCategoryName()+" category are not allowed between: "+
                        this.fromDate.toString()+" and: "+ this.toDate.toString() +" ,and you have "+currentProduct.getName()+ " in cart");
        }
        return true;
    }
    public boolean validateMinAgePolicy(User user, ConcurrentHashMap<String, ConcurrentHashMap<Product, Integer>> bagContent) throws Exception {
        int userAge=100;
        if(userAge >= this.minAge)
            return true;

        for (ConcurrentHashMap<Product, Integer> productTuple: bagContent.values()){
            Product currentProduct = productTuple.keys().nextElement();
            if(currentProduct.haveCategory(this.category))
                throw new Exception("");
        }
        return true;
    }
}
