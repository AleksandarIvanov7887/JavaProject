package project.issue.tracker.database.db;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class PersistanceManager {	
	public static void persistObject(List<Object> list) {
		EntityManager em = Persistence.createEntityManagerFactory("project.issue.tracker.ui").createEntityManager();
		em.getTransaction().begin();
		for (int i = 0 ; i < list.size(); i++) {
			em.persist(list.get(i));
		}
		em.getTransaction().commit();
	}
}
