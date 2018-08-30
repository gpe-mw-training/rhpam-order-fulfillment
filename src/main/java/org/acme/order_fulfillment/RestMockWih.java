package org.acme.order_fulfillment;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class RestMockWih implements WorkItemHandler {

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		manager.abortWorkItem(workItem.getId());

	}

	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		String url = (String) workItem.getParameter("Url");
		System.out.println("REST Web service executed at endpoint: " + url);

		manager.completeWorkItem(workItem.getId(), null);
	}

}
