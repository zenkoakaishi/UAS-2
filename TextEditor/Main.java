// Main.java

class Action {
    String type; // "INSERT", "DELETE", "REPLACE"
    String text;
    int position;
    String previousText; // untuk undo
    Action next;
    Action prev;

    public Action(String type, String text, int position, String previousText) {
        this.type = type;
        this.text = text;
        this.position = position;
        this.previousText = previousText;
    }

    @Override
    public String toString() {
        return type + " \"" + text + "\" at " + position;
    }
}

class TextEditor {
    private StringBuilder content;
    private Action head;
    private Action currentAction;

    public TextEditor() {
        content = new StringBuilder();
        head = null;
        currentAction = null;
    }

    public void insertText(String text, int position) {
        if (position < 0 || position > content.length()) return;

        content.insert(position, text);
        Action action = new Action("INSERT", text, position, "");
        addAction(action);
    }

    public void deleteText(int start, int length) {
        if (start < 0 || start + length > content.length()) return;

        String deleted = content.substring(start, start + length);
        content.delete(start, start + length);
        Action action = new Action("DELETE", deleted, start, deleted);
        addAction(action);
    }

    public void replaceText(int start, int length, String newText) {
        if (start < 0 || start + length > content.length()) return;

        String oldText = content.substring(start, start + length);
        content.replace(start, start + length, newText);
        Action action = new Action("REPLACE", newText, start, oldText);
        addAction(action);
    }

    private void addAction(Action action) {
        if (currentAction != null && currentAction.next != null) {
            currentAction.next.prev = null;
            currentAction.next = null;
        }

        if (head == null) {
            head = action;
            currentAction = action;
        } else {
            currentAction.next = action;
            action.prev = currentAction;
            currentAction = action;
        }
    }

    public void undo() {
        if (currentAction == null) return;

        Action a = currentAction;

        switch (a.type) {
            case "INSERT":
                content.delete(a.position, a.position + a.text.length());
                break;
            case "DELETE":
                content.insert(a.position, a.previousText);
                break;
            case "REPLACE":
                content.replace(a.position, a.position + a.text.length(), a.previousText);
                break;
        }

        currentAction = currentAction.prev;
    }

    public void redo() {
        if (currentAction == null) {
            if (head != null) {
                currentAction = head;
            } else return;
        } else if (currentAction.next == null) {
            return;
        } else {
            currentAction = currentAction.next;
        }

        Action a = currentAction;

        switch (a.type) {
            case "INSERT":
                content.insert(a.position, a.text);
                break;
            case "DELETE":
                content.delete(a.position, a.position + a.text.length());
                break;
            case "REPLACE":
                content.replace(a.position, a.position + a.previousText.length(), a.text);
                break;
        }
    }

    public String getCurrentText() {
        return content.toString();
    }

    public void getActionHistory() {
        System.out.println("=== Action History ===");
        Action temp = head;
        int i = 1;
        while (temp != null) {
            System.out.println(i + ". " + temp.toString());
            temp = temp.next;
            i++;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();

        editor.insertText("Hello World", 0);
        editor.insertText("Beautiful ", 6);
        System.out.println(editor.getCurrentText()); // Hello Beautiful World

        editor.undo();
        System.out.println(editor.getCurrentText()); // Hello World

        editor.redo();
        System.out.println(editor.getCurrentText()); // Hello Beautiful World

        editor.replaceText(6, 9, "Amazing");
        System.out.println(editor.getCurrentText()); // Hello Amazing World

        editor.deleteText(6, 8);
        System.out.println(editor.getCurrentText()); // Hello World

        editor.undo(); // Undo delete
        System.out.println(editor.getCurrentText()); // Hello Amazing World

        editor.getActionHistory();
    }
}
