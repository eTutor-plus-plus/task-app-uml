package at.jku.dke.task_app.uml.evaluation.atg.objects;

public class UMLNoteConnection
{
    private String noteName;
    private String className;

    public UMLNoteConnection() {
    }

    public UMLNoteConnection(String noteName, String className) {
        this.noteName = noteName;
        this.className = className;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


}
