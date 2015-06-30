package project.issue.tracker.database.db;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import project.issue.tracker.database.models.Project;
import project.issue.tracker.database.models.Task;
import project.issue.tracker.database.models.User;

public class QuerySelector {
	private static QuerySelector instance = null;
	private static final String persistanceUnitName = "project.issue.tracker.ui";
	private EntityManagerFactory factory;
	private  EntityManager entityManager;
	private CriteriaBuilder criteriaBuilder;
	
	public static QuerySelector getInstance() {
		if (instance == null) {
			return new QuerySelector();
		}
		else return instance;
	}
	
	public void persistObjects(List<Object> list) {
		entityManager.getTransaction().begin();
		for (int i = 0 ; i < list.size(); i++) {
			entityManager.persist(list.get(i));
		}
		entityManager.getTransaction().commit();
	}
	
	public void persistObject(Object object) {
		entityManager.getTransaction().begin();
		entityManager.persist(object);
		entityManager.getTransaction().commit();
	}
	
	private QuerySelector() {
		factory = Persistence.createEntityManagerFactory(persistanceUnitName);
		entityManager = factory.createEntityManager();
		criteriaBuilder = entityManager.getCriteriaBuilder();
	}
	
	public List<User> getAllUsers() {
		CriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
		Root<User> markRoot = criteria.from(User.class);
		criteria.select(markRoot);
		return entityManager.createQuery(criteria).getResultList();
	}
	
	public List<Task> getAllTasks() {
		CriteriaQuery<Task> criteria = criteriaBuilder.createQuery(Task.class);
		Root<Task> markRoot = criteria.from(Task.class);
		criteria.select(markRoot);
		return entityManager.createQuery(criteria).getResultList();
	}

	public List<Project> getAllProjects() {
		CriteriaQuery<Project> criteria = criteriaBuilder.createQuery(Project.class);
		Root<Project> markRoot = criteria.from(Project.class);
		criteria.select(markRoot);
		return entityManager.createQuery(criteria).getResultList();
	}
	
	public List<User> getUsersByName(String name) {
		CriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
		Root<User> markRoot = criteria.from(User.class);
		criteria.select(markRoot);
		Metamodel model = entityManager.getMetamodel();
		EntityType<User> Mark_ = model.entity(User.class);
		criteria.where(criteriaBuilder.equal(
				markRoot.get(Mark_.getDeclaredSingularAttribute("fullName")),name));
		return entityManager.createQuery(criteria).getResultList();
	}

	public List<User> getUsersByUName(String username) {
		CriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
		Root<User> markRoot = criteria.from(User.class);
		criteria.select(markRoot);
		Metamodel model = entityManager.getMetamodel();
		EntityType<User> Mark_ = model.entity(User.class);
		Predicate pr1 = criteriaBuilder.equal(
				markRoot.get(Mark_.getDeclaredSingularAttribute("userName")), username);
		criteria.where(pr1);
		return entityManager.createQuery(criteria).getResultList();
	}
	
	public List<Project> getProjectByName(String name) {
		CriteriaQuery<Project> criteria = criteriaBuilder.createQuery(Project.class);
		Root<Project> markRoot = criteria.from(Project.class);
		criteria.select(markRoot);
		Metamodel model = entityManager.getMetamodel();
		EntityType<Project> Mark_ = model.entity(Project.class);
		criteria.where(criteriaBuilder.equal(
				markRoot.get(Mark_.getDeclaredSingularAttribute("projectName")), name));
		return entityManager.createQuery(criteria).getResultList();
	}
	
	public List<Task> getTasksByName(String name) {
		CriteriaQuery<Task> criteria = criteriaBuilder.createQuery(Task.class);
		Root<Task> markRoot = criteria.from(Task.class);
		criteria.select(markRoot);
		Metamodel model = entityManager.getMetamodel();
		EntityType<Task> Mark_ = model.entity(Task.class);
		criteria.where(criteriaBuilder.equal(
				markRoot.get(Mark_.getDeclaredSingularAttribute("taskName")), name));
		return entityManager.createQuery(criteria).getResultList();
	}
	
	public Task getTaskById(String id) {
		return entityManager.find(Task.class, Integer.valueOf(id));
	}

	public Project getProjectById(String id) {
		return entityManager.find(Project.class, Long.valueOf(id));
	}
	
	public User getUserById(String id) {
		return entityManager.find(User.class, Long.valueOf(id));
	}

}
