package es.urjc.etsii.blueantweb.Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Usuario", schema="dbo")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int Id;
	
	@Column(name="fechaalta")
	@DateTimeFormat(pattern = "AAAA-MM-DD hh:mm:ss.nnn")
	private String FechaAlta;
	
	@Column(name="claseletra")
	private String ClaseLetra;
	
	private String Publicacion,Nombre,Centro,Otros;
	private int Exp,clasenumero,Edadmeses,Genero,Necesp,Usoprog,Usodisp,Usoplat;
	
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getFechaAlta() {
		return FechaAlta;
	}

	public void setFechaAlta(String fechaAlta) {
		FechaAlta = fechaAlta;
	}

	public String getPublicacion() {
		return Publicacion;
	}

	public void setPublicacion(String publicacion) {
		Publicacion = publicacion;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public String getCentro() {
		return Centro;
	}

	public void setCentro(String centro) {
		Centro = centro;
	}

	public String getClaseLetra() {
		return ClaseLetra;
	}

	public void setClaseLetra(String claseLetra) {
		ClaseLetra = claseLetra;
	}

	public String getOtros() {
		return Otros;
	}

	public void setOtros(String otros) {
		Otros = otros;
	}

	public int getExp() {
		return Exp;
	}

	public void setExp(int exp) {
		Exp = exp;
	}

	public int getClaseNumero() {
		return clasenumero;
	}

	public void setClaseNumero(int claseNumero) {
		clasenumero = claseNumero;
	}

	public int getEdadMeses() {
		return Edadmeses;
	}

	public void setEdadMeses(int edadMeses) {
		Edadmeses = edadMeses;
	}

	public int getGenero() {
		return Genero;
	}

	public void setGenero(int genero) {
		Genero = genero;
	}

	public int getNecEsp() {
		return Necesp;
	}

	public void setNecEsp(int necEsp) {
		Necesp = necEsp;
	}

	public int getUsoProg() {
		return Usoprog;
	}

	public void setUsoProg(int usoProg) {
		Usoprog = usoProg;
	}

	public int getUsoDisp() {
		return Usodisp;
	}

	public void setUsoDisp(int usoDisp) {
		Usodisp = usoDisp;
	}

	public int getUsoPlat() {
		return Usoplat;
	}

	public void setUsoPlat(int usoPlat) {
		Usoplat = usoPlat;
	}

	public Usuario(){
		
	}
}
