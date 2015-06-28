package project.issue.tracker.database.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long projectId;
	
	private String description;
	
	@Column(unique = true)
	private String projectName;
	
	@OneToMany(mappedBy = "project", fetch = FetchType.LAZY, targetEntity = Task.class)
	private List<Task> tasks;
	
	public Project() {
		super();
		tasks = new ArrayList<Task>();
	}
	
	public Project(Long id, String description, String projectName) {
		super();
		this.projectId = id;
		this.description = description;
		this.projectName = projectName;
	}

	public Long getId() {
		return projectId;
	}

	public void setId(Long id) {
		this.projectId = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	public void addTask(Task newTask) {
		this.tasks.add(newTask);
	}

}
