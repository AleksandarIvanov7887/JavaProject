package project.issue.tracker.database.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
public class Task {

	public static final String STATUS_OPEN = "1";
    public static final String STATUS_IN_PROGESS = "2";
    public static final String STATUS_COMPLETED = "3";
    
    public static final String IMPORTANT = "1";
    public static final String NOT_IMPORTANT = "0";
	
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int taskId;
    
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Project.class)
	@JoinColumn(name = "project", nullable = true, referencedColumnName = "projectId")
    private Project project;
    
    private String title;
    
    @Column(name = "important")
    private boolean important;
    
    private String description;
    
    @Temporal(TemporalType.DATE)
    private Date dueDate;
    
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	@JoinColumn(name = "createdBy", nullable = true, referencedColumnName = "userId")
    private User author;
    
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	@JoinColumn(name = "assignedTo", nullable = true, referencedColumnName = "userId")
    private User assignee;
    
    private String status;
    
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, targetEntity = Comment.class)
    private List<Comment> comments;
    
    public Task() {
    	super();
    	comments = new ArrayList<Comment>();
    }
    
	public Task(int id, Project project, String title, String description,
			Date dueDate, User authorId, User assigneeId, String status,
			List<Comment> comments) {
		super();
		this.taskId = id;
		this.project = project;
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
		this.author = authorId;
		this.assignee = assigneeId;
		this.status = status;
		this.comments = comments;
	}

	public int getId() {
		return taskId;
	}

	public void setId(int id) {
		this.taskId = id;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User authorId) {
		this.author = authorId;
	}

	public User getAssignee() {
		return assignee;
	}

	public void setAssignee(User assigneeId) {
		this.assignee = assigneeId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public void addComment(Comment comment) {
		this.comments.add(comment);
	}

	public boolean isImportant() {
		return important;
	}

	public void setImportant(boolean important) {
		this.important = important;
	}
	
	
	
}
