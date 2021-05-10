package es.urjc.etsii.blueantweb.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StatsController {
    
	@RequestMapping(value = { "/stats" })
	public String stats_name(@RequestParam(defaultValue="german", value="usrName") String name, Model model) {
		try {
			model.addAttribute("name", name);
		} catch (Exception e) {
			model.addAttribute("name", "DEFAULT");
		}
		
		return "stats_template";
	}

}
