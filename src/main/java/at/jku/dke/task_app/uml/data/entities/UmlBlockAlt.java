package at.jku.dke.task_app.uml.data.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "umlblockalt")
public class UmlBlockAlt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "uml_block_alternative", nullable = false, length = Integer.MAX_VALUE)
    private String umlBlockAlternative;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uml_block_id", nullable = false)
    private UmlBlock umlBlock;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUmlBlockAlternative() {
        return umlBlockAlternative;
    }

    public void setUmlBlockAlternative(String umlBlockAlternative) {
        this.umlBlockAlternative = umlBlockAlternative;
    }

    public UmlBlock getUmlBlock() {
        return umlBlock;
    }

    public void setUmlBlock(UmlBlock umlBlock) {
        this.umlBlock = umlBlock;
    }

}
