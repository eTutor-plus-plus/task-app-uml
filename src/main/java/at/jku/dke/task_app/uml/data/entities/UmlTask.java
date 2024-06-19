package at.jku.dke.task_app.uml.data.entities;

import at.jku.dke.etutor.task_app.data.entities.BaseTask;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
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

    @NotNull
    @Column(name = "classpoints", nullable = false, precision = 7, scale = 2)
    private BigDecimal classPoints;

    @NotNull
    @Column(name = "attributepoints", nullable = false, precision = 7, scale = 2)
    private BigDecimal attributePoints;

    @NotNull
    @Column(name = "relationshippoints", nullable = false, precision = 7, scale = 2)
    private BigDecimal relationshipPoints;

    @NotNull
    @Column(name = "constraintpoints", nullable = false, precision = 7, scale = 2)
    private BigDecimal constraintPoints;

    @NotNull
    @Column(name = "associationpoints", nullable = false, precision = 7, scale = 2)
    private BigDecimal associationPoints;

    public @NotNull BigDecimal getClassPoints() {
        return classPoints;
    }

    public void setClassPoints(@NotNull BigDecimal classPoints) {
        this.classPoints = classPoints;
    }

    public @NotNull BigDecimal getAttributePoints() {
        return attributePoints;
    }

    public void setAttributePoints(@NotNull BigDecimal attributePoints) {
        this.attributePoints = attributePoints;
    }

    public @NotNull BigDecimal getRelationshipPoints() {
        return relationshipPoints;
    }

    public void setRelationshipPoints(@NotNull BigDecimal relationshipPoints) {
        this.relationshipPoints = relationshipPoints;
    }

    public @NotNull BigDecimal getConstraintPoints() {
        return constraintPoints;
    }

    public void setConstraintPoints(@NotNull BigDecimal constraintPoints) {
        this.constraintPoints = constraintPoints;
    }

    public @NotNull BigDecimal getAssociationPoints() {
        return associationPoints;
    }

    public void setAssociationPoints(@NotNull BigDecimal associationPoints) {
        this.associationPoints = associationPoints;
    }

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
