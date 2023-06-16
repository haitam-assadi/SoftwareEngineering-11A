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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.System;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {
	private static boolean isMarketInit = false;
	private Server server = Server.getInstance();
	static String guestName;
	GeneralModel controller;
	List<ProductDTO> products; // = new ArrayList<>()
	Alert alert = Alert.getInstance();

	@GetMapping("/")
	public String index(HttpServletRequest request, Model model) throws Exception {
		String referer = request.getHeader("referer");
		controller = new GeneralModel();
		products = null;
		if(!isMarketInit) {
			ResponseT<String> response = server.initializeMarket();
			if (response.ErrorOccurred) {
				alert.setFail(true);
				alert.setMessage(response.errorMessage);
				model.addAttribute("controller", controller);
				model.addAttribute("alert", alert.copy());
				model.addAttribute("products", products);
				return "error"; // ??? or index
			}
			isMarketInit = true;
		}
//		GeneralModel controller = new GeneralModel();
		if(request.getSession().getAttribute("controller") != null){
			controller = (GeneralModel) request.getSession().getAttribute("controller");
			if(referer != null && referer.contentEquals(request.getRequestURL()))
				products = (List<ProductDTO>) request.getSession().getAttribute("products");
		}

		if(controller.getName().equals("")){ // new client
			String name = server.enterMarket().value;
			controller.setName(name);
			request.getSession().setAttribute("controller",controller);
			request.getSession().setAttribute("products",null);
		}
		if(referer != null && referer.contentEquals("http://localhost:8080/login")){
			controller.checkForAppendingMessages();
		}
		guestName = controller.getName();
		model.addAttribute("controller", controller);
		model.addAttribute("alert", alert.copy());
		model.addAttribute("products", products);
		alert.reset();
//		controller.checkForLiveMessages();
//		List<String> messages = controller.getMessages();
//		model.addAttribute("hasMessages", controller.hasMessages());
//		model.addAttribute("messages", messages);
//		products = null; // TODO: ???
		return "index";
	}

	@PostMapping("/search")
	public String userSearch(HttpServletRequest request, @ModelAttribute Search search) {
//		GeneralModel controller = new GeneralModel();
//		controller = new GeneralModel();
		if(request.getSession().getAttribute("controller") != null){
			controller = (GeneralModel) request.getSession().getAttribute("controller");
		}
		ResponseT<List<ProductDTO>> response;
		if(!search.getSearchStoreName().equals("")){
			ResponseT<ProductDTO> response1 = server.getProductInfoFromStore(controller.getName(), search.getSearchStoreName(), search.getSearchProducts());
			if(response1.ErrorOccurred){
				products = null;
				alert.setFail(true);
				alert.setMessage(response1.errorMessage);
//				return "redirect:/";
			}
			else{
				products = new ArrayList<>();
				products.add(response1.value);
			}
		}
		else{
			if(search.getSearchBy().equals("product")){
				response = server.getProductInfoFromMarketByName(controller.getName(), search.getSearchProducts());
				if(response.ErrorOccurred){
					products = null;
					alert.setFail(true);
					alert.setMessage(response.errorMessage);
//					return "redirect:/";
				}
			} else if(search.getSearchBy().equals("category")){
				response = server.getProductInfoFromMarketByCategory(controller.getName(), search.getSearchProducts());
				if(response.ErrorOccurred){
					products = null;
					alert.setFail(true);
					alert.setMessage(response.errorMessage);
//					return "redirect:/";
				}
			} else{
				response = server.getProductInfoFromMarketByKeyword(controller.getName(), search.getSearchProducts());
				if(response.ErrorOccurred){
					products = null;
					alert.setFail(true);
					alert.setMessage(response.errorMessage);
//					return "redirect:/";
				}
			}
			if(!response.ErrorOccurred)
				products = response.getValue();
		}
		request.getSession().setAttribute("products", products);
		return "redirect:/";
	}

	@PostMapping("/filter")
	public String filter(HttpServletRequest request, @ModelAttribute Filter filter){
		products = null; // new List???
		if(request.getSession().getAttribute("controller") != null){
			controller = (GeneralModel) request.getSession().getAttribute("controller");
			if(request.getSession().getAttribute("products") != null)
				products = (List<ProductDTO>) request.getSession().getAttribute("products");
		}
		filter.setProducts(products);
		ResponseT<List<ProductDTO>> response;
		if(filter.getFilterPrice().equals("filterPrice")){
			response = server.filterByPrice(controller.getName(), filter.getProducts(), filter.getMin(), filter.getMax());
			if(response.ErrorOccurred){
				alert.setFail(true);
				alert.setMessage(response.errorMessage);
				return "redirect:/";
			}
			filter.setProducts(response.getValue());
			products = response.getValue();
		}
		if(filter.getFilterCategory().equals("filterCategory")){
			response = server.filterByCategory(controller.getName(), filter.getProducts(), filter.getCategoryName());
			if(response.ErrorOccurred){
				alert.setFail(true);
				alert.setMessage(response.errorMessage);
				return "redirect:/";
			}
			filter.setProducts(response.getValue());
			products = response.getValue();
		}
		request.getSession().setAttribute("products", products);
		return "redirect:/";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
//		GeneralModel controller = new GeneralModel();
//		controller = new GeneralModel();
		if(request.getSession().getAttribute("controller") != null){
			controller = (GeneralModel) request.getSession().getAttribute("controller");
		}
		ResponseT<String> response = server.memberLogOut(controller.getName());
		if(response.ErrorOccurred){
			alert.setFail(true);
			alert.setMessage(response.errorMessage);
			return "redirect:/";
		}
		controller = new GeneralModel(); // TODO: ???
		controller.setName(response.value);
		controller.setLogged(false);
		controller.setSystemManager(false);
		controller.setHasRole(false);
		products = null; // TODO: ???
		request.getSession().setAttribute("controller", controller);
		request.getSession().setAttribute("products", products);
		return "redirect:/";
	}

	@GetMapping("/memberDeals")
	public String getMemberDeals(){
		// TODO:
//		ResponseT<List<DealDTO>> responseT = server.getMemberDeals("", controller.getName());
		return "memberDeals";
	}


	@GetMapping("/error")
	public String error(@ModelAttribute User user, Model model) {
		model.addAttribute("message", "Page not found");
		return "error";
	}

	@PostMapping("/clear")
	public String clearNotifications(HttpServletRequest request, @RequestParam String name){
		String referer = request.getHeader("referer");
		if(request.getSession().getAttribute("controller") != null){
			controller = (GeneralModel) request.getSession().getAttribute("controller");
		}
		controller.clearMessages();
		server.clearMessages(controller.getName());
		request.getSession().setAttribute("controller", controller);
		return "redirect:" + referer;
	}
}
