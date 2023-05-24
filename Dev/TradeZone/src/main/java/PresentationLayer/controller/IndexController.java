package PresentationLayer.controller;

import CommunicationLayer.Server;
import DTO.ProductDTO;
import ServiceLayer.ResponseT;
import PresentationLayer.model.Alert;
import PresentationLayer.model.ItemDetails;
import PresentationLayer.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import PresentationLayer.model.Search;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {
	private Server server = Server.getInstance();
	private GeneralController controller = GeneralController.getInstance();
	List<ProductDTO> products; // = new ArrayList<>()
	Alert alert = Alert.getInstance();

	@GetMapping("/")
	public String index(Model model) {
		if(controller.getName().equals("")){
			ResponseT<Boolean> response = server.initializeMarket();
			if(response.ErrorOccurred){
				alert.setFail(true);
				alert.setMessage(response.errorMessage);
				model.addAttribute("controller", controller);
				model.addAttribute("alert", alert.copy());
				model.addAttribute("products", products);
				return "index"; // "/" ???
			}
			String name = server.enterMarket().value;
			controller.setName(name);
		}
		model.addAttribute("controller", controller);
		model.addAttribute("alert", alert.copy());
		model.addAttribute("products", products);
		alert.reset();
//		products = null; // TODO: ???
		return "index";
	}

	@PostMapping("/search")
	public String userSearch(@ModelAttribute Search search, Model model) {
		ResponseT<List<ProductDTO>> response;
		if(!search.getSearchStoreName().equals("")){
			ResponseT<ProductDTO> response1 = server.getProductInfoFromStore(controller.getName(), search.getSearchStoreName(), search.getSearchProducts());
			if(response1.ErrorOccurred){
				products = null;
				alert.setFail(true);
				alert.setMessage(response1.errorMessage);
				return "redirect:/";
			}
			products = new ArrayList<>();
			products.add(response1.value);
		}
		else{
			if(search.getSearchBy().equals("product")){
				response = server.getProductInfoFromMarketByName(controller.getName(), search.getSearchProducts());
				if(response.ErrorOccurred){
					products = null;
					alert.setFail(true);
					alert.setMessage(response.errorMessage);
					return "redirect:/";
				}
			} else if(search.getSearchBy().equals("category")){
				response = server.getProductInfoFromMarketByCategory(controller.getName(), search.getSearchProducts());
				if(response.ErrorOccurred){
					products = null;
					alert.setFail(true);
					alert.setMessage(response.errorMessage);
					return "redirect:/";
				}
			} else{
				response = server.getProductInfoFromMarketByKeyword(controller.getName(), search.getSearchProducts());
				if(response.ErrorOccurred){
					products = null;
					alert.setFail(true);
					alert.setMessage(response.errorMessage);
					return "redirect:/";
				}
			}
			products = response.getValue();
		}
		return "redirect:/";
	}

	@GetMapping("/logout")
	public String logout(Model model) {
		ResponseT<String> response = server.memberLogOut(controller.getName());
		if(response.ErrorOccurred){
			alert.setFail(true);
			alert.setMessage(response.errorMessage);
			return "redirect:/"; // TODO: redirect + request
		}
		controller.setName(response.value);
		controller.setLogged(false);
		controller.setSystemManager(false);
		controller.setHasRole(false);
//		products = null; // TODO: ???
		return "redirect:/"; // TODO: redirect + request
	}

	@GetMapping("/error")
	public String error(@ModelAttribute User user, Model model) {
		model.addAttribute("message", "Page not found");
		return "error";
	}

//	@PostMapping("/add_to_cart")
//	public String addToCart(@ModelAttribute ItemDetails itemDetails, Model model, HttpServletRequest request) {
//		String referer = request.getHeader("referer");
//		int amount = itemDetails.getAmount();
//		String productName = itemDetails.getName();
//		String storeName = itemDetails.getStoreName();
//		ResponseT<Boolean> response = server.addToCart(controller.getName(), storeName, productName, amount);
//		if(response.ErrorOccurred){
//			alert.setFail(true);
//			alert.setMessage(response.errorMessage);
//			return "redirect:/"; // TODO: redirect:/ + request.getHeader(...)
//		}
//		alert.setSuccess(true);
//		alert.setMessage("Added successfully");
//		controller.updateCart();
//		return "redirect:" + referer;
////		return "redirect:" + request.getHeader("Referer");
////		return "redirect:/";
//	}

//	@PostMapping("/indexUpdateProductAmount")
//	public String updateProductAmount(@ModelAttribute ItemDetails itemDetails, Model model) {
//		int amount = itemDetails.getAmount();
//		String productName = itemDetails.getName();
//		String storeName = itemDetails.getStoreName();
//		ResponseT<Boolean> response = server.changeProductAmountInCart(controller.getName(), storeName, productName, amount);
//		if(response.ErrorOccurred){
//			alert.setFail(true);
//			alert.setMessage(response.errorMessage);
//			return "redirect:/";
//		}
//		if(response.getValue()){ // updated
//			alert.setSuccess(true);
//			alert.setMessage("product amount has updated successfully");
//			controller.updateCart();
//		}
//		else{ // not updated
//			alert.setFail(true);
//			alert.setMessage("Failed to update the product amount");
//		}
//		return "redirect:/";
//	}
}
