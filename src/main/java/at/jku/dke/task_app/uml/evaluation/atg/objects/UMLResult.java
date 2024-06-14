package at.jku.dke.task_app.uml.evaluation.atg.objects;

import java.util.List;

public class UMLResult {
    private List<UMLClass> umlClasses;
    private List<UMLRelationship> relationships;
    private List<UMLAssociation> associations;
    private List<UMLConstraints> constraints;


    public UMLResult(List<UMLClass> umlClasses, List<UMLRelationship> relationships, List<UMLAssociation> associations, List<UMLConstraints> constraints) {
        this.umlClasses = umlClasses;
        this.relationships = relationships;
        this.associations = associations;
        this.constraints = constraints;
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

    // getters and setters...
}
