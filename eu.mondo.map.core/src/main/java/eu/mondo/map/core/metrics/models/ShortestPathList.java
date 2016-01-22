package eu.mondo.map.core.metrics.models;

import java.util.ArrayList;
import java.util.List;

public class ShortestPathList extends ListMetric<Integer> {

	private List<Integer> shortestPaths;

	private int maxDepth;

	private int pairs;

	public ShortestPathList() {
		shortestPaths = new ArrayList<Integer>();
		maxDepth = 50;
		pairs = 100;
	}

//	@Override
	public void calculate() {
//		if (shortestPaths.size() == 0) {
//			metricValue = 0.0;
//			return;
//		}
//		double sum = 0.0;
//		for (Integer distance : shortestPaths) {
//			sum += distance;
//		}
//		metricValue = sum / shortestPaths.size();

	}

	public boolean add(Integer e) {
		return shortestPaths.add(e);
	}

	public void clear() {
		shortestPaths.clear();
//		metricValue = 0;
	}

//	@Override
//	protected String getIdentifier() {
//		return "AvgShortestPath";
//	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	public int getPairs() {
		return pairs;
	}

	public void setPairs(int pairs) {
		this.pairs = pairs;
	}

}