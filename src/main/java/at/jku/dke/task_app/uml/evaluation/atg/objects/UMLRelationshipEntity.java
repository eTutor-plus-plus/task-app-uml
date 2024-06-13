package at.jku.dke.task_app.uml.evaluation.atg.objects;

public class UMLRelationshipEntity {
    private String classname;
    private String multiplicity;
    private String type;
    private int points;

    public UMLRelationshipEntity(String classname, String multiplicity, String type, int points) {
        this.classname = classname;
        this.multiplicity = multiplicity;
        this.type = type;
        this.points = points;
    }

    public UMLRelationshipEntity(String classname, String multiplicity, String type) {
        this.classname = classname;
        this.multiplicity = multiplicity;
        this.type = type;
    }

    public UMLRelationshipEntity() {
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(String multiplicity) {
        this.multiplicity = multiplicity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
