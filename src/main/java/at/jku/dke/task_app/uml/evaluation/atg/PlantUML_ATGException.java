package at.jku.dke.task_app.uml.evaluation.atg;

public class PlantUML_ATGException extends RuntimeException{

    private final int line;
    private final int column;
    private final String ruleName;


    public PlantUML_ATGException(String message, Throwable cause, int line, int column, String ruleName) {
        super(message, cause);
        this.line = line;
        this.column = column;
        this.ruleName = ruleName;
    }

}
