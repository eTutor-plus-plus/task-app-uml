package at.jku.dke.task_app.uml.evaluation;

import at.jku.dke.task_app.uml.evaluation.atg.gen.PlantUML_ATGBaseListener;
import at.jku.dke.task_app.uml.evaluation.atg.gen.PlantUML_ATGParser;
import at.jku.dke.task_app.uml.evaluation.atg.objects.UMLClass;
import at.jku.dke.task_app.uml.evaluation.atg.objects.UMLAttribute;
import at.jku.dke.task_app.uml.evaluation.atg.objects.UMLAssociation;

import java.util.ArrayList;
import java.util.List;

public class MyPlantUML_ATGListener extends PlantUML_ATGBaseListener {
    private List<UMLClass> umlClasses = new ArrayList<>();
    private UMLClass currentClass;
    private List<UMLAttribute> currentAttributes;
    private List<UMLAssociation> currentAssociations;

    @Override
    public void enterClassDefinition(PlantUML_ATGParser.ClassDefinitionContext ctx) {
        currentClass = new UMLClass();
        if (umlClasses.stream().anyMatch(c -> c.getName().equals(ctx.className.getText()))) {

          currentClass = umlClasses.stream().filter(c -> c.getName().equals(ctx.className.getText())).findFirst().get();
        }
        currentClass.setName(ctx.className().getText());

        currentAttributes = new ArrayList<>();
        currentAssociations = new ArrayList<>();
        currentClass.setAbstract(ctx.abstractModifier() != null);
    }


    @Override
    public void enterAttribute(PlantUML_ATGParser.AttributeContext ctx) {
        UMLAttribute attribute = new UMLAttribute();
        attribute.setName(ctx.attributeName().getText());
        currentAttributes.add(attribute);
    }

    @Override
    public void exitClassDefinition(PlantUML_ATGParser.ClassDefinitionContext ctx) {
        currentClass.setAttributes(currentAttributes);
        currentClass.setAssociations(currentAssociations);
        umlClasses.add(currentClass);
        currentClass = null;

    }

    public List<UMLClass> getUmlClasses() {
        return umlClasses;
    }

    @Override
    public void enterRelationship(PlantUML_ATGParser.RelationshipContext ctx) {
        String className = ctx.participant1.className.getText();
        String className2 = ctx.participant2.className.getText();
        System.out.println(className + " " + className2);
        if (className!=null&&!umlClasses.stream().anyMatch(c -> c.getName().equals(className))) {

            umlClasses.add(new UMLClass(className));
        }
        if (className2!=null&&!umlClasses.stream().anyMatch(c -> c.getName().equals(className2))) {
            umlClasses.add(new UMLClass(className2));
        }
    }

    @Override
    public void exitRelationship(PlantUML_ATGParser.RelationshipContext ctx) {
        if(currentClass!=null) {
            umlClasses.add(currentClass);
        }
        currentClass = null;
    }
}
