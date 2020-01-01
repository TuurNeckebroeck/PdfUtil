package org.tuurneckebroeck.pdfutil.task.lib;

import java.util.LinkedList;
import java.util.List;

@Deprecated
public class TaskChain extends Task {

    public TaskChain(TaskCallbackHandler parent) {
        super(parent);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not implemented yet.");
        // TODO implementeren

        // DESIGN oplossing vinden voor het combineren van de callbacks van de verschillende tasks in de chain
        // Er moet ook op een of andere manier een resultaat kunnen gekoppeld worden aan een Task, zodat een lijst van deze resultaten teruggegeven kan worden
        // best ook met een custom message, bv exceptionmessage (al vermeld in een andere designtodo)
    }

    private List<Task> tasks = new LinkedList<Task>();
}
