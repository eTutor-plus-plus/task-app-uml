package at.jku.dke.task_app.uml.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UmlBlockAltDto {

    @JsonProperty("umlBlockAlt")
    private String solutionBlockAlternative;

    public String getSolutionBlockAlternative() {
        return solutionBlockAlternative;
    }

    public void setSolutionBlockAlternative(String solutionBlockAlternative) {
        this.solutionBlockAlternative = solutionBlockAlternative;
    }
    public UmlBlockAltDto() {
    }

    @JsonCreator
    public UmlBlockAltDto(String solutionBlockAlternative) {
        this.solutionBlockAlternative = solutionBlockAlternative;
    }
}
