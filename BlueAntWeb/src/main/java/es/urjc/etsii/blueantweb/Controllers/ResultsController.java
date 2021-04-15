package es.urjc.etsii.blueantweb.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.urjc.etsii.blueantweb.Entities.Partida;
import es.urjc.etsii.blueantweb.Entities.Usuario;
import es.urjc.etsii.blueantweb.Repositories.EstadisticaRepository;
import es.urjc.etsii.blueantweb.Repositories.PartidaRepository;
import es.urjc.etsii.blueantweb.Repositories.UsuarioRepository;

@Controller
public class ResultsController {
	
    @Autowired
	private UsuarioRepository userRepo;
	
    @Autowired
	private EstadisticaRepository statsRepo;
	
    @Autowired
	private PartidaRepository matchRepo;
	
	@RequestMapping(value = { "/results" })
	public String results(@RequestParam String tabla, @RequestParam String columna, @RequestParam(required = false, defaultValue = "-1") String genero, @RequestParam(required = false) String rangoDesde, @RequestParam(required = false) String rangoHasta, Model model) {
		System.out.println("tabla: " + tabla);
		System.out.println("columna: " + columna);
		System.out.println("rangoDesde: " + rangoDesde);
		System.out.println("rangoHasta: " + rangoHasta);
		System.out.println("genero: " + genero);
		/*
		if (String.valueOf(tabla) == "usuario"){
	 	*/
		List<Usuario> usuarios_final = new ArrayList<>();
		List<Usuario> usuarios = new ArrayList<>();
		List<Usuario> usuarios_1 = new ArrayList<>();
		List<Usuario> usuarios_0 = new ArrayList<>();

		System.out.println("AAAAAA");
		if(columna.equals("edad"))
		{	
			System.out.println("SE BUSCA POR EDAD");
			if(genero.equals("0") || genero.equals("1")) 
				usuarios = userRepo.findByAgeAndGender(Integer.parseInt(rangoDesde), Integer.parseInt(rangoHasta),Integer.parseInt(genero));
			else
				usuarios = userRepo.findByAge(Integer.parseInt(rangoDesde), Integer.parseInt(rangoHasta));
			if (!usuarios.isEmpty() && usuarios.size() > 0) {
				System.out.println("se han encontrado "+usuarios.size()+" resultados");
			    Usuario u = usuarios.get(0);
				System.out.println(u.getNombre());
				System.out.println(u.getGenero());
				System.out.println(u.getEdadMeses());
			}
		}else if(columna.equals("genero") || !genero.equals("-1")) 
		{
			System.out.println("SE BUSCA POR GENERO " + genero);
			if (genero.equals("1") || genero.equals("2")) {
				usuarios_1 = userRepo.findByGender(Integer.parseInt("1"));
			}
			if (!usuarios_1.isEmpty() && usuarios_1.size() > 0) {
				System.out.println("se han encontrado "+usuarios_1.size()+" resultados");
			    Usuario u = usuarios_1.get(0);
				System.out.println(u.getNombre());
				System.out.println(u.getGenero());
				System.out.println(u.getEdadMeses());
			}
			if (genero.equals("0") || genero.equals("2")) {
				usuarios_0 = userRepo.findByGender(Integer.parseInt("0"));
			}
			if (!usuarios_0.isEmpty() && usuarios_0.size() > 0) {
				System.out.println("se han encontrado "+usuarios_0.size()+" resultados");
			    Usuario u = usuarios_0.get(0);
				System.out.println(u.getNombre());
				System.out.println(u.getGenero());
				System.out.println(u.getEdadMeses());
			}
		}

		usuarios_final.addAll(usuarios_0);
		usuarios_final.addAll(usuarios_1);
		usuarios_final.addAll(usuarios);
		
		for(int i=0; i<usuarios_final.size(); i++) {
			Usuario aux_u = usuarios_final.get(i);
			System.out.println("Usuario: " + aux_u.getId());
			List<Integer> lista_partidas = matchRepo.findByIdUsuario2(aux_u.getId());
			System.out.println("Tiene " + lista_partidas.size() + " partidas.");
			// si ha participado en partidas, buscar sus datos
			if(lista_partidas!=null || !lista_partidas.isEmpty())
			{
				for(int j=0; j<lista_partidas.size(); j++) {
					int aux_p_id = lista_partidas.get(j);
					System.out.println("  Partida con id: " + aux_p_id);
					List<String> lista_estadistica = statsRepo.findByIdPartida(aux_p_id);
					System.out.println("  Tiene " + lista_estadistica.size() + " subpartidas.");
					
					for(int k=0; k<lista_estadistica.size(); k++) {
						String aux_len_sub_par = lista_estadistica.get(k);
						System.out.println("    La subpartida ha durado " + aux_len_sub_par);
					}
				}
			}
		}
			
		System.out.println("BBBBBBBBB");
		
		model.addAttribute("name", "DEFAULT");
		model.addAttribute("resultados", usuarios_final);
		return "results_template";
	}
}
