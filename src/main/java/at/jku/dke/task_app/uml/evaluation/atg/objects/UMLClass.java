package at.jku.dke.task_app.uml.evaluation.atg.objects;

import java.util.List;


public class UMLClass {
    private String name;
    private boolean isAbstract;
    private int points;
    private List<UMLAttribute> attributes;
    private UMLClass parentClass;
    private List<UMLAssociation> associations; // associations this class is part of

    // Add a constructor that takes certain parameters
    public UMLClass(String name, boolean isAbstract, List<UMLAttribute> attributes, UMLClass parentClass, int points, List<UMLAssociation> associations) {
        this.name = name;
        this.isAbstract = isAbstract;
        this.attributes = attributes;
        this.parentClass = parentClass;
        this.points = points;
        this.associations = associations;
    }

    public UMLClass(String name) {
        this.name = name;
    }

    public UMLClass() {

    }

    // Add a setAbstract method
    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public List<UMLAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<UMLAttribute> attributes) {
        this.attributes = attributes;
    }

    public UMLClass getParentClass() {
        return parentClass;
    }

    public void setParentClass(UMLClass parentClass) {
        this.parentClass = parentClass;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<UMLAssociation> getAssociations() {
        return associations;
    }

    public void setAssociations(List<UMLAssociation> associations) {
        this.associations = associations;
    }

    // Rest of your code...
}
