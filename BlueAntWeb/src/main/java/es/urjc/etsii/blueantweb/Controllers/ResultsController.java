package es.urjc.etsii.blueantweb.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    
    class myPartidaObject{
		public int _idPartida;
		public List<Integer> lista_estadistica_tiempo2;
		 protected myPartidaObject(int id) {
			 this._idPartida = id;
			 this.lista_estadistica_tiempo2 = new ArrayList<>();
		 }
		 protected void addEstadistica(Integer e_t2) {
			 this.lista_estadistica_tiempo2.add(e_t2);
		 }
		 protected List<Integer> getEstadistica(){
			 return this.lista_estadistica_tiempo2;
		 }
		 public String toString() {
			 String return_string = "idPartida: " + _idPartida +"\n		[";
			 for (Integer tiempo : lista_estadistica_tiempo2) {
				 return_string += ""+ tiempo +", ";
			 }
			 return_string = return_string.substring(0, return_string.length() - 2);
			 return_string += "]\n";
			 return return_string;
		 }
    }
	
	class myUsuarioObject{
		private int _idUsuario;
		private Usuario _u;
		private List<myPartidaObject> _lista_partidas;
		
		 protected myUsuarioObject(int id) {
			 this._idUsuario = id;
			 this._lista_partidas = new ArrayList<>();
		 }
		 protected void setUsuario(Usuario u) {
			 this._u = u;
		 }
		 protected void addmyPartidaObject(myPartidaObject p) {
			 this._lista_partidas.add(p);
		 }
		 protected List<myPartidaObject> getmyPartidaObject() {
			 return this._lista_partidas;
		 }
		 public String toString() {
			 String return_string = "\nidUsuario: " + _idUsuario +"\n{\n";
			 return_string += _u.toString();
			 for (int i=0; i<_lista_partidas.size();i++) {
				 myPartidaObject aux_p = _lista_partidas.get(i);
				 if (aux_p != null){
					 return_string += "	"+aux_p.toString();
				 }
			 }
			 return_string += "\n}";
			 return return_string;
		 }
    }
	
	@RequestMapping(value = { "/results" })
	public String results(@RequestParam(required = false, defaultValue = "-1") String genero, 
			@RequestParam(required = false) String rangoDesde, @RequestParam(required = false) String rangoHasta, 
			@RequestParam(required = false) String solo_centros, @RequestParam(required = false) String agrupacion_resultados, 
			@RequestParam(required = false) String tipo_grafico, Model model) {

		System.out.println("--------------------------");
		System.out.println("DATOS DE CONSULTA");
		System.out.println("rangoDesde: " + rangoDesde);
		System.out.println("rangoHasta: " + rangoHasta);
		System.out.println("genero: " + genero);
		System.out.println("Solo Centros: " + solo_centros);
		System.out.println("Agrupar resultados por: " + agrupacion_resultados);
		System.out.println("Tipo grafico: " + tipo_grafico);
		
		
		List<Usuario> usuarios = new ArrayList<>();
		String final_titulo_grafica = "";
		List<List<Integer>> final_datos_grafica = new ArrayList<>();
		List<String> final_nombres_grafica = new ArrayList<>();
		
		System.out.println("*******************");
		
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

		if(tipo_grafico.equals("num_jugadores")) {
			final_titulo_grafica += "NUMERO DE JUGADORES";
		}else if(tipo_grafico.equals("num_partidas")) {
			final_titulo_grafica += "NUMERO DE PARTIDAS JUGADAS";
		}else if(tipo_grafico.equals("media_duracion")) {
			final_titulo_grafica += "MEDIA DURACION DE LAS PARTIDAS";
		}else if(tipo_grafico.equals("mediana_duracion")) {
			final_titulo_grafica += "MEDIANA DURACION DE LAS PARTIDAS";
		}
		final_titulo_grafica += " ENTRE " + rangoDesde + " Y " + rangoHasta + " MESES";
		final_titulo_grafica += " AGRUPADOS POR " + agrupacion_resultados.toUpperCase();
		
		if(agrupacion_resultados.equals("edad")) {
			final_nombres_grafica.add("Genero");
			final_nombres_grafica.add("Ambos");
		}else {
			final_nombres_grafica.add("Genero");
			final_nombres_grafica.add("Masculino");
			final_nombres_grafica.add("Femenino");
		}
		
		List<Integer> aux_lista_consulta = new ArrayList<>();
		HashMap<Integer, Usuario> hashUsuarios = new HashMap<Integer, Usuario>();
		
		HashMap<Integer, List<List<Integer>>> hashResultadosDivididos = new HashMap<Integer, List<List<Integer>>>();

		for(int i=0; i<usuarios.size(); i++) {
			Usuario aux_u = usuarios.get(i);

			int edadAnno = -1;
			if(agrupacion_resultados.equals("genero")) {
				if(aux_u.getGenero() == 0)
					edadAnno = 0;
				else if(aux_u.getGenero() == 1)	
					edadAnno = 0;
			}else {
				int edadMes = aux_u.getEdadMeses();
				int resto = edadMes % 12;
				if(resto != 0) 
					resto = 1;
				edadAnno = (edadMes / 12) + resto;
			}
			
			Integer keyEdad = edadAnno;
			
			if(hashResultadosDivididos.containsKey(keyEdad)) {
				List<List<Integer>> l = hashResultadosDivididos.get(keyEdad);
				if(agrupacion_resultados.equals("edad-genero") || agrupacion_resultados.equals("genero")){
					List<Integer> l_m = l.get(0);
					List<Integer> l_f = l.get(1);
					if(aux_u.getGenero() == 0) {
						l_m.add(aux_u.getId());
						l.set(0,l_m);
					}else if(aux_u.getGenero() == 1) {	
						l_f.add(aux_u.getId());
						l.set(1,l_f);
					}
				}else {
					List<Integer> l_a = l.get(0);
					l_a.add(aux_u.getId());
					l.set(0,l_a);
				}
				hashResultadosDivididos.put(keyEdad, l);
			}else {
				List<List<Integer>> l = new ArrayList<>();
				if(agrupacion_resultados.equals("edad-genero") || agrupacion_resultados.equals("genero")){
					List<Integer> l_m = new ArrayList<>();
					List<Integer> l_f = new ArrayList<>();
					if(aux_u.getGenero() == 0)
						l_m.add(aux_u.getId());
					else if(aux_u.getGenero() == 1)	
						l_f.add(aux_u.getId());
					l.add(l_m);
					l.add(l_f);
				}else {
					List<Integer> l_a = new ArrayList<>();
					l_a.add(aux_u.getId());
					l.add(l_a);
				}
				hashResultadosDivididos.put(keyEdad, l);
			}
			
			aux_lista_consulta.add(aux_u.getId());
			hashUsuarios.put(aux_u.getId(), aux_u);
		}
		
		
		System.out.println("DIVISION USUARIOS: " + hashResultadosDivididos.keySet());
		
		for (Integer key: hashResultadosDivididos.keySet()) {
		    System.out.println(key + "=" + hashResultadosDivididos.get(key));
		}
				
		/*
		 * SI SOLO SE QUIERE EL NUMERO DE JUGADORES, EVITAMOS HACER CONSULTAS INNECESARIAS
		 */
		if(tipo_grafico.equals("num_jugadores")){
			for (Integer key: hashResultadosDivididos.keySet()) {
				List<Integer> aux_lista = new ArrayList<>();
				List<List<Integer>> aux_lista_data = new ArrayList<>();
				
				aux_lista.add(key);
				aux_lista_data = hashResultadosDivididos.get(key); 
				for(int i=0; i<aux_lista_data.size(); i++) {
					aux_lista.add(aux_lista_data.get(i).size());
				}
				final_datos_grafica.add(aux_lista);
			}
			model.addAttribute("resultados", usuarios);
			model.addAttribute("titulo_grafica", final_titulo_grafica);
			model.addAttribute("datos_grafica", final_datos_grafica);
			model.addAttribute("nombres_grafica", final_nombres_grafica);
			model.addAttribute("num_resultados", usuarios.size());
			return "results_template";
		}

		/*
		 * BUSCAR TODAS LAS PARTIDAS EN LAS QUE HAN PARTICIPADO LOS USUARIOS FILTRADOS COMO USUARIO2
		 */
		List<Object[]> lista_partidas = new ArrayList<>();
		int sublista_ini = 0;
		int sublista_fin = 2000;
		int step = 2000;
		boolean salir = false;
		
		if(aux_lista_consulta.size() < 2000)
			lista_partidas = matchRepo.findByIdUsuario2List(aux_lista_consulta);
		else {
			System.out.println("Hay que dividir la lista de partidas consultados, hay mas de "+sublista_fin+" parametros. ("+aux_lista_consulta.size()+")");
			while(salir == false) {
				List<Integer> sublista = aux_lista_consulta.subList(sublista_ini, sublista_fin);
				List<Object[]> parcial_lista_partidas = matchRepo.findByIdUsuario2List(sublista);
				lista_partidas.addAll(parcial_lista_partidas);
				if(sublista_fin == aux_lista_consulta.size())
					salir = true;
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
		salir = false;
		
		if(lista_partidas_id.size() < 2000)
			lista_estadistica = statsRepo.findByIdPartidaList(lista_partidas_id);
		else {
			System.out.println("Hay que dividir la lista de estadisticas consultadas, hay mas de "+sublista_fin+" parametros. ("+lista_partidas_id.size()+")");
			while(salir == false) {
				List<Integer> sublista = lista_partidas_id.subList(sublista_ini, sublista_fin);
				List<Object[]> parcial_lista_estadisticas = statsRepo.findByIdPartidaList(sublista);
				lista_estadistica.addAll(parcial_lista_estadisticas);
				if(sublista_fin == lista_partidas_id.size())
					salir = true;
				sublista_ini = sublista_fin + 1;
				sublista_fin = sublista_fin + step;
				if(sublista_fin >= lista_partidas.size())
					sublista_fin = lista_partidas.size();
			}
		}		
		
		HashMap<Integer, myPartidaObject> hashDatosPartidas = new HashMap<Integer, myPartidaObject>();
		
		for (Object[] par_e : lista_estadistica) {
		      if(hashDatosPartidas.containsKey(par_e[0])) {
		    	  /*
		    	   * Si esta el id de la partida en el mapa, se obtiene el objeto myPartidaObject asociado a ese id de partida.
		    	   * Se le añade el tiempo2 de estadistica asociada al idPartida actual a la lista
		    	   */
		    	  myPartidaObject p = hashDatosPartidas.get(par_e[0]);
		    	  Integer e_tiempo2 = (Integer) par_e[1];
		    	  p.addEstadistica(e_tiempo2);
		    	  hashDatosPartidas.put((Integer) par_e[0], p);
		      }else {
		    	  /*
		    	   * Si no esta el id de la partida en el mapa, se crea un objeto myPartidaObject nuevo.
		    	   * Se le añade el tiempo2 de estadistica asociada al idPartida actual a la lista
		    	   */
		    	  myPartidaObject p = new myPartidaObject((Integer) par_e[0]);
		    	  Integer e_tiempo2 = (Integer) par_e[1];
		    	  p.addEstadistica(e_tiempo2);
		    	  hashDatosPartidas.put((Integer) par_e[0], p);
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
		    	  myPartidaObject p = hashDatosPartidas.get(par[0]);
		    	  u.addmyPartidaObject(p);
		    	  hashDatosUsr.put((Integer) par[1], u);
		      }else {
		    	  /*
		    	   * Si no esta el id del usr en el mapa, se crea un objeto myUsuarioObject nuevo.
		    	   * Se le añade a la lista de myPartidaObject la partida actual 
		    	   */
		    	  myUsuarioObject u = new myUsuarioObject((Integer) par[1]);
		    	  myPartidaObject p = hashDatosPartidas.get(par[0]);
		    	  u.addmyPartidaObject(p);
		    	  u.setUsuario(hashUsuarios.get((Integer) par[1]));
		    	  hashDatosUsr.put((Integer) par[1], u);
		      }
		  }
		// MOSTRAR LOS DATOS OBTENIDOS
		/*
		for (Integer index: hashDatosUsr.keySet()) {
		    String value = hashDatosUsr.get(index).toString();
		    System.out.println(value);
		}
		*/
		
		System.out.println("*******************");

		List<List<Double>> final_datos_grafica_2 = new ArrayList<>();
		
		for (Integer key: hashResultadosDivididos.keySet()) {
			List<Double> aux_lista = new ArrayList<>();
			List<Integer> aux_lista_todas_partidas = new ArrayList<>();
			List<List<Integer>> aux_lista_data = new ArrayList<>();
			double num_partidas = 0;
			
			aux_lista.add((double) key);
			aux_lista_data = hashResultadosDivididos.get(key);
			
			System.out.println("CALCULANDO EN EL GRUPO "+ key);
			
			// por cada una de las listas de usuarios (separados)
			for(int i=0; i<aux_lista_data.size(); i++) {
				num_partidas = 0;
				aux_lista_todas_partidas = new ArrayList<>();
				List<Integer> aux_lista_usr_id = aux_lista_data.get(i);
				// por cada uno de los usuarios
				for(int j=0; j<aux_lista_usr_id.size(); j++) {
					int id_usr = aux_lista_usr_id.get(j);
					if(hashDatosUsr.containsKey(id_usr)){
						myUsuarioObject u = hashDatosUsr.get(id_usr);
						List<myPartidaObject> lista_myP = u.getmyPartidaObject();
						// por cada una de las partidas
						for(int k=0; k<lista_myP.size(); k++) {
							myPartidaObject p = lista_myP.get(k);
							if((p != null)) {
								List<Integer> e = p.getEstadistica();
								aux_lista_todas_partidas.addAll(e);
								num_partidas += e.size();
							}
						}
					}
				}
				// Calcular el numero de partidas de esta listas de usuarios
				if(tipo_grafico.equals("num_partidas")){
					aux_lista.add(num_partidas);
				}
				// Calcular la media de esta listas de usuarios
				if(tipo_grafico.equals("media_duracion")){
					double suma = 0;
					for(int index=0; index<aux_lista_todas_partidas.size();index++) {
						suma += aux_lista_todas_partidas.get(index);
					}
					double media = suma / aux_lista_todas_partidas.size();
					//System.out.println("MEDIA: "+suma+" / "+aux_lista_todas_partidas.size()+" = "+media);
					aux_lista.add(media);
				}
				// Calcular la desviacion media de esta listas de usuarios
				if(tipo_grafico.equals("des_media_duracion")){
					double suma = 0;
					double desviacion_media = 0;
					for(int index=0; index<aux_lista_todas_partidas.size();index++) {
						suma += aux_lista_todas_partidas.get(index);
					}
					double media = suma / aux_lista_todas_partidas.size();
					
					for(int index=0; index<aux_lista_todas_partidas.size();index++) {
						desviacion_media += Math.pow(aux_lista_todas_partidas.get(index) - media, 2);
					}
					desviacion_media = Math.sqrt(desviacion_media / aux_lista_todas_partidas.size());
					aux_lista.add(desviacion_media);
				}
				// Calcular la mediana de esta listas de usuarios
				if(tipo_grafico.equals("mediana_duracion")){
					aux_lista_todas_partidas.sort(null);
					List<Integer> aux_l = aux_lista_todas_partidas;
					//System.out.println(aux_l);
					double mediana = 0;
					if(aux_l.size() > 0) {
						if (aux_l.size() % 2 == 0)
							mediana = (aux_l.get(aux_l.size()/2) + aux_l.get((aux_l.size() - 1)/2)) / 2;
						else
							mediana = aux_l.get(aux_l.size()/2);
					}
					//System.out.println("MEDIANA: "+mediana);
					aux_lista.add(mediana);
				}
			}
			
			final_datos_grafica_2.add(aux_lista);
		}
		
		model.addAttribute("resultados", usuarios);
		model.addAttribute("titulo_grafica", final_titulo_grafica);
		model.addAttribute("datos_grafica", final_datos_grafica_2);
		model.addAttribute("nombres_grafica", final_nombres_grafica);
		model.addAttribute("num_resultados", usuarios.size());
		return "results_template";
	}
}
