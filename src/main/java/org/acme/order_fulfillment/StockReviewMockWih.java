package org.acme.order_fulfillment;

import java.io.IOException;
import java.net.URISyntaxException;
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
		if (po == null)
			po = new ProductOrder();
		po.setProductName((String) workItem.getParameter("productName"));
		po.setRequestDate(new Date());

		if ("ERROR".equalsIgnoreCase(po.getProductName()))
			throw new RuntimeException("Error while stock review");

		if ("RANDOM".equalsIgnoreCase(po.getProductName())) {
			try {
				po.setProductName(getRandomProductName());
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (URISyntaxException e) {
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

	private String getRandomProductName() throws IOException, URISyntaxException {
		final int LINES_IN_FILE = 144;
		Random rnd = new Random();
		int iR = rnd.nextInt(LINES_IN_FILE) + 1;

		// return the iRth line of the "products.txt" in resource path
		return Files.readAllLines(Paths.get(ClassLoader.getSystemResource("products.txt").toURI())).get(iR);
	}

	private boolean calculateInStock() {
		Random rnd = new Random();
		int iR = rnd.nextInt(10) + 1;
		// 20% chances of in stock
		return iR < 8;
	}

}
