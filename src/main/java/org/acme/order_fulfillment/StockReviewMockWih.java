package org.acme.order_fulfillment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class StockReviewMockWih implements WorkItemHandler {

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		manager.abortWorkItem(workItem.getId());

	}

	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		ProductOrder po = (ProductOrder) workItem.getParameter("productOrder");
		po.setRequestDate(new Date());

		if ("ERROR".equalsIgnoreCase(po.getProductName()))
			throw new RuntimeException("Error while stock review");

		if ("RANDOM".equalsIgnoreCase(po.getProductName())) {
			try {
				po.setProductName(getRandomProductName());
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		if ("IN_STOCK".equalsIgnoreCase(po.getProductName())) {
			po.setInStock(true);
		} else {
			po.setInStock(this.calculateInStock());
		}

		Map<String, Object> out = new HashMap<String, Object>();
		out.put("productOrder", po);

		manager.completeWorkItem(workItem.getId(), out);
	}

	private String getRandomProductName() throws IOException {
		final int LINES_IN_FILE = 144;
		Random rnd = new Random();
		int iR = rnd.nextInt(LINES_IN_FILE) + 1;

		return Files.readAllLines(Paths.get("products.csv")).get(iR);
	}

	private boolean calculateInStock() {
		Random rnd = new Random();
		int iR = rnd.nextInt(10) + 1;
		// 20% chances of in stock
		return iR < 8;
	}

}
