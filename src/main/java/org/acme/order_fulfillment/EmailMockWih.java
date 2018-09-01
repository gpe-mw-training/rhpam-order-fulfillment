package org.acme.order_fulfillment;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class EmailMockWih implements WorkItemHandler {

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		manager.abortWorkItem(workItem.getId());

	}

	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		System.out.println("Email sent: " + workItem.getParameter("Body"));
		manager.completeWorkItem(workItem.getId(), null);
	}

}
