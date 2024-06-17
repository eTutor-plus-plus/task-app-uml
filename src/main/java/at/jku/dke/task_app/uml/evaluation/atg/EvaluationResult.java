package at.jku.dke.task_app.uml.evaluation.atg;

import at.jku.dke.task_app.uml.evaluation.atg.objects.*;

import java.util.ArrayList;
import java.util.List;

public class EvaluationResult {
    int points;
    String message;
    List<UMLClass> missingClasses;
    List<UMLAttribute> missingAttributes;
    List<UMLClass> missingAbstractClasses;
    List<UMLRelationship> missingRelationships;
    List<UMLAssociation> missingAssociations;
    List<UMLConstraints> missingConstraints;
    List<UMLClass> wrongClasses;
    List<UMLAttribute> wrongAttributes;
    List<UMLRelationship> wrongRelationships;
    List<UMLAssociation> wrongAssociations;
    List<UMLConstraints> wrongConstraints;

    public EvaluationResult() {
        points = 0;
        message = "";
        missingClasses = new ArrayList<>();
        missingAttributes = new ArrayList<>();
        missingAbstractClasses = new ArrayList<>();
        missingRelationships = new ArrayList<>();
        missingAssociations = new ArrayList<>();
        missingConstraints = new ArrayList<>();
        wrongClasses = new ArrayList<>();
        wrongAttributes = new ArrayList<>();
        wrongRelationships = new ArrayList<>();
        wrongAssociations = new ArrayList<>();
        wrongConstraints = new ArrayList<>();
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UMLClass> getMissingClasses() {
        return missingClasses;
    }

    public void setMissingClasses(List<UMLClass> missingClasses) {
        this.missingClasses = missingClasses;
    }

    public List<UMLAttribute> getMissingAttributes() {
        return missingAttributes;
    }

    public void setMissingAttributes(List<UMLAttribute> missingAttributes) {
        this.missingAttributes = missingAttributes;
    }

    public List<UMLClass> getMissingAbstractClasses() {
        return missingAbstractClasses;
    }

    public void setMissingAbstractClasses(List<UMLClass> missingAbstractClasses) {
        this.missingAbstractClasses = missingAbstractClasses;
    }

    public List<UMLRelationship> getMissingRelationships() {
        return missingRelationships;
    }

    public void setMissingRelationships(List<UMLRelationship> missingRelationships) {
        this.missingRelationships = missingRelationships;
    }

    public List<UMLAssociation> getMissingAssociations() {
        return missingAssociations;
    }

    public void setMissingAssociations(List<UMLAssociation> missingAssociations) {
        this.missingAssociations = missingAssociations;
    }

    public List<UMLConstraints> getMissingConstraints() {
        return missingConstraints;
    }

    public void setMissingConstraints(List<UMLConstraints> missingConstraints) {
        this.missingConstraints = missingConstraints;
    }

    public List<UMLClass> getWrongClasses() {
        return wrongClasses;
    }

    public void setWrongClasses(List<UMLClass> wrongClasses) {
        this.wrongClasses = wrongClasses;
    }

    public List<UMLAttribute> getWrongAttributes() {
        return wrongAttributes;
    }

    public void setWrongAttributes(List<UMLAttribute> wrongAttributes) {
        this.wrongAttributes = wrongAttributes;
    }

    public List<UMLRelationship> getWrongRelationships() {
        return wrongRelationships;
    }

    public void setWrongRelationships(List<UMLRelationship> wrongRelationships) {
        this.wrongRelationships = wrongRelationships;
    }

    public List<UMLAssociation> getWrongAssociations() {
        return wrongAssociations;
    }

    public void setWrongAssociations(List<UMLAssociation> wrongAssociations) {
        this.wrongAssociations = wrongAssociations;
    }

    public List<UMLConstraints> getWrongConstraints() {
        return wrongConstraints;
    }

    public void setWrongConstraints(List<UMLConstraints> wrongConstraints) {
        this.wrongConstraints = wrongConstraints;
    }
}
