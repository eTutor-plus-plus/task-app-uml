package at.jku.dke.task_app.uml.data.entities;

import at.jku.dke.etutor.task_app.data.entities.BaseSubmission;
import jakarta.persistence.*;

@Entity
@Table(name = "submission")
public class UmlSubmission extends BaseSubmission<UmlTask> {



    @Column(name = "submission", length = Integer.MAX_VALUE)
    private String submission;

    public String getSubmission() {
        return submission;
    }

    public void setSubmission(String submission) {
        this.submission = submission;
    }

/*
 TODO [Reverse Engineering] create field to map the 'mode' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "mode", columnDefinition = "submission_mode not null")
    private Object mode;
*/
}
