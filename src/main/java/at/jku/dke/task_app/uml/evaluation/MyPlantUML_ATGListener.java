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
        currentClass.setName(ctx.className().getText());
        currentAttributes = new ArrayList<>();
        currentAssociations = new ArrayList<>();
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
}
