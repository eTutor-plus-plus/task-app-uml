package at.jku.dke.task_app.uml.evaluation.atg.objects;

import java.util.ArrayList;
import java.util.List;

public class UMLRelationship {
    private UMLRelationshipEntity entity1;
    private UMLRelationshipEntity entity2;
    private String name;
    private String direction; // inheritance, association, etc.
    private String type;
    private int points;

    public UMLRelationship(UMLRelationshipEntity entity1,UMLRelationshipEntity entity2, String name, String direction, int points, String type) {
        this.entity1 = entity1;
        this.entity2 = entity2;
        this.name = name;
        this.direction = direction;
        this.points = points;
        this.type = type;
    }

    public UMLRelationship(String name, String direction, int points) {
        this.name = name;
        this.direction = direction;
        this.points = points;
    }

    public UMLRelationship(UMLRelationshipEntity entity1,UMLRelationshipEntity entity2,String direction, String name, String type) {
        this.entity1 = entity1;
        this.entity2 = entity2;
        this.direction = direction;
        this.name = name;
        this.type = type;

    }
public UMLRelationship() {
    }
    public UMLRelationship(String name, String direction) {
        this.name = name;
        this.direction = direction;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UMLRelationshipEntity getEntity1() {
        return entity1;
    }

    public void setEntity1(UMLRelationshipEntity entity1) {
        this.entity1 = entity1;
    }

    public UMLRelationshipEntity getEntity2() {
        return entity2;
    }

    public void setEntity2(UMLRelationshipEntity entity2) {
        this.entity2 = entity2;
    }
}
