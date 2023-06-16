package PresentationLayer.model;

import CommunicationLayer.Server;
import DTO.BagDTO;
import DTO.DealDTO;
import ServiceLayer.ResponseT;
import org.springframework.web.context.annotation.SessionScope;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static PresentationLayer.controller.CartController.buildCart;

@SessionScope
public class GeneralModel {
    private Server server = Server.getInstance();
    private String name;
    private boolean hasRole;
    private boolean logged;
    private boolean systemManager;
    private String currentPage;
    private double cartTotalPrice;
    private double cartDiscountTotalPrice;
    private List<Bag> cart;
    private Alert alert;

    private static GeneralModel controller = null;
    public static GeneralModel getInstance(){
        if(controller == null){
            controller = new GeneralModel();
        }
        return controller;
    }

    public GeneralModel(){
        this.name = "";
        this.hasRole = false;
        this.logged = false;
        this.systemManager = false;
        currentPage = "/";
        this.cartTotalPrice = 0;
        cart = new ArrayList<>();
        alert = Alert.getInstance();
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public boolean getHasRole(){
        return this.hasRole;
    }

    public void setHasRole(boolean hasRole){
        this.hasRole = hasRole;
    }

    public boolean getLogged(){
        return this.logged;
    }

    public void setLogged(boolean logged){
        this.logged = logged;
    }

    public boolean isSystemManager() {
        ResponseT<Boolean> isSystemManager = server.isSystemManager(this.name);
        systemManager = (!isSystemManager.ErrorOccurred);
        return systemManager;
    }

    public void setSystemManager(boolean systemManager) {
        this.systemManager = systemManager;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public double getCartTotalPrice() {
        ResponseT<Double> response = server.getCartPriceBeforeDiscount(this.name);
        if(!response.ErrorOccurred){
            cartTotalPrice = round(response.getValue(), 2);
        }
        else cartTotalPrice = -1;
//        cartTotalPrice = getCartTotalPrice(cart);
        return cartTotalPrice;
    }

    public void setCartTotalPrice(double cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public double getCartDiscountTotalPrice() {
        ResponseT<Double> response = server.getCartPriceAfterDiscount(this.name);
        if(!response.ErrorOccurred){
            cartDiscountTotalPrice = round(response.getValue(), 2);
        }
        else cartDiscountTotalPrice = -1;
        return cartDiscountTotalPrice;
    }

    public void setCartDiscountTotalPrice(double cartDiscountTotalPrice) {
        this.cartDiscountTotalPrice = cartDiscountTotalPrice;
    }

    public List<Bag> getCart() {
        return cart;
    }

    public void setCart(List<Bag> cart) {
        this.cart = cart;
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    public void updateCart(){
        ResponseT<List<BagDTO>> response  = server.getCartContent(name);
        if(!response.ErrorOccurred){
            cart = buildCart(response.getValue());
        }
    }

    public boolean cartContainsProduct(String storeName, String productName){
        for(Bag bag : cart){
            if(bag.getStoreName().equals(storeName)){
                for(Product product : bag.getProducts()){
                    if(product.getName().equals(productName))
                        return true;
                }
            }
        }
        return false;
    }

    public int getProductAmountInCart(String storeName, String productName){
        int amount = 0;
        for(Bag bag : cart){
            if(bag.getStoreName().equals(storeName)){
                for(Product product : bag.getProducts()){
                    if(product.getName().equals(productName))
                        return product.getAmount();
                }
            }
        }
        return amount;
    }

    public boolean cartHasDisc(){
        return (getCartTotalPrice() != getCartDiscountTotalPrice());
    }

    private double getCartTotalPrice(List<Bag> bags){
        double total = 0;
        for(Bag b : bags){
            total += b.getTotalPrice();
        }
        total = round(total, 2);
        return total;
    }

    private List<Integer> getManagerPermissionsForStore(String storeName){
        ResponseT<List<Integer>> response = server.getManagerPermissionsForStore(this.name, storeName, this.name);
        if(response.ErrorOccurred){
            return null;
        }
        return response.getValue();
    }

    //return 1=storeFounder, 2=storeOwner, 3=storeManager, -1= noRule
    public boolean hasPermission(String storeName, int permId){
        ResponseT<Integer> response = server.getRuleForStore(storeName, this.name);
        if(response.ErrorOccurred)
            return false;
        int role = response.getValue();
        if(role == 1 || role == 2)
            return true;
        if(role == -1)
            return false;
        List<Integer> permissions = getManagerPermissionsForStore(storeName);
        if(permissions == null)
            return false;
        return permissions.contains(permId);
    }

    public int getRole(String storeName){
        ResponseT<Integer> response = server.getRuleForStore(storeName, this.name);
        if(response.ErrorOccurred)
            return -1;
        return response.getValue();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static List<Deal> buildDeals(List<DealDTO> dealDTOS){
        List<Deal> deals = new LinkedList<>();
        for(DealDTO dealDTO : dealDTOS){
            Map<String, List<Double>> products = new HashMap<>();
            for(String productName : dealDTO.products_amount.keySet()){
                List<Double> amount_price = new LinkedList<>();
                amount_price.add(0, dealDTO.products_amount.get(productName)*1.0);
                amount_price.add(1, dealDTO.products_prices.get(productName));
                products.put(productName, amount_price);
            }
            Deal deal = new Deal(dealDTO.storeName, dealDTO.date, dealDTO.username, products, dealDTO.totalPrice);
            deals.add(deal);
        }
        return deals;
    }

    public static String determineCurrentPage(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String currentPage = requestURI.substring(contextPath.length());

        // Remove any query parameters from the current page
        int queryParamIndex = currentPage.indexOf('?');
        if (queryParamIndex != -1) {
            currentPage = currentPage.substring(0, queryParamIndex);
        }

        return currentPage;
    }
}
