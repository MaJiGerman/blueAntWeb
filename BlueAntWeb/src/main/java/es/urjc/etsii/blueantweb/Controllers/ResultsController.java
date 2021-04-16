package es.urjc.etsii.blueantweb.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
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
		List<Integer> aux_lista_consulta = new ArrayList<>();
		
		for(int i=0; i<usuarios_final.size(); i++) {
			Usuario aux_u = usuarios_final.get(i);
			aux_lista_consulta.add(aux_u.getId());
			/*
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
			*/
		}

		List<Object[]> lista_partidas = new ArrayList<>();
		int sublista_ini = 0;
		int sublista_fin = 2000;
		int step = 2000;
		
		if(aux_lista_consulta.size() < 2000)
			lista_partidas = matchRepo.findByIdUsuario2List(aux_lista_consulta);
		else {
			System.out.println("Hay que dividir la lista, hay mas de "+sublista_fin+" parametros");
			while(sublista_fin != aux_lista_consulta.size()) {
				List<Integer> sublista = aux_lista_consulta.subList(sublista_ini, sublista_fin);
				List<Object[]> parcial_lista_partidas = matchRepo.findByIdUsuario2List(sublista);
				lista_partidas.addAll(parcial_lista_partidas);
				sublista_ini = sublista_fin + 1;
				sublista_fin = sublista_fin + step;
				if(sublista_fin >= aux_lista_consulta.size())
					sublista_fin = aux_lista_consulta.size();
			}
		}
		
		HashMap<Integer, List<Integer>> hashDatosUsr = new HashMap<Integer, List<Integer>>();
		
		for (Object[] par : lista_partidas) {
		      System.out.println("Id: " + par[0] + ", IdUsuario2: " + par[1]);
		      if(hashDatosUsr.containsKey(par[1])) {
		    	  List<Integer> id_partidas = hashDatosUsr.get((Integer) par[1]);
		    	  id_partidas.add((Integer) par[0]);
		    	  hashDatosUsr.put((Integer) par[1], id_partidas);
		      }else {
		    	  List<Integer> id_partidas = new ArrayList<>();
		    	  id_partidas.add((Integer) par[0]);
		    	  hashDatosUsr.put((Integer) par[1], id_partidas);
		      }
		  }
			
		System.out.println("BBBBBBBBB");
		
		model.addAttribute("name", "DEFAULT");
		model.addAttribute("resultados", usuarios_final);
		model.addAttribute("num_resultados", usuarios_final.size());
		return "results_template";
	}
}
