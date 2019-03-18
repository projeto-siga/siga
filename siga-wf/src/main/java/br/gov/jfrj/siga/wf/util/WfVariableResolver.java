package br.gov.jfrj.siga.wf.util;

import org.jbpm.JbpmConfiguration.Configs;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;
import org.jbpm.jpdl.el.ELException;
import org.jbpm.jpdl.el.VariableResolver;
import org.jbpm.taskmgmt.exe.SwimlaneInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jbpm.taskmgmt.exe.TaskMgmtInstance;

public class WfVariableResolver implements VariableResolver {
	
  private ExecutionContext executionContext;

  public WfVariableResolver(ExecutionContext executionContext) {
	  this.executionContext = executionContext;
  }

  public Object resolveVariable(String name) throws ELException {
    ExecutionContext executionContext = this.executionContext;

    if ("taskInstance".equals(name)) return executionContext.getTaskInstance();
    if ("processInstance".equals(name)) return executionContext.getProcessInstance();
    if ("processDefinition".equals(name)) return executionContext.getProcessDefinition();
    if ("token".equals(name)) return executionContext.getToken();
    if ("taskMgmtInstance".equals(name)) return executionContext.getTaskMgmtInstance();
    if ("contextInstance".equals(name)) return executionContext.getContextInstance();

//    TaskInstance taskInstance = executionContext.getTaskInstance();
//    if (taskInstance != null && taskInstance.hasVariableLocally(name)) {
//      return taskInstance.getVariable(name);
//    }

    ContextInstance contextInstance = executionContext.getContextInstance();
    if (contextInstance != null) {
      Token token = executionContext.getToken();
      if (contextInstance.hasVariable(name, token)) {
        return contextInstance.getVariable(name);
      }
      if (contextInstance.hasTransientVariable(name)) {
        return contextInstance.getTransientVariable(name);
      }
    }

    TaskMgmtInstance taskMgmtInstance = executionContext.getTaskMgmtInstance();
    if (taskMgmtInstance != null) {
      SwimlaneInstance swimlaneInstance = taskMgmtInstance.getSwimlaneInstance(name);
      if (swimlaneInstance != null) return swimlaneInstance.getActorId();
    }

    return Configs.hasObject(name) ? Configs.getObject(name) : null;
  }
}