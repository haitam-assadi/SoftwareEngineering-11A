package PresentationLayer.controller;

import CommunicationLayer.Server;
import DTO.ProductDTO;
import PresentationLayer.model.*;
import ServiceLayer.ResponseT;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {
	private Server server = Server.getInstance();
	private GeneralController controller = GeneralController.getInstance();
	@GetMapping("/")
	public String index(Model model) {
		List<ProductDTO> products = new ArrayList<>();
		if(controller.getName().equals("")){
			ResponseT<Boolean> response = server.initializeMarket();
			if(response.ErrorOccurred){
				model.addAttribute("message", response.errorMessage);
				return "error";
			}
			String name = server.enterMarket().value;
			server.addToCart(name, "user1_first_Store", "user1_first_Store_product1", 5);
			server.addToCart(name, "user1_first_Store", "user1_first_Store_product2", 3);
			server.addToCart(name, "user1_Second_Store", "user1_Second_Store_product1", 4);
			controller.setName(name);
		}
		model.addAttribute("logged", controller.getLogged());
		model.addAttribute("name", controller.getName());
		model.addAttribute("hasRole", controller.getHasRole());
		model.addAttribute("products", products);
		model.addAttribute("message", "");
		return "index";
	}
	
	@PostMapping("/search")
	public String userSearch(@ModelAttribute Search search, Model model) {
		List<ProductDTO> products = new ArrayList<>();
		if(search.getSearchBy().equals("product")){
			ResponseT<List<ProductDTO>> response = server.getProductInfoFromMarketByName(controller.getName(), search.getSearchProducts());
			if(response.ErrorOccurred){
				return "role_page";
			}
			products = response.value;
		}
		else{
			ResponseT<List<ProductDTO>> response = server.getProductInfoFromMarketByCategory(controller.getName(), search.getSearchProducts());
			if(response.ErrorOccurred){
				return "role_page";
			}
			products = response.value;
		}
		System.out.println(products.toString());
		model.addAttribute("products", products);
		model.addAttribute("name", controller.getName());
		model.addAttribute("logged", controller.getLogged());
		model.addAttribute("hasRole", controller.getHasRole());
		model.addAttribute("message", "No Products Available");
		return "index";
	}

	@GetMapping("/logout")
	public String logout(Model model) {
		List<ProductDTO> products = new ArrayList<>();
		ResponseT<String> response = server.memberLogOut(controller.getName());
		if(response.ErrorOccurred){
			model.addAttribute("message", response.errorMessage);
			return "error";
		}
		controller.setName(response.value);
		controller.setLogged(false);
		model.addAttribute("logged", controller.getLogged());
		model.addAttribute("hasRole", false);
		model.addAttribute("name", controller.getName());
		model.addAttribute("products", products);
		model.addAttribute("message", "");
		return "index";
	}

	@PostMapping("/index")
	public String returntoIndex(@ModelAttribute Search search, Model model) {
		List<ProductDTO> products = new ArrayList<>();
		model.addAttribute("logged", controller.getLogged());
		model.addAttribute("hasRole", controller.getHasRole());
		model.addAttribute("name", controller.getName());
		model.addAttribute("products", products);
		model.addAttribute("message", "");
		return "index";
	}

	@GetMapping("/error")
	public String error(@ModelAttribute User user, Model model) {
		model.addAttribute("message", "Page not found");
		return "error";
	}

	@PostMapping("/add_to_cart")
	public String addToCart(@ModelAttribute AddedAmount addedAmount, Model model) {
		int amount = addedAmount.getAmount();
		String product = addedAmount.getProductName();
		List<ProductDTO> products = new ArrayList<>();
		model.addAttribute("logged", controller.getLogged());
		model.addAttribute("hasRole", controller.getHasRole());
		model.addAttribute("name", controller.getName());
		model.addAttribute("products", products);
		model.addAttribute("message", "");
		return "index";
	}
}
