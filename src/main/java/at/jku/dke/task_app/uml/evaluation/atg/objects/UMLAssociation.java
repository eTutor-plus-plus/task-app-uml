package at.jku.dke.task_app.uml.evaluation.atg.objects;


public class UMLAssociation {
    private String assoClass;
    private String class1;
    private String class2;
    private int points;
    //cons


    public UMLAssociation(int points, String class2, String class1, String assoClass) {
        this.points = points;
        this.class2 = class2;
        this.class1 = class1;
        this.assoClass = assoClass;
    }

    public UMLAssociation(String class1, String class2,String assoClass) {
        this.assoClass = assoClass;
        this.class1 = class1;
        this.class2 = class2;
    }

    public UMLAssociation() {
    }
    //getters and setters
    public String getAssoClass() {
        return assoClass;
    }

    public void setAssoClass(String assoClass) {
        this.assoClass = assoClass;
    }

    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }

    public String getClass2() {
        return class2;
    }

    public void setClass2(String class2) {
        this.class2 = class2;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
