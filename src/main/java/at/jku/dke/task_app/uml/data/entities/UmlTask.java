package at.jku.dke.task_app.uml.data.entities;

import at.jku.dke.etutor.task_app.data.entities.BaseTask;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "umltask")
public class UmlTask extends BaseTask {

    @Column(name = "identifiers", length = Integer.MAX_VALUE)
    private String identifiers;

    @NotNull
    @Column(name = "complete_comparison", nullable = false)
    private Boolean completeComparison = false;

    public Boolean getCompleteComparison() {
        return completeComparison;
    }

    public void setCompleteComparison(Boolean completeComparison) {
        this.completeComparison = completeComparison;
    }

    public String getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(String identifiers) {
        this.identifiers = identifiers;
    }

/*
 TODO [Reverse Engineering] create field to map the 'status' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "status", columnDefinition = "task_status not null")
    private Object status;
*/
}
