package at.jku.dke.task_app.uml.evaluation.atg;

import org.antlr.v4.runtime.BaseErrorListener;

public class PlantUML_ATGErrorListener extends BaseErrorListener {
    @Override
    public void syntaxError(org.antlr.v4.runtime.Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, org.antlr.v4.runtime.RecognitionException e) {
        throw new PlantUML_ATGException(msg, e, line, charPositionInLine, null);
    }
    public PlantUML_ATGErrorListener() {
    }


}
