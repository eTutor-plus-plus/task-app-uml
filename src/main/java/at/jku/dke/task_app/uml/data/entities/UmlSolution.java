package at.jku.dke.task_app.uml.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "solution")
public class UmlSolution {
    @Id
    @NotNull
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "solution", nullable = false, length = Integer.MAX_VALUE)
    private String solution;

    @NotNull
    @Column(name = "task_id", nullable = false)
    private Long taskId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

}
