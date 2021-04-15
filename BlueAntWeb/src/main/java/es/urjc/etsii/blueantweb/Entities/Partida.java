package es.urjc.etsii.blueantweb.Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Partida", schema="dbo")
public class Partida {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long Id;
	
	@Column(name="fechahora")
	@DateTimeFormat(pattern = "AAAA-MM-DD hh:mm:ss.nnn")
	private String FechaHora;
	
	@OneToOne
	private Usuario Idusuario1,Idusuario2;
	private int Puntuacion1,Puntuacion2,Animopre1,Animopre2,Animopos1,Animopos2,Ganadas1,Ganadas2;
	
	public long getId() {
		return Id;
	}

	public void setId(long id) {
		this.Id = id;
	}

	public String getFechaHora() {
		return FechaHora;
	}

	public void setFechaHora(String fechaHora) {
		FechaHora = fechaHora;
	}

	public Usuario getIdUsuario1() {
		return Idusuario1;
	}

	public void setIdUsuario1(Usuario idUsuario1) {
		Idusuario1 = idUsuario1;
	}

	public Usuario getIdUsuario2() {
		return Idusuario2;
	}

	public void setIdUsuario2(Usuario idUsuario2) {
		Idusuario2 = idUsuario2;
	}

	public int getPuntuacion1() {
		return Puntuacion1;
	}

	public void setPuntuacion1(int puntuacion1) {
		Puntuacion1 = puntuacion1;
	}

	public int getPuntuacion2() {
		return Puntuacion2;
	}

	public void setPuntuacion2(int puntuacion2) {
		Puntuacion2 = puntuacion2;
	}

	public int getAnimoPre1() {
		return Animopre1;
	}

	public void setAnimoPre1(int animoPre1) {
		Animopre1 = animoPre1;
	}

	public int getAnimoPre2() {
		return Animopre2;
	}

	public void setAnimoPre2(int animoPre2) {
		Animopre2 = animoPre2;
	}

	public int getAnimoPos1() {
		return Animopos1;
	}

	public void setAnimoPos1(int animoPos1) {
		Animopos1 = animoPos1;
	}

	public int getAnimoPos2() {
		return Animopos2;
	}

	public void setAnimoPos2(int animoPos2) {
		Animopos2 = animoPos2;
	}

	public int getGanadas1() {
		return Ganadas1;
	}

	public void setGanadas1(int ganadas1) {
		Ganadas1 = ganadas1;
	}

	public int getGanadas2() {
		return Ganadas2;
	}

	public void setGanadas2(int ganadas2) {
		Ganadas2 = ganadas2;
	}

	public Partida() {
		
	}
}
