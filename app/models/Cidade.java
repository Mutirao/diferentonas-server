package models;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@NamedQueries({ @NamedQuery(name = "findComIniciativas", query = "SELECT c FROM Cidade c JOIN FETCH c.iniciativas WHERE c.id = :paramId") })
public class Cidade implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3104811717507160248L;
    public static final String TABLE = "Cidade";
    @Id
    private Long id;
    private String nome;
    private String uf;
    private Float idhm;
    private Float idhmRenda;
    private Float idhmLongevidade;
    private Float idhmEducacao;
    private Long populacao;
    private Float latitude;
    private Float longitude;
    private Float altitude;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "cidades_similares",
            joinColumns = {@JoinColumn(name = "origin_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "similar_id", referencedColumnName = "id")})
    @JsonBackReference
    private List<Cidade> similares;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cidade")
    @JsonIgnore
    private List<Score> scores;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cidade", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Iniciativa> iniciativas;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cidade", targetEntity=Novidade.class)
    @JsonIgnore
    private List<Novidade> novidades;

    @Transient
    private boolean seguidaPeloRequisitante;

    
    public Cidade() {
        this.similares = new LinkedList<>();
        this.scores = new LinkedList<>();
        this.iniciativas = new LinkedList<>();
        this.novidades = new LinkedList<>();
    }

    public Cidade(Long id, String nome, String uf, Float idhm, Float idhmRenda,
                  Float idhmLongevidade, Float idhmEducacao, Long populacao, Float latitude, Float longitude, Float altitude) {
        this();
        this.id = id;
        this.nome = nome;
        this.uf = uf;
        this.idhm = idhm;
        this.idhmRenda = idhmRenda;
        this.idhmLongevidade = idhmLongevidade;
        this.idhmEducacao = idhmEducacao;
        this.populacao = populacao;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public Float getIdhm() {
        return idhm;
    }

    public void setIdhm(Float idhm) {
        this.idhm = idhm;
    }

    public Float getIdhmRenda() {
        return idhmRenda;
    }

    public void setIdhmRenda(Float idhmRenda) {
        this.idhmRenda = idhmRenda;
    }

    public Float getIdhmLongevidade() {
        return idhmLongevidade;
    }

    public void setIdhmLongevidade(Float idhmLongevidade) {
        this.idhmLongevidade = idhmLongevidade;
    }

    public Float getIdhmEducacao() {
        return idhmEducacao;
    }

    public void setIdhmEducacao(Float idhmEducacao) {
        this.idhmEducacao = idhmEducacao;
    }

    public Long getPopulacao() {
        return populacao;
    }

    public void setPopulacao(Long populacao) {
        this.populacao = populacao;
    }
    
    public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Float getAltitude() {
		return altitude;
	}

	public void setAltitude(Float altitude) {
		this.altitude = altitude;
	}

	public List<Cidade> getSimilares() {
        return similares;
    }

    public void setSimilares(List<Cidade> similares) {
        this.similares = similares;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    public List<Iniciativa> getIniciativas() {
        return iniciativas;
    }

    public void setIniciativas(List<Iniciativa> iniciativas) {
        this.iniciativas = iniciativas;
    }

    public List<Novidade> getNovidades() {
		return novidades;
	}

	public void setNovidades(List<Novidade> novidades) {
		this.novidades = novidades;
	}

	@Override
	public String toString() {
		return "Cidade [id=" + id + ", nome=" + nome + ", uf=" + uf + ", idhm="
				+ idhm + ", idhmRenda=" + idhmRenda + ", idhmLongevidade="
				+ idhmLongevidade + ", idhmEducacao=" + idhmEducacao
				+ ", populacao=" + populacao + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", altitude=" + altitude + "]";
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
        Cidade other = (Cidade) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

	public void criaScore(Score score) {
		scores.add(score);
		score.setCidade(this);
	}

	public void atualizaScore(Score scoreAtualizado, Date dataDaAtualizacao) {
		for (Score score : scores) {
			if (score.getArea().equals(scoreAtualizado.getArea())) {
				// TODO criar coleção de novidades no score e migrar esse if pra dentro do score.atualiza
				if(Math.abs(score.compareTo(scoreAtualizado)) >= 1){
					novidades.add(new Novidade(TipoDaNovidade.ATUALIZACAO_DE_SCORE, dataDaAtualizacao, this, score));
				}
				score.atualiza(scoreAtualizado);
				return;
			}
		}
		scores.add(scoreAtualizado);
		scoreAtualizado.setCidade(this);
		novidades.add(new Novidade(TipoDaNovidade.NOVO_SCORE, dataDaAtualizacao, this, scoreAtualizado));
	}

	public void addIniciativa(Iniciativa iniciativa, Date dataDaAtualizacao, boolean publicaNovidade) {
		iniciativas.add(iniciativa);
		iniciativa.setCidade(this);
		if(publicaNovidade){
			novidades.add(new Novidade(TipoDaNovidade.NOVA_INICIATIVA, dataDaAtualizacao, this, iniciativa));
		}
	}

	public boolean isSeguidaPeloRequisitante() {
		return seguidaPeloRequisitante;
	}

	public void setSeguidaPeloRequisitante(boolean seguidaPeloRequisitante) {
		this.seguidaPeloRequisitante = seguidaPeloRequisitante;
	}
}
