package project.issue.tracker.database.models;

import java.util.Date;

import javax.persistence.*;

@Entity
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int commentId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	@JoinColumn(name = "author", nullable = true, referencedColumnName = "userId")
    private User author;
    
	@Temporal(TemporalType.DATE)
    private Date date;
    
    private String content;
    
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Task.class)
	@JoinColumn(name = "task", nullable = true, referencedColumnName = "taskId")
    private Task task;
    
    private String authorName;
    
    
    public Comment() {
    	super();
    }
    
	public Comment(int id, User author, Date date, String content, Task task) {
		super();
		this.commentId = id;
		this.author = author;
		this.date = date;
		this.content = content;
		this.task = task;
		this.authorName = author.getFullName();
	}

	public int getId() {
		return commentId;
	}

	public void setId(int id) {
		this.commentId = id;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	
    
}
