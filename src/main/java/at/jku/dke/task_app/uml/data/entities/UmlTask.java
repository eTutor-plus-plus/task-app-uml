package at.jku.dke.task_app.uml.data.entities;

import at.jku.dke.etutor.task_app.data.entities.BaseTask;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "umltask")
public class UmlTask extends BaseTask {



    @NotNull
    @Column(name = "complete_comparison", nullable = false)
    private Boolean completeComparison = false;

    @Column(name = "identifiers")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> identifiers;

    public List<String> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<String> identifiers) {
        this.identifiers = identifiers;
    }

    public Boolean getCompleteComparison() {
        return completeComparison;
    }

    public void setCompleteComparison(Boolean completeComparison) {
        this.completeComparison = completeComparison;
    }


/*
 TODO [Reverse Engineering] create field to map the 'status' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "status", columnDefinition = "task_status not null")
    private Object status;
*/
}
