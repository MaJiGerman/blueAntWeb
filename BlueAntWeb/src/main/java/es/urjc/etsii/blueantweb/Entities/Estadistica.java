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
@Table(name = "Estadistica", schema="dbo")
public class Estadistica {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long Id;
	
	@Column(name="Tiempoinicio")
	@DateTimeFormat(pattern = "AAAA-MM-DD hh:mm:ss.nnn")
	private String TiempoInicio;
	
	@OneToOne
	private Partida Idpartida;
	
	private int Nivel,Tiempo1,Tiempo2,Pasosprop,Pasosdados,Pasosoptimo,Ganador;
	private String Vectorposiciones, Vectorprop,Vectorejecutado,Vectoroptimo;
	
	public long getId() {
		return Id;
	}

	public void setId(long id) {
		this.Id = id;
	}

	public String getTiempoInicio() {
		return TiempoInicio;
	}

	public void setTiempoInicio(String tiempoInicio) {
		TiempoInicio = tiempoInicio;
	}

	public Partida getIdPartida() {
		return Idpartida;
	}

	public void setIdPartida(Partida idPartida) {
		Idpartida = idPartida;
	}

	public long getNivel() {
		return Nivel;
	}

	public void setNivel(int nivel) {
		Nivel = nivel;
	}

	public long getTiempo1() {
		return Tiempo1;
	}

	public void setTiempo1(int tiempo1) {
		Tiempo1 = tiempo1;
	}

	public long getTiempo2() {
		return Tiempo2;
	}

	public void setTiempo2(int tiempo2) {
		Tiempo2 = tiempo2;
	}

	public int getPasosProp() {
		return Pasosprop;
	}

	public void setPasosProp(int pasosProp) {
		Pasosprop = pasosProp;
	}

	public int getPasosDados() {
		return Pasosdados;
	}

	public void setPasosDados(int pasosDados) {
		Pasosdados = pasosDados;
	}

	public int getPasosOptimo() {
		return Pasosoptimo;
	}

	public void setPasosOptimo(int pasosOptimo) {
		Pasosoptimo = pasosOptimo;
	}

	public int getGanador() {
		return Ganador;
	}

	public void setGanador(int ganador) {
		Ganador = ganador;
	}

	public String getVectorPosiciones() {
		return Vectorposiciones;
	}

	public void setVectorPosiciones(String vectorPosiciones) {
		Vectorposiciones = vectorPosiciones;
	}

	public String getVectorProp() {
		return Vectorprop;
	}

	public void setVectorProp(String vectorProp) {
		Vectorprop = vectorProp;
	}

	public String getVectorEjecutado() {
		return Vectorejecutado;
	}

	public void setVectorEjecutado(String vectorEjecutado) {
		Vectorejecutado = vectorEjecutado;
	}

	public String getVectorOptimo() {
		return Vectoroptimo;
	}

	public void setVectorOptimo(String vectorOptimo) {
		Vectoroptimo = vectorOptimo;
	}

	public Estadistica() {
		
	}
}
