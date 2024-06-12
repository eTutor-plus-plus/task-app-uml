package at.jku.dke.task_app.uml.data.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "solutionblockalternative")
public class UmlBlockAlt {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "solution_block_alternative", nullable = false, length = Integer.MAX_VALUE)
    private String solutionBlockAlternative;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "solution_block_id", nullable = false)
    private UmlBlock umlBlockAlt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setSolutionBlockAlternative(String solutionBlockAlternative) {
        this.solutionBlockAlternative = solutionBlockAlternative;
    }

    public UmlBlock getUmlBlockAlt() {
        return umlBlockAlt;
    }

    public void setUmlBlockAlt(UmlBlock umlBlockAlt) {
        this.umlBlockAlt = umlBlockAlt;
    }

    public UmlBlockAlt(String solutionBlockAlternative) {
        this.solutionBlockAlternative = solutionBlockAlternative;
    }



    public UmlBlockAlt() {

    }

}
