package models;

import com.google.inject.Inject;
import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.JPAApi;

import javax.inject.Singleton;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

@Singleton
public class OpiniaoDAO {

    @PersistenceContext
    private JPAApi jpaAPI;

    @Inject
    public OpiniaoDAO(JPAApi jpaAPI) {
        this.jpaAPI = jpaAPI;
    }

    public Opiniao find(String idOpiniao) {
        UUID idOpiniaoUUID = UUID.fromString(idOpiniao);
        return jpaAPI.em().find(Opiniao.class, idOpiniaoUUID);
    }

    public void delete(Opiniao paraRemover) {
        jpaAPI.em().remove(paraRemover);
    }

    public List<Opiniao> findByIniciativa(Long idIniciativa, int pagina, int tamanhoPagina) {
        TypedQuery<Opiniao> query = jpaAPI.em().createQuery(
                "SELECT o FROM Opiniao o WHERE o.iniciativa.id = :id ORDER BY criadaEm desc", Opiniao.class)
                .setParameter("id", idIniciativa)
                .setFirstResult(pagina * tamanhoPagina)
                .setMaxResults(tamanhoPagina);
        return query.getResultList();
    }

    public List<Opiniao> findRecentes(UUID cidadaoId, int pagina, int tamanhoDaPagina) {
        TypedQuery<Opiniao> query = jpaAPI.em()
                .createQuery("SELECT n "
                        + "FROM Cidadao c "
                        + "JOIN c.iniciativasAcompanhadas as acompanhadas "
                        + "JOIN acompanhadas.opinioes as n "
                        + "WHERE c.id = :cidadao_id "
                        + "ORDER BY n.criadaEm DESC", Opiniao.class)
                .setParameter("cidadao_id", cidadaoId)
                .setFirstResult(pagina * tamanhoDaPagina)
                .setMaxResults(tamanhoDaPagina);
        return query.getResultList();
    }
}
