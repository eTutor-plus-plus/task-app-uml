package at.jku.dke.task_app.uml.evaluation.atg.objects;

public class UMLNote {
    private String note;
    private String noteName;

    public UMLNote() {
    }

    public UMLNote(String note, String noteName) {
        this.note = note;
        this.noteName = noteName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }
}
