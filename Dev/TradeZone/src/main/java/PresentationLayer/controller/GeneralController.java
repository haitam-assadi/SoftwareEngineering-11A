package PresentationLayer.controller;

import CommunicationLayer.Server;
import DTO.BagDTO;
import DTO.DealDTO;
import PresentationLayer.model.Alert;
import PresentationLayer.model.Bag;
import PresentationLayer.model.Deal;
import PresentationLayer.model.Product;
import ServiceLayer.ResponseT;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static PresentationLayer.controller.CartController.buildCart;

public class GeneralController {
    private Server server = Server.getInstance();
    private String name;
    private boolean hasRole;
    private boolean logged;
    private boolean systemManager;
    private String currentPage;
    private double cartTotalPrice;
    private List<Bag> cart;

    private static GeneralController controller = null;
    public static GeneralController getInstance(){
        if(controller == null){
            controller = new GeneralController();
        }
        return controller;
    }

    private GeneralController(){
        this.name = "";
        this.hasRole = false;
        this.logged = false;
        this.systemManager = false;
        currentPage = "/";
        this.cartTotalPrice = 0;
        cart = new ArrayList<>();
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
        cartTotalPrice = getCartTotalPrice(cart);
        return cartTotalPrice;
    }

    public void setCartTotalPrice(double cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public List<Bag> getCart() {
        return cart;
    }

    public void setCart(List<Bag> cart) {
        this.cart = cart;
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

    private double getCartTotalPrice(List<Bag> bags){
        double total = 0;
        for(Bag b : bags){
            total += b.getTotalPrice();
        }
        total = round(total, 2);
        return total;
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
