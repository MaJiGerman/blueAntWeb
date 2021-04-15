package es.urjc.etsii.blueantweb.Controllers;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.urjc.etsii.blueantweb.Entities.Estadistica;
import es.urjc.etsii.blueantweb.Entities.Usuario;
import es.urjc.etsii.blueantweb.Repositories.EstadisticaRepository;
import es.urjc.etsii.blueantweb.Repositories.UsuarioRepository;

@Controller
public class StatsController {
	
	@Autowired
    protected DataSource dataSource;

    public void showTables() throws Exception {
        DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
        ResultSet tables = metaData.getTables(null, null, null, new String[] { "TABLE" });
        while (tables.next()) {
            String tableName=tables.getString("TABLE_NAME");
            System.out.println(tableName);
            ResultSet columns = metaData.getColumns(null,  null,  tableName, "%");
            while (columns.next()) {
                String columnName=columns.getString("COLUMN_NAME");
                System.out.println("\t" + columnName);
            }
        }
    }
    
    @Autowired
	private EstadisticaRepository statsRepo;    
    @Autowired
	private UsuarioRepository userRepo;
    
	@RequestMapping(value = { "/stats" })
	public String stats_name(@RequestParam(defaultValue="german", value="usrName") String name, Model model) {
		try {
			// showTables();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			//Usuario u = userRepo.findById(Integer.parseInt(name));
			//System.out.println(u.getNombre());
			model.addAttribute("name", name);
		} catch (Exception e) {
			System.out.println("no se encuentra a "+name);
			model.addAttribute("name", "DEFAULT");
		}
		
		/*
		Estadistica e = statsRepo.findById(1);
		System.out.println(e);
		*/
		
		// model.addAttribute("name", u.getNombre());
		return "stats_template";
	}

}
