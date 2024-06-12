package at.jku.dke.task_app.uml.data.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "solutionblock")
public class UmlBlock {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private UmlTask task;

    @OneToMany(mappedBy = "umlBlockAlt", orphanRemoval = true)
    private List<UmlBlockAlt> umlBlockAlt = new ArrayList<>();

    public List<UmlBlockAlt> getUmlBlockAlt() {
        return umlBlockAlt;
    }

    @JsonProperty("umlBlock")
    public void setUmlBlockAlt(List<UmlBlockAlt> umlBlockAlt) {
        this.umlBlockAlt = umlBlockAlt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public UmlTask getTask() {
        return task;
    }

    public void setTask(UmlTask task) {
        this.task = task;
    }

    public void addUmlBlock(String umlBlockAlt) {
        UmlBlockAlt newUmlBlockAlt = new UmlBlockAlt(umlBlockAlt);
        newUmlBlockAlt.setUmlBlockAlt(this);
        this.umlBlockAlt.add(newUmlBlockAlt);
    }

    public List<UmlBlockAlt> getUmlBlockAlts() {
        return umlBlockAlt;
    }
    public UmlBlock() {
        System.out.println("UmlBlock created");
    }
    public UmlBlock (List<UmlBlockAlt> umlBlockAlt){
        this.umlBlockAlt = umlBlockAlt;
    }
}
