package eu.mondo.map.modelmetrics.scalar.typed;

import eu.mondo.map.core.graph.Network;
import eu.mondo.map.core.graph.Node;
import eu.mondo.map.core.metrics.typed.TypedScalarMetric;

public class EdgeDimensionConnectivity extends TypedScalarMetric<String, Double> {

	public EdgeDimensionConnectivity() {
		super("EdgeDimensionConnectivity");
	}

	public void calculate(final String dimension, final Network<?> network) {
		int sumOfEdges = 0;
		for (Node<?> node : network.getNodes(dimension)) {
			sumOfEdges += node.getNumberOfNeighbors(dimension);
		}
		sumOfEdges /= 2;
		typedValues.put(dimension, (double) sumOfEdges / network.getNumberOfEdges());
	}

	public void calculate(final Network<?> network) {
		clear();
		for (String dimension : network.getDimensions()) {
			calculate(dimension, network);
		}
	}

	public void calculateExclusive(final String dimension, final Network<?> network) {
		throw new UnsupportedOperationException("calculateExclusive is not implemented");
//		int sumOfEdges = 0;
//		Set<String> otherDimensions;
//		for (Node<?> node : network.getNodes(dimension)) {
//			for (Node<?> neighbor : node.getNeighbors(dimension)) {
//				otherDimensions = node.getDimensionsAsSet();
//				otherDimensions.remove(dimension);
//				for (String dim : otherDimensions) {
//					if (node.getNeighbors(dimension).contains(neighbor)) {
//						continue;
//					}
//					sumOfEdges++;
//				}
//			}
//		}
//		sumOfEdges /= 2;
//		typedValues.put(dimension, (double) sumOfEdges / network.getNumberOfEdges());
	}

	public void calculateExclusive(final Network<?> network) {
		clear();
		for (String dimension : network.getDimensions()) {
			calculateExclusive(dimension, network);
		}
	}
}
