package PresentationLayer.controller;

import PresentationLayer.model.GeneralModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class headerController {
    private GeneralModel controller = GeneralModel.getInstance();

    @GetMapping()
    public String header(Model model){
        return "";
    }

}
