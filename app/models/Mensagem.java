/**
 * 
 */
package models;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonFormat;

import play.data.validation.Constraints;


/**
 * @author ricardoas
 *
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "all", query = "SELECT m FROM Mensagem m ORDER BY criadaEm desc"),
		@NamedQuery(name = "naolidas", query = "SELECT m FROM Mensagem m WHERE m.criadaEm > :dataDaUltimaLida ORDER BY criadaEm desc") })
public class Mensagem implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1169792076018944039L;

	public static final String TABLE = "Mensagem";
	
	@Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	
    @Column(length = 1000)
    @Constraints.MaxLength(value = 1000,message = "Mensagens devem ter 1000 caracteres ou menos")
    @Constraints.Required(message = "Campo necessário")
    @Constraints.MinLength(1)
    private String conteudo;
    
    @Column(length = 100)
    @Constraints.MaxLength(value = 100,message = "Títulos devem ter 1000 caracteres ou menos")
    @Constraints.Required(message = "Campo necessário")
    @Constraints.MinLength(1)
	private String titulo;

    @Column(length = 1000)
    @Constraints.MaxLength(value = 1000,message = "Mensagens devem ter 1000 caracteres ou menos")
    @Constraints.Required(message = "Campo necessário")
    @Constraints.MinLength(1)
    private String autor;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="DD/mm/yyyy,HH:00", timezone="BRT")
    private Date criadaEm;

    public Mensagem(){
        this.criadaEm = new Date();
    }
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}
	
	public Date getCriadaEm() {
		return criadaEm;
	}

	public void setCriadaEm(Date criadaEm) {
		this.criadaEm = criadaEm;
	}

	@Override
	public String toString() {
		return "Mensagem [id=" + id + ", conteudo=" + conteudo + ", titulo="
				+ titulo + ", autor=" + autor + "]";
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
		Mensagem other = (Mensagem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
