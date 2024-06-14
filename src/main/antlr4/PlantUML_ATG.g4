grammar PlantUML_ATG;

@header {
import at.jku.dke.task_app.uml.evaluation.atg.objects.UMLAssociation;
import at.jku.dke.task_app.uml.evaluation.atg.objects.UMLClass;
import at.jku.dke.task_app.uml.evaluation.atg.objects.UMLAttribute;
import at.jku.dke.task_app.uml.evaluation.atg.objects.UMLRelationship;
import at.jku.dke.task_app.uml.evaluation.atg.objects.UMLRelationshipEntity;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

}

@members {
List<UMLClass> umlClasses = new ArrayList<>();
 Map<String, List<UMLAttribute>> attributesMap = new HashMap<>();
    List<UMLRelationship> relationships = new ArrayList<>();
    Map<String, UMLClass> classMap = new HashMap<>();
    String currentClassName; // To track current class in context for attributes
    List<UMLAssociation> associations = new ArrayList<>();
}


// Parser Rules
classDiagram: '@startuml' (classDefinition | relationship | multiRelationship | constraints | note | noteConnection | association)* '@enduml'
                {
                    System.out.println("All Classes: " + umlClasses);
                    System.out.println("Attributes Map: " + attributesMap);
                    System.out.println("Relationships: " + relationships);
                };

association: '(' className1=className ',' className2=className ')' '..' className3=className score?
                {
                    UMLAssociation association = new UMLAssociation();
                    association.setClass1($className1.text);
                    association.setClass2($className2.text);
                    association.setAssoClass($className3.text);
                    associations.add(association);
                };


noteConnection: noteName '..' multiRelationshipName;

constraints: '(' className1=className ',' className2=className ')' '..' '(' className3=className ',' className4=className ')' ':' '{' constrainttype '}' score? ;

constrainttype: ('disjoint'|'overlapping'|('Teilmenge' labelMultiplicity)|('Ungleich' labelMultiplicity?));

note: 'note' '"' noteText '"' 'as' noteName;

noteName: Identifier;
noteText: Identifier;

classDefinition: visibility? abstractModifier? 'class' className ('extends' parentClassName)? score?
                {
                    UMLClass clazz = new UMLClass($className.text);
                    clazz.setAbstract($abstractModifier.text != null);
                    if ($parentClassName.text != null && $parentClassName.text != null) {
                        clazz.setParentClass(classMap.get($parentClassName.text));
                    }
                    umlClasses.add(clazz);
                    classMap.put($className.text, clazz);
                    attributesMap.put($className.text, new ArrayList<>());
                }
                '{' ((attribute|specialAttribute)*) '}';

multiRelationship: 'diamond' multiRelationshipName;

specialAttribute: '{' speciallabel '}'  score? ;


attribute: attributeName attributeModifier? score?
                {
                    UMLAttribute attr = new UMLAttribute($attributeName.text);
                    if($attributeModifier.text != null){
                    attr.setType($attributeModifier.text);
                    }
                    attributesMap.get(currentClassName).add(attr);
                };

attributeModifier: '{ID}';
score: '[' points ']';
relationship: participant1=participant relationTyp participant2=participant (':' label)? labelMultiplicity? score?
                {
                                     UMLRelationshipEntity entity1 = new UMLRelationshipEntity();
                                     entity1.setClassname($participant1.text);
                                     entity1.setMultiplicity($participant1.multiplicity);

                                     UMLRelationshipEntity entity2 = new UMLRelationshipEntity();
                                     entity2.setClassname($participant2.text);
                                     entity2.setMultiplicity($participant2.multiplicity);

                                     List<UMLRelationshipEntity> entities = new ArrayList<>();
                                     entities.add(entity1);
                                     entities.add(entity2);

                                     UMLRelationship relation = new UMLRelationship();
                                     relation.setEntities(entities);
                                     relation.setName($label.text);
                                     relation.setType($relationTyp.text);

                                     relationships.add(relation);
                };



participant returns [String multiplicity]
    : participantMultiplicity1=participantMultiplicity? className participantMultiplicity2=participantMultiplicity?
        {
            currentClassName = $className.text;
            $multiplicity = $participantMultiplicity1.text + $participantMultiplicity2.text;
        };

participantMultiplicity returns [String multiplicity]
     : '"'('*'|cardinality|(cardinality'..'cardinality))'"'
         {
             $multiplicity = $text;
         };
// Lexer Rules
relationTyp: ('*--'|'--' | '<--' | '---|>' | '<|--');
visibility: ('+' | '-' | '#' | '~');
abstractModifier: 'abstract';
points: Integer;
className: Identifier
                    {
                        currentClassName = $Identifier.text;
                    };
parentClassName: Identifier;
speciallabel: AnythingButCurlyBraces+;
attributeName: Identifier;
label: Identifier+;
multiRelationshipName: Identifier;
labelMultiplicity returns [String multiplicity]
    : ('*' | '?' | '+'|'>'|'<')
        {
            $multiplicity = $text;
        };
cardinality: Integer;
// Skip white spaces and newlines
WS: [ \t\r\n]+ -> skip;
Identifier: [a-zA-Z_][a-zA-Z0-9_]*;
Integer : [0-9]+;
AnythingButCurlyBraces : ~'}';

// Parser entry point

start: classDiagram;
