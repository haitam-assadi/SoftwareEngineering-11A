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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {
	private static boolean isMarketInit = false;
	private Server server = Server.getInstance();
	GeneralModel controller;
	List<ProductDTO> products; // = new ArrayList<>()
	Alert alert = Alert.getInstance();

	@GetMapping("/")
	public String index(HttpServletRequest request, Model model) {
		String referer = request.getHeader("referer");
		controller = new GeneralModel();
		products = null;
		if(!isMarketInit) {
			ResponseT<Boolean> response = server.initializeMarket();
			if (response.ErrorOccurred) {
				alert.setFail(true);
				alert.setMessage(response.errorMessage);
				model.addAttribute("controller", controller);
				model.addAttribute("alert", alert.copy());
				model.addAttribute("products", products);
				return "index"; // "/" ???
			}
			isMarketInit = true;
		}
//		GeneralModel controller = new GeneralModel();
		if(request.getSession().getAttribute("controller") != null){
			controller = (GeneralModel) request.getSession().getAttribute("controller");
			if(referer.contentEquals(request.getRequestURL()))
				products = (List<ProductDTO>) request.getSession().getAttribute("products");
		}

		if(controller.getName().equals("")){ // new client
			String name = server.enterMarket().value;
			controller.setName(name);
			request.getSession().setAttribute("controller",controller);
			request.getSession().setAttribute("products",null);
		}
		model.addAttribute("controller", controller);
		model.addAttribute("alert", alert.copy());
		model.addAttribute("products", products);
		alert.reset();
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
//		controller = new GeneralModel(); // TODO: ???
		controller.setName(response.value);
		controller.setLogged(false);
		controller.setSystemManager(false);
		controller.setHasRole(false);
		request.getSession().setAttribute("controller", controller);
		request.getSession().setAttribute("products", null);
//		products = null; // TODO: ???
		return "redirect:/"; // TODO: redirect + request
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
}
