package project.issue.tracker.database.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.mindrot.jbcrypt.BCrypt;

import project.issue.tracker.database.db.EntityManagerProvider;

@Entity
@Table(name = "user")
public class User {
	private static final String TYPE_NORMAL = "user";
	private static final String TYPE_ADMIN = "admin";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;

	@Column(unique = true)
	private String userName;

	private String email;

	private String password;

	private String passwordSalt;

	private String fullName;

	private String role;

	private int itemsPerPage;

	@OneToMany(mappedBy = "assignee", fetch = FetchType.LAZY, targetEntity = Task.class)
	private List<Task> assignedTasks;

	@OneToMany(mappedBy = "author", fetch = FetchType.LAZY, targetEntity = Task.class)
	private List<Task> createdTasks;

	@OneToMany(mappedBy = "author", fetch = FetchType.LAZY, targetEntity = Comment.class)
	private List<Comment> comments;

	public User() {
		super();
		assignedTasks = new ArrayList<Task>();
		createdTasks = new ArrayList<Task>();
		comments = new ArrayList<Comment>();
	}

	public Long getId() {
		return userId;
	}

	public void setId(Long id) {
		this.userId = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.passwordSalt = BCrypt.gensalt();
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String type) {
		this.role = type;
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public List<Task> getAssignedTasks() {
		return assignedTasks;
	}

	public void setAssignedTasks(List<Task> assignedTasks) {
		this.assignedTasks = assignedTasks;
	}

	public List<Task> getCreatedTasks() {
		return createdTasks;
	}

	public void setCreatedTasks(List<Task> createdTasks) {
		this.createdTasks = createdTasks;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

}
