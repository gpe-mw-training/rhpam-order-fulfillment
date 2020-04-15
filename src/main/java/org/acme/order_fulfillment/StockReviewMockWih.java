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

	private final static String[] products = {"Aloe Relief","Astro Pilot","Auto Pilot","Bear Edge","Bear Survival Edge","Bella",
		"Blue Steel Max Putter","Blue Steel Putter","BugShield Extreme","BugShield Lotion","BugShield Lotion Lite","BugShield Natural",
		"BugShield Spray","Calamine Relief","Canyon Mule Carryall","Canyon Mule Climber Backpack","Canyon Mule Cooler",
		"Canyon Mule Extreme Backpack","Canyon Mule Journey Backpack","Canyon Mule Weekender Backpack","Capri","Cat Eye",
		"Compact Relief Kit","Course Pro Gloves","Course Pro Golf and Tee Set","Course Pro Golf Bag","Course Pro Putter",
		"Course Pro Umbrella","Dante","Deluxe Family Relief Kit","Double Edge","Edge Extreme","EverGlow Butane","EverGlow Double",
		"EverGlow Kerosene","EverGlow Lamp","EverGlow Single","Fairway","Firefly 2","Firefly 4","Firefly Charger",
		"Firefly Climbing Lamp","Firefly Extreme","Firefly Lite","Firefly Mapreader","Firefly Multi-light","Firefly Rechargeable Battery",
		"Flicker Lantern","Glacier Basic","Glacier Deluxe","Glacier GPS","Glacier GPS Extreme","Granite Axe","Granite Belay",
		"Granite Carabiner","Granite Chalk Bag","Granite Climbing Helmet","Granite Extreme","Granite Grip","Granite Hammer",
		"Granite Ice","Granite Pulley","Granite Shovel","Granite Signal Mirror","Hailstorm Steel Irons","Hailstorm Steel Woods Set",
		"Hailstorm Titanium Irons","Hailstorm Titanium Woods Set","Hawk Eye","Hibernator","Hibernator Camp Cot","Hibernator Extreme",
		"Hibernator Lite","Hibernator Pad","Hibernator Pillow","Hibernator Self - Inflating Mat","Husky Harness","Husky Harness Extreme",
		"Husky Rope 100","Husky Rope 200","Husky Rope 50","Husky Rope 60","Inferno","Infinity","Insect Bite Relief","Kodiak",
		"Lady Hailstorm Steel Irons","Lady Hailstorm Steel Woods Set","Lady Hailstorm Titanium Irons","Lady Hailstorm Titanium Woods Set",
		"Legend","Lux","Max Gizmo","Maximus","Mountain Man Analog","Mountain Man Combination","Mountain Man Deluxe","Mountain Man Digital",
		"Mountain Man Extreme","Opera Vision","Pocket Gizmo","Polar Extreme","Polar Ice","Polar Sports","Polar Sun","Polar Wave",
		"Ranger Vision","Retro","Sam","Seeker 35","Seeker 50","Seeker Extreme","Seeker Mini","Single Edge","Sky Pilot","Star Dome",
		"Star Gazer 2","Star Gazer 3","Star Gazer 6","Star Lite","Star Peg","Sun Blocker","Sun Shelter 15","Sun Shelter 30",
		"Sun Shelter Stick","Sun Shield","Trail Master","Trail Scout","Trail Star","TrailChef Canteen","TrailChef Cook Set",
		"TrailChef Cup","TrailChef Deluxe Cook Set","TrailChef Double Flame","TrailChef Kettle","TrailChef Kitchen Kit",
		"TrailChef Single Flame","TrailChef Utensils","TrailChef Water Bag","Trendi","TX","Venue","Zodiak","Zone"}; 

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		manager.abortWorkItem(workItem.getId());

	}

	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		ProductOrder po = (ProductOrder) workItem.getParameter("productOrder");
		if (po == null)
			po = new ProductOrder();
		po.setProductName((String) workItem.getParameter("productName"));
		po.setRequestDate(new Date());

		if ("RANDOM".equalsIgnoreCase(po.getProductName())) {
			try {
				Random rnd = new Random();
				int iR = rnd.nextInt(10) + 1;
				// 20% Probability of error during stock review
				if (iR <= 8) {
					po.setProductName(getRandomProductName());
				} else {
					po.setProductName("ERROR");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}

		if ("ERROR".equalsIgnoreCase(po.getProductName()))
			throw new RuntimeException("Error while stock review");

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
		Random rnd = new Random();
		int iR = rnd.nextInt(products.length);

		return products[iR];
	}

	private boolean calculateInStock() {
		Random rnd = new Random();
		int iR = rnd.nextInt(10) + 1;
		// 60% chances of in stock
		return iR <= 6;
	}

}
