package eu.mondo.map.modelmetrics.scalar.typed;

import java.util.Map;

import eu.mondo.map.core.graph.Network;
import eu.mondo.map.core.graph.Node;
import eu.mondo.map.core.metrics.typed.TypedScalarMetric;
import eu.mondo.map.modelmetrics.scalar.NumberOfNodes;

/**
 * Takes values in [0,1] and computes the ratio of nodes of the network that
 * belong to a particular dimension.
 * 
 *
 */
public class NodeDimensionConnectivity extends TypedScalarMetric<String, Double> {

	public NodeDimensionConnectivity() {
		super("NodeDimensionConnectivity");
	}

	/**
	 * Calculates the Node Dimension Connectivity for a particular
	 * dimension.
	 * 
	 * @param dimension
	 *                String representing the dimension
	 * @param typedEdges
	 *                NumberOfTypedEdges object
	 * @param numberOfNodes
	 *                NumberOfNodes object
	 */
	public void calculate(final String dimension, final NumberOfTypedEdges typedEdges,
			final NumberOfNodes numberOfNodes) {
		if (!typedEdges.getValues().containsKey(dimension)) {
			throw new IllegalArgumentException("The dimension does not exist in the map:" + dimension);
		}
		putRatio(dimension, numberOfNodes.getValue(), typedEdges.getValues());
	}

	/**
	 * Calculates the Node Dimension Connectivity for every possible
	 * dimension that can be found in the given parameter.
	 * 
	 * @param typedEdges
	 *                NumberOfTypedEdges object
	 */
	public void calculate(final NumberOfTypedEdges typedEdges, final NumberOfNodes numberOfNodes) {
		clear();
		for (String key : typedEdges.getValues().keySet()) {
			putRatio(key, numberOfNodes.getValue(), typedEdges.getValues());
		}
	}

	public void calculate(final String dimension, final Network<?> network) {
		typedValues.put(dimension, (double) (network.getNumberOfNodes(dimension) / network.getNumberOfNodes()));
	}

	public void calculateExclusive(final String dimension, final Network<?> network) {
		if (!network.getNodesOnDimensions().containsKey(dimension)) {
			throw new IllegalArgumentException("Dimension does not exist: " + dimension);
		}
		int numOfNodes = 0;
		for (Node<?> node : network.getNodesOnDimensions().get(dimension)) {
			if (node.getDimensions().keySet().size() == 1) {
				numOfNodes++;
			}
		}
		typedValues.put(dimension, (double) numOfNodes / network.getNumberOfNodes());
	}

	public void calculate(final Network<?> network) {
		clear();
		for (String dimension : network.getDimensions()) {
			calculate(dimension, network);
		}
	}

	public void calculateExclusive(final Network<?> network) {
		clear();
		for (String dimension : network.getDimensions()) {
			calculateExclusive(dimension, network);
		}
	}

	protected void putRatio(final String dimension, final double allNodes, final Map<String, Integer> edges) {
		typedValues.put(dimension, edges.get(dimension) / allNodes);
	}

}
