package project.issue.tracker.database;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import project.issue.tracker.database.models.Project;
import project.issue.tracker.database.models.Task;
import project.issue.tracker.database.models.User;

public class TestConnection {

	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("project.issue.tracker.ui");
		EntityManager em = factory.createEntityManager();
//		
//		Project p = new Project();
//		p.setDescription("IssueTracker");
//		p.setProjectName("name");
//		
//		User u = new User();
//		u.setUserName("al");
//		u.setPassword("aA12345678");
//		u.setEmail("alexander120116@abv.bg");
//		u.setFullName("Aleksandar Ivanov");
//		u.setRole(User.TYPE_ADMIN);
		
		
//		
//		Task t = new Task();
//		t.setTitle("issue");
//		t.setAuthor(u);
//		
//		u.getCreatedTasks().add(t);
//		p.addTask(t);
//		
//		em.getTransaction().begin();
//		em.persist(p);
//		em.persist(u);
//		em.persist(t);
//		em.getTransaction().commit();

//		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//		CriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
//		Metamodel model = em.getMetamodel();
//		EntityType<User> Mark_ = model.entity(User.class);
//		Root<User> markRoot = criteria.from(User.class);
//		criteria.select(markRoot);
//		criteria.where(criteriaBuilder.equal(
//				markRoot.get(Mark_.getDeclaredSingularAttribute("username")),"sasheto"));
//		User sasheto = em.createQuery(criteria).getResultList().get(0);
		
//		final List<User> users = em.createQuery("SELECT u FROM User u").getResultList();
//		
//		System.out.println(users.get(0).getCreatedTasks().size());
		
		QuerySelector selector = QuerySelector.getInstance();
		
		try{
			selector.getAllProjects();
			selector.getAllTasks();
			selector.getAllUsers();
			selector.getProjectById("23");
			selector.getProjectByName("name");
			selector.getUsersByUName("username");
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		
		
		
	}

}
