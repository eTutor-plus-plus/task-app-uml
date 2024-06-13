package at.jku.dke.task_app.uml.evaluation.atg.objects;

public class UMLConstraints {
    private String rel1C1;
    private String rel1C2;
    private String rel2C1;
    private String rel2C2;
    private String type;

    public UMLConstraints(String rel1C1, String rel1C2, String rel2C1, String rel2C2, String type) {
        this.rel1C1 = rel1C1;
        this.rel1C2 = rel1C2;
        this.rel2C1 = rel2C1;
        this.rel2C2 = rel2C2;
        this.type = type;
    }

    public UMLConstraints() {
    }

    public String getRel1C1() {
        return rel1C1;
    }

    public void setRel1C1(String rel1C1) {
        this.rel1C1 = rel1C1;
    }

    public String getRel1C2() {
        return rel1C2;
    }

    public void setRel1C2(String rel1C2) {
        this.rel1C2 = rel1C2;
    }

    public String getRel2C1() {
        return rel2C1;
    }

    public void setRel2C1(String rel2C1) {
        this.rel2C1 = rel2C1;
    }

    public String getRel2C2() {
        return rel2C2;
    }

    public void setRel2C2(String rel2C2) {
        this.rel2C2 = rel2C2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
