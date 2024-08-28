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
    List<UMLMultiRelationship> missingMultiRelationships;
    List<UMLNote> missingNotes;
    List<UMLNoteConnection> missingNoteConnections;

    List<UMLClass> wrongClasses;
    List<UMLAttribute> wrongAttributes;
    List<UMLRelationship> wrongRelationships;
    List<UMLAssociation> wrongAssociations;
    List<UMLConstraints> wrongConstraints;
    List<UMLMultiRelationship> wrongMultiRelationships;
    List<UMLNote> wrongNotes;
    List<UMLNoteConnection> wrongNoteConnections;

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
        wrongNotes = new ArrayList<>();
        wrongNoteConnections = new ArrayList<>();
        wrongMultiRelationships = new ArrayList<>();
        missingMultiRelationships = new ArrayList<>();
        missingNotes = new ArrayList<>();
        missingNoteConnections = new ArrayList<>();
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

    public List<UMLMultiRelationship> getMissingMultiRelationships() {
        return missingMultiRelationships;
    }

    public void setMissingMultiRelationships(List<UMLMultiRelationship> missingMultiRelationships) {
        this.missingMultiRelationships = missingMultiRelationships;
    }

    public List<UMLNote> getMissingNotes() {
        return missingNotes;
    }

    public void setMissingNotes(List<UMLNote> missingNotes) {
        this.missingNotes = missingNotes;
    }

    public List<UMLNoteConnection> getMissingNoteConnections() {
        return missingNoteConnections;
    }

    public void setMissingNoteConnections(List<UMLNoteConnection> missingNoteConnections) {
        this.missingNoteConnections = missingNoteConnections;
    }

    public List<UMLMultiRelationship> getWrongMultiRelationships() {
        return wrongMultiRelationships;
    }

    public void setWrongMultiRelationships(List<UMLMultiRelationship> wrongMultiRelationships) {
        this.wrongMultiRelationships = wrongMultiRelationships;
    }

    public List<UMLNote> getWrongNotes() {
        return wrongNotes;
    }

    public void setWrongNotes(List<UMLNote> wrongNotes) {
        this.wrongNotes = wrongNotes;
    }

    public List<UMLNoteConnection> getWrongNoteConnections() {
        return wrongNoteConnections;
    }

    public void setWrongNoteConnections(List<UMLNoteConnection> wrongNoteConnections) {
        this.wrongNoteConnections = wrongNoteConnections;
    }
}
