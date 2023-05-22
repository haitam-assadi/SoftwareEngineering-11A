package PresentationLayer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class headerController {
    private GeneralController controller = GeneralController.getInstance();

    @GetMapping()
    public String header(Model model){
        return "";
    }

}
