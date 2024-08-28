package at.jku.dke.task_app.uml.evaluation.atg.objects;

public class UMLAttribute {
    private String name;
    private String type;
    private int points;


    public UMLAttribute(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public UMLAttribute(String name, String type, int points) {
        this.name = name;
        this.type = type;
        this.points = points;
    }

    public UMLAttribute(String name) {
        this.name = name;
    }

    public UMLAttribute() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
