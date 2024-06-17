package at.jku.dke.task_app.uml.evaluation.atg.objects;

import java.util.List;

public class UMLResult {
    private List<UMLClass> umlClasses;
    private List<UMLRelationship> relationships;
    private List<UMLAssociation> associations;
    private List<UMLConstraints> constraints;

    private List<UMLClass> remainingUmlClasses;
    private List<UMLRelationship> remainingRelationships;
    private List<UMLAssociation> remainingAssociations;
    private List<UMLConstraints> remainingConstraints;


    public UMLResult(List<UMLClass> umlClasses, List<UMLRelationship> relationships, List<UMLAssociation> associations, List<UMLConstraints> constraints) {
        this.umlClasses = umlClasses;
        this.relationships = relationships;
        this.associations = associations;
        this.constraints = constraints;
        this.remainingUmlClasses = umlClasses;
        this.remainingRelationships = relationships;
        this.remainingAssociations = associations;
        this.remainingConstraints = constraints;
    }

    public List<UMLClass> getRemainingUmlClasses() {
        return remainingUmlClasses;
    }

    public void setRemainingUmlClasses(List<UMLClass> remainingUmlClasses) {
        this.remainingUmlClasses = remainingUmlClasses;
    }

    public List<UMLRelationship> getRemainingRelationships() {
        return remainingRelationships;
    }

    public void setRemainingRelationships(List<UMLRelationship> remainingRelationships) {
        this.remainingRelationships = remainingRelationships;
    }

    public List<UMLAssociation> getRemainingAssociations() {
        return remainingAssociations;
    }

    public void setRemainingAssociations(List<UMLAssociation> remainingAssociations) {
        this.remainingAssociations = remainingAssociations;
    }

    public List<UMLConstraints> getRemainingConstraints() {
        return remainingConstraints;
    }

    public void setRemainingConstraints(List<UMLConstraints> remainingConstraints) {
        this.remainingConstraints = remainingConstraints;
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
