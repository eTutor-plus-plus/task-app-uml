grammar PlantUML_ATG;

@header {
import java.util.*;
import at.jku.dke.etutor.modules.uml.atg.objects.*;
}

@members {
    List<UMLClass> umlClasses = new ArrayList<>();
    Map<String, List<UMLAttribute>> attributesMap = new HashMap<>();
    List<UMLRelationship> relationships = new ArrayList<>();
    Map<String, UMLClass> classMap = new HashMap<>();
    String currentClassName; // To track current class in context for attributes
}

// Parser Rules
classDiagram: '@startuml' (classDefinition | relationship | multiRelationship | constraints | note | noteConnection | association)* '@enduml'
                {
                    System.out.println("All Classes: " + umlClasses);
                    System.out.println("Attributes Map: " + attributesMap);
                    System.out.println("Relationships: " + relationships);
                };

association: '(' className[0] ',' className[1] ')' '..' className[2]
                {
                    UMLAssociation assoc = new UMLAssociation(classMap.get($className[0].text), classMap.get($className[1].text), classMap.get($className[2].text));
                    relationships.add(assoc);
                };

noteConnection: noteName '..' multiRelationshipName;

constraints: '(' className[0] ',' className[1] ')' '..' '(' className[2] ',' className[3] ')' ':' '{' constrainttype '}';

constrainttype: ('disjoint'|'overlapping'|('Teilmenge' labelMultiplicity)|('Ungleich' labelMultiplicity?));

note: 'note' '"' noteText '"' 'as' noteName;

noteName: Identifier;
noteText: Identifier;

classDefinition: visibility? abstractModifier? 'class' className[true] ('extends' parentClassName)?
                {
                    UMLClass clazz = new UMLClass($className.text);
                    clazz.setAbstract($abstractModifier != null && $abstractModifier.text != null);
                    if ($parentClassName != null && $parentClassName.text != null) {
                        clazz.setParentClass(classMap.get($parentClassName.text));
                    }
                    umlClasses.add(clazz);
                    classMap.put($className.text, clazz);
                    attributesMap.put($className.text, new ArrayList<>());
                }
                '{' (attribute*) '}';

multiRelationship: 'diamond' multiRelationshipName;

attribute: attributeName attributeModifier?
                {
                    UMLAttribute attr = new UMLAttribute($attributeName.text);
                    attr.setModifier($attributeModifier != null ? $attributeModifier.text : null);
                    attributesMap.get(currentClassName).add(attr);
                };

attributeModifier: '{ID}';

relationship: participant[0] relationTyp participant[1] (':' label)? labelMultiplicity?
                {
                    UMLRelationship relationship = new UMLRelationship(classMap.get($participant[0].text), classMap.get($participant[1].text), $relationTyp.text);
                    relationships.add(relationship);
                };

relationTyp: ('*--'|'--' | '<--' | '---|>' | '<|--');

participant: participantMultiplicity? className[false] participantMultiplicity?
                {
                    currentClassName = $className.text;
                };

participantMultiplicity: '"'('*'|cardinality|(cardinality'..'cardinality))'"';

// Lexer Rules
visibility: ('+' | '-' | '#' | '~');
abstractModifier: 'abstract';
className[boolean create]: Identifier
                    {
                        if ($create) {
                            currentClassName = $Identifier.text;
                        }
                    };
parentClassName: Identifier;
attributeName: Identifier;
label: Identifier+;
multiRelationshipName: Identifier;
labelMultiplicity: ('*' | '?' | '+'|'>'|'<')?;
cardinality: Integer;
// Skip white spaces and newlines
WS: [ \t\r\n]+ -> skip;
Identifier: [a-zA-Z_][a-zA-Z0-9_]*;
Integer : [0-9]+;
// Parser entry point
start: classDiagram;
