package models;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Um acontecimento (visto ou não) na linha do tempo de um cidadão.
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Novidade implements Serializable{

	private static final long serialVersionUID = -6390884039407731417L;
	
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column
	private TipoDaNovidade tipo;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm", timezone="America/Recife")
    private Date criadaEm;

    @OneToOne
    private Opiniao opiniao;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Iniciativa iniciativa;

    @ManyToOne(fetch = FetchType.EAGER)
	private Cidade cidade;

    @ManyToOne(fetch = FetchType.EAGER)
	private Score score;
    
    private String nomeDoCampo;
    
    @Column(columnDefinition = "TEXT")
    private String valorAntigo;

    @Column(columnDefinition = "TEXT")
	private String valorNovo;

	public Novidade(){
		
	}

    public Novidade(TipoDaNovidade tipo, Opiniao opiniao, Iniciativa iniciativa, Cidade cidade) {
		this.tipo = tipo;
		this.criadaEm = opiniao.getCriadaEm();
        this.opiniao = opiniao;
		this.iniciativa = iniciativa;
		this.cidade = cidade;
    }

	public Novidade(TipoDaNovidade tipo, Date data, Cidade cidade,
			Iniciativa iniciativa) {
		this.tipo = tipo;
		this.criadaEm = data;
		this.cidade = cidade;
		this.iniciativa = iniciativa;
	}

	public Novidade(TipoDaNovidade tipo, Date data, Cidade cidade,
			Iniciativa iniciativa, String nomeDoCampo, String valorAntigo, String valorNovo) {
		this.tipo = tipo;
		this.criadaEm = data;
		this.cidade = cidade;
		this.iniciativa = iniciativa;
		this.nomeDoCampo = nomeDoCampo;
		this.valorAntigo = valorAntigo;
		this.valorNovo = valorNovo;
	}

	public Novidade(TipoDaNovidade tipo, Date data, Cidade cidade,
			Score score) {
		this.tipo = tipo;
		this.criadaEm = data;
		this.cidade = cidade;
		this.score = score;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public TipoDaNovidade getTipo() {
		return tipo;
	}

	public void setTipo(TipoDaNovidade tipo) {
		this.tipo = tipo;
	}

	public Date getCriadaEm() {
		return criadaEm;
	}

	public void setCriadaEm(Date criadaEm) {
		this.criadaEm = criadaEm;
	}

	public Opiniao getOpiniao() {
		return opiniao;
	}

	public void setOpiniao(Opiniao opiniao) {
		this.opiniao = opiniao;
	}
	
	public Iniciativa getIniciativa() {
		return iniciativa;
	}

	public void setIniciativa(Iniciativa iniciativa) {
		this.iniciativa = iniciativa;
	}
	
	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}
	
	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}
	
	public String getNomeDoCampo() {
		return nomeDoCampo;
	}

	public void setNomeDoCampo(String nomeDoCampo) {
		this.nomeDoCampo = nomeDoCampo;
	}

	public String getValorAntigo() {
		return valorAntigo;
	}

	public void setValorAntigo(String valorAntigo) {
		this.valorAntigo = valorAntigo;
	}
	
	public String getValorNovo() {
		return valorNovo;
	}

	public void setValorNovo(String valorNovo) {
		this.valorNovo = valorNovo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Novidade other = (Novidade) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Novidade [id=" + id + ", tipo=" + tipo + ", criadaEm="
				+ criadaEm + "]";
	}
	
    
}
