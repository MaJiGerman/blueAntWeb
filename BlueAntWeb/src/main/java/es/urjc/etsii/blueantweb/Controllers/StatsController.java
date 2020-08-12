package es.urjc.etsii.blueantweb.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StatsController {

	@RequestMapping(value = { "/stats" })
	public String home(Model model) {
		model.addAttribute("name", "GERMAN");
		return "stats_template";
	}
}
