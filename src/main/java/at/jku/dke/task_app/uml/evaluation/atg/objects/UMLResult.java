package at.jku.dke.task_app.uml.evaluation.atg.objects;

import java.util.List;

public class UMLResult {
    private List<UMLClass> umlClasses;
    private List<UMLRelationship> relationships;
    private List<UMLAssociation> associations;
    private List<UMLConstraints> constraints;

    private List<UMLMultiRelationship> multiRelationships;
    private List<UMLNote> notes;
    private List<UMLNoteConnection> noteConnections;


    public UMLResult(List<UMLClass> umlClasses, List<UMLRelationship> relationships, List<UMLAssociation> associations, List<UMLConstraints> constraints, List<UMLMultiRelationship> multiRelationships, List<UMLNote> notes, List<UMLNoteConnection> noteConnections) {
        this.umlClasses = umlClasses;
        this.relationships = relationships;
        this.associations = associations;
        this.constraints = constraints;
        this.multiRelationships = multiRelationships;
        this.notes = notes;
        this.noteConnections = noteConnections;
    }

    public UMLResult() {
    }

    public List<UMLClass> getUmlClasses() {
        return umlClasses;
    }

    public void setUmlClasses(List<UMLClass> umlClasses) {
        this.umlClasses = umlClasses;
    }

    public List<UMLRelationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<UMLRelationship> relationships) {
        this.relationships = relationships;
    }

    public List<UMLAssociation> getAssociations() {
        return associations;
    }

    public void setAssociations(List<UMLAssociation> associations) {
        this.associations = associations;
    }

    public List<UMLConstraints> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<UMLConstraints> constraints) {
        this.constraints = constraints;
    }

    public List<UMLMultiRelationship> getMultiRelationships() {
        return multiRelationships;
    }

    public void setMultiRelationships(List<UMLMultiRelationship> multiRelationships) {
        this.multiRelationships = multiRelationships;
    }

    public List<UMLNote> getNotes() {
        return notes;
    }

    public void setNotes(List<UMLNote> notes) {
        this.notes = notes;
    }

    public List<UMLNoteConnection> getNoteConnections() {
        return noteConnections;
    }

    public void setNoteConnections(List<UMLNoteConnection> noteConnections) {
        this.noteConnections = noteConnections;
    }
}
