package es.urjc.etsii.blueantweb.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import es.urjc.etsii.blueantweb.Repositories.UsuarioRepository;

@Controller
public class FilterController {
	
    @Autowired
	private UsuarioRepository userRepo;
    
	@RequestMapping(value = { "/filter" })
	public String stats_name(Model model) {
		List<Integer> lista_experiencias = userRepo.findDistincExp();
		//System.out.println(lista_experiencias);
		model.addAttribute("lista_exp", lista_experiencias);
		return "filter_template";
	}

}
