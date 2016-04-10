package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.h2.tools.Csv;

import play.Logger;
import play.db.jpa.JPAApi;

import com.google.inject.Inject;

public class InitialData {

    @Inject
	public InitialData(JPAApi jpaAPI) {

		List<Cidade> cidades = jpaAPI.withTransaction(entityManager -> {
			return entityManager.createQuery("FROM Cidade").getResultList();
		});


		if (cidades.isEmpty()) {
            Logger.info("Populando BD");
			try {
				ResultSet resultSet = new Csv().read("public/data/dados2010.csv", null, "utf-8");
				resultSet.next(); // header
                int count = 0;
				while (resultSet.next()) {
					Cidade cidade = new Cidade(
							resultSet.getLong(2),
							resultSet.getString(3), 
							resultSet.getString(4),
							resultSet.getDouble(5), 
							resultSet.getDouble(6),
							resultSet.getDouble(7), 
							resultSet.getDouble(8), 
							resultSet.getLong(9));
					jpaAPI.withTransaction(() -> {
						jpaAPI.em().persist(cidade);
					});
                    count++;
                    if(count % 500 == 0){
                        Logger.info("Inseri " + count + " cidades...");
                    }
				}
			} catch (SQLException e1) {
                Logger.error(e1.getLocalizedMessage());
                e1.printStackTrace();
			}
		} else {
            Logger.info("BD já populado");
        }
	}
}