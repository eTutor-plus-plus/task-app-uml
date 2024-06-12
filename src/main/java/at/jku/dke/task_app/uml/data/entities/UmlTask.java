package at.jku.dke.task_app.uml.data.entities;

import at.jku.dke.etutor.task_app.data.entities.BaseTask;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task")
public class UmlTask extends BaseTask {
    @Id
    @NotNull
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

/*
 TODO [Reverse Engineering] create field to map the 'status' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "status", columnDefinition = "task_status not null")
    private Object status;
*/
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "task_id")
    private List<UmlSolution> umlSolutions = new ArrayList<>();

    public List<UmlSolution> getSolutions() {
        return umlSolutions;
    }

    public void setSolutions(List<UmlSolution> umlSolutions) {
        this.umlSolutions = umlSolutions;
    }
}
