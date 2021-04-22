package es.urjc.etsii.blueantweb.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.urjc.etsii.blueantweb.Entities.Estadistica;
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
	public String results(@RequestParam String division, @RequestParam String columna, @RequestParam(required = false, defaultValue = "-1") String genero, 
			@RequestParam(required = false) String rangoDesde, @RequestParam(required = false) String rangoHasta, 
			@RequestParam(required = false) String solo_centros, Model model) {
		
		System.out.println("division: " + division);
		System.out.println("columna: " + columna);
		System.out.println("rangoDesde: " + rangoDesde);
		System.out.println("rangoHasta: " + rangoHasta);
		System.out.println("genero: " + genero);
		System.out.println("Solo Centros: " + solo_centros);
		
		List<Usuario> usuarios_final = new ArrayList<>();
		List<Usuario> usuarios = new ArrayList<>();

		class myPartidaObject{
			public int _idPartida;
			public List<Estadistica> lista_estadisticas;
			 protected myPartidaObject(int id) {
				 this._idPartida = id;
				 this.lista_estadisticas = new ArrayList<>();
			 }
	    }
		
		class myUsuarioObject{
			public int _idUsuario;
			public List<myPartidaObject> _lista_partidas;
			 protected myUsuarioObject(int id) {
				 this._idUsuario = id;
				 this._lista_partidas = new ArrayList<>();
			 }
			 protected void setmyPartidaObject(List<myPartidaObject> lista_p) {
				 this._lista_partidas = lista_p;
			 }
			 protected void addmyPartidaObject(myPartidaObject p) {
				 this._lista_partidas.add(p);
			 }
	    }

		System.out.println("--------------------------");
		
		if(columna.equals("edad"))
		{	
			System.out.println("SE BUSCA POR EDAD");
			if(genero.equals("0") || genero.equals("1")){ 
				if(solo_centros != null) {
					usuarios = userRepo.findByAgeAndGenderAndCentre(Integer.parseInt(rangoDesde), Integer.parseInt(rangoHasta),Integer.parseInt(genero));
				}else {
					usuarios = userRepo.findByAgeAndGender(Integer.parseInt(rangoDesde), Integer.parseInt(rangoHasta),Integer.parseInt(genero));
				}
			}else {
				if(solo_centros != null) {
					usuarios = userRepo.findByAgeAndCentre(Integer.parseInt(rangoDesde), Integer.parseInt(rangoHasta));
				}else {
					usuarios = userRepo.findByAge(Integer.parseInt(rangoDesde), Integer.parseInt(rangoHasta));
				}
			}
			if (!usuarios.isEmpty() && usuarios.size() > 0) {
				System.out.println("se han encontrado "+usuarios.size()+" resultados");
			}
			else{
				model.addAttribute("filtro_edad_ini", rangoDesde);
				model.addAttribute("filtro_edad_fin", rangoHasta);
				model.addAttribute("genero", genero);
				if(solo_centros != null)
					model.addAttribute("centros", "SI");
				else
					model.addAttribute("centros", "NO");
				return "filter_empty_template";
			}
		}

		usuarios_final.addAll(usuarios);
		List<Integer> aux_lista_consulta = new ArrayList<>();
		
		for(int i=0; i<usuarios_final.size(); i++) {
			Usuario aux_u = usuarios_final.get(i);
			aux_lista_consulta.add(aux_u.getId());
		}

		/*
		 * BUSCAR TODAS LAS PARTIDAS EN LAS QUE HAN PARTICIPADO LOS USUARIOS FILTRADOS COMO USUARIO2
		 */
		List<Object[]> lista_partidas = new ArrayList<>();
		int sublista_ini = 0;
		int sublista_fin = 2000;
		int step = 2000;
		
		if(aux_lista_consulta.size() < 2000)
			lista_partidas = matchRepo.findByIdUsuario2List(aux_lista_consulta);
		else {
			System.out.println("Hay que dividir la lista de partidas consultados, hay mas de "+sublista_fin+" parametros. ("+aux_lista_consulta.size()+")");
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
		List<Integer> lista_partidas_id = new ArrayList<>();
		for (Object[] par : lista_partidas) {
			lista_partidas_id.add((Integer) par[0]);
		}
		
		/*
		 * BUSCAR TODAS LAS ESTADISTICAS EN LAS QUE HAN PARTICIPADO LOS USUARIOS FILTRADOS COMO USUARIO2
		 */
		List<Object[]> lista_estadistica = new ArrayList<>();
		// reiniciar datos para consulta
		sublista_ini = 0;
		sublista_fin = 2000;
		
		if(lista_partidas_id.size() < 2000)
			lista_estadistica = statsRepo.findByIdPartidaList(lista_partidas_id);
		else {
			System.out.println("Hay que dividir la lista de estadisticas consultadas, hay mas de "+sublista_fin+" parametros. ("+lista_partidas_id.size()+")");
			while(sublista_fin != lista_partidas_id.size()) {
				List<Integer> sublista = lista_partidas_id.subList(sublista_ini, sublista_fin);
				List<Object[]> parcial_lista_estadisticas = statsRepo.findByIdPartidaList(sublista);
				lista_estadistica.addAll(parcial_lista_estadisticas);
				
				sublista_ini = sublista_fin + 1;
				sublista_fin = sublista_fin + step;
				if(sublista_fin >= lista_partidas.size())
					sublista_fin = lista_partidas.size();
			}
		}		
		/*
		 * CREAR UN HASH MAP (TIEMPO ACCESO O(1)-O(N)) CON ID USR Y myUsuarioObject
		 */
		
		HashMap<Integer, myUsuarioObject> hashDatosUsr = new HashMap<Integer, myUsuarioObject>();
		
		for (Object[] par : lista_partidas) {
		      //System.out.println("Id Partida: " + par[0] + ", Id Usuario2: " + par[1]);
		      if(hashDatosUsr.containsKey(par[1])) {
		    	  /*
		    	   * Si esta el id del usr en el mapa, se obtiene el objeto myUsuarioObject asociado a ese id.
		    	   * Se le añade a la lista de myPartidaObject la partida actual 
		    	   */
		    	  myUsuarioObject u = hashDatosUsr.get(par[1]);
		    	  myPartidaObject p = new myPartidaObject((Integer) par[0]);
		    	  u.addmyPartidaObject(p);
		    	  hashDatosUsr.put((Integer) par[1], u);
		      }else {
		    	  /*
		    	   * Si no esta el id del usr en el mapa, se crea un objeto myUsuarioObject nuevo.
		    	   * Se le añade una partida a la lista de myPartidaObject con las estadisticas vacias
		    	   */
		    	  myUsuarioObject u = new myUsuarioObject((Integer) par[1]);
		    	  myPartidaObject p = new myPartidaObject((Integer) par[0]);
		    	  List<myPartidaObject> aux_lista_p = new ArrayList<>();
		    	  aux_lista_p.add(p);
		    	  u.setmyPartidaObject(aux_lista_p);
		    	  hashDatosUsr.put((Integer) par[1], u);
		      }
		  }

		System.out.println("--------------------------");
		
		model.addAttribute("name", "DEFAULT");
		model.addAttribute("resultados", usuarios_final);
		model.addAttribute("num_resultados", usuarios_final.size());
		return "results_template";
	}
}
