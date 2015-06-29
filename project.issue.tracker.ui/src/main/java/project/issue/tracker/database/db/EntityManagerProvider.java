package project.issue.tracker.database.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerProvider {
	static EntityManagerFactory factory = Persistence.createEntityManagerFactory("project.issue.tracker.ui");
	static EntityManager entityManager = factory.createEntityManager();
	
	public static EntityManager getManager() {
		return entityManager;
	}

}
