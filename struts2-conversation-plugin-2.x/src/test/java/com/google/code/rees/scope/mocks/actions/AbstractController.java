package com.google.code.rees.scope.mocks.actions;

public abstract class AbstractController {

    private String abstractField = "hola";

    private String workflowString = "oopy doopy";

    public void setAbstractField(String abstractField) {
        this.abstractField = abstractField;
    }

    public String getAbstractField() {
        return abstractField;
    }

    public void setWorkflowString(String workflowString) {
        this.workflowString = workflowString;
    }

    public String getWorkflowString() {
        return workflowString;
    }

}
