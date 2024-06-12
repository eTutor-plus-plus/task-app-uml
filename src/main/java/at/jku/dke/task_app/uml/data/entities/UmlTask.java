package at.jku.dke.task_app.uml.data.entities;

import at.jku.dke.etutor.task_app.data.entities.BaseTask;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task")
public class UmlTask extends BaseTask {

    @NotNull
    @Column(name = "identifiers", nullable = false)
    private String identifiers;

    public String getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(String identifiers) {
        this.identifiers = identifiers;
    }

    @OneToMany(mappedBy = "task", orphanRemoval = true)
    private List<UmlBlock> umlBlocks = new ArrayList<>();

    public List<UmlBlock> getSolutionblocks() {
        return umlBlocks;
    }

    public void setSolutionblocks(List<UmlBlock> umlBlocks) {
        this.umlBlocks = umlBlocks;
    }

    public UmlTask() {
    }
    /*
 TODO [Reverse Engineering] create field to map the 'status' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "status", columnDefinition = "task_status not null")
    private Object status;
*/
}
