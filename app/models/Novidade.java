package models;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Um acontecimento (visto ou não) na linha do tempo de um cidadão.
 */
public class Novidade implements Serializable{

	private static final long serialVersionUID = -6390884039407731417L;

	public static final String TIPO_OPINIAO = "opiniao";

    private String tipo;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "iniciativa")
//    private Iniciativa iniciativa;

    private Opiniao opiniao;

	public Novidade(){
        this.tipo = TIPO_OPINIAO;
	}

    public Novidade(Opiniao opiniao) {
		this();
        this.opiniao = opiniao;
//        this.iniciativa = opiniao.getIniciativa();
    }

    public String getTipo() {
        return tipo;
    }

//    public Iniciativa getIniciativa() {
//        return iniciativa;
//    }
//
//    public void setIniciativa(Iniciativa iniciativa) {
//        this.iniciativa = iniciativa;
//    }

	public Opiniao getOpiniao() {
		return opiniao;
	}

	public void setOpiniao(Opiniao opiniao) {
		this.opiniao = opiniao;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
    
    
}
