package at.jku.dke.task_app.uml.evaluation.atg.objects;

import java.util.ArrayList;
import java.util.List;

public class UMLRelationship {
    private List<UMLRelationshipEntity> entities;
    private String name;
    private String direction; // inheritance, association, etc.
    private String type;
    private int points;

    public UMLRelationship(List<UMLRelationshipEntity> entities, String name, String direction, int points, String type) {
        this.entities = entities;
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

    public UMLRelationship(List<UMLRelationshipEntity> entities, String direction, String name, String type) {
        this.entities = entities;
        this.direction = direction;
        this.name = name;
        this.type = type;
    }

    public UMLRelationship(String name, String direction) {
        this.name = name;
        this.direction = direction;
    }

    public UMLRelationship() {
        entities = new ArrayList<>();
    }

    public List<UMLRelationshipEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<UMLRelationshipEntity> entities) {
        this.entities = entities;
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

    public void addEntity(UMLRelationshipEntity entity) {
        this.entities.add(entity);
    }

}
