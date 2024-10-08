package at.jku.dke.task_app.uml.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UmlBlockDto {

    @JsonProperty("umlBlock")
    private List<UmlBlockAltDto> umlBlockAlt;

    public UmlBlockDto() {
    }

    public UmlBlockDto(List<UmlBlockAltDto> umlBlockAlt) {

        this.umlBlockAlt = umlBlockAlt;
    }

    public List<UmlBlockAltDto> getUmlBlockAlt() {
        return umlBlockAlt;
    }

    public void setUmlBlockAlt(List<UmlBlockAltDto> umlBlockAlt) {
        this.umlBlockAlt = umlBlockAlt;
    }

}
