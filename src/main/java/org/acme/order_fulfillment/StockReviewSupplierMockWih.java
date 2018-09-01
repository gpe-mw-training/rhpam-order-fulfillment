package org.acme.order_fulfillment;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class StockReviewSupplierMockWih implements WorkItemHandler {

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		manager.abortWorkItem(workItem.getId());

	}

	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		ProductOrder po = (ProductOrder) workItem.getParameter("productOrder");

		int deliverable = calculateDeliverable();
		po.setUndeliverable(deliverable == -1);
		po.setLateDelivery(deliverable > 2);
		po.setSupplierDeliveryDays(deliverable);

		Map<String, Object> out = new HashMap<String, Object>();
		out.put("productOrder", po);

		manager.completeWorkItem(workItem.getId(), out);
	}

	private int calculateDeliverable() {
		Random rnd = new Random();
		int iR = rnd.nextInt(10) + 1;
		// 60% chances of early delivery
		if (iR <= 6)
			return rnd.nextInt(2) + 1;
		// 20% chances of late delivery
		if (iR <= 8)
			return rnd.nextInt(10) + 3;
		// 20% (Remaining) chance of undeliverable
		return -1;
	}

}
