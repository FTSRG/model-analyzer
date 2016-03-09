package eu.mondo.map.modelmetrics.composite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import eu.mondo.map.core.graph.Network;
import eu.mondo.map.core.graph.Node;
import eu.mondo.map.core.metrics.ListMetric;

public class ShortestPathList extends ListMetric<Integer> {

	protected ListMultimap<Node<?>, Node<?>> visits;
	protected Map<Node<?>, Integer> depths;
	protected Map<Node<?>, Integer> results;

	public ShortestPathList() {
		super("ShortestPathList");
	}

	public void calculate(final Network<?> network) {
		clear();
		for (Node<?> sourceNode : network.getNodes()) {
			for (Node<?> targetNode : network.getNodes()) {
				if (sourceNode != targetNode) {
					addDepth(calculate(sourceNode, targetNode));
				}
			}
		}

	}

	public void calculate(final Network<?> network, final int numberOfRandomPairs) {
		clear();
		ListMultimap<Integer, Integer> pairs = determineRandomPairs(network, numberOfRandomPairs);

		Node<?> sourceNode;
		Node<?> targetNode;
		for (Integer sourceIndex : pairs.keySet()) {
			sourceNode = network.getNodes().get(sourceIndex);
			for (Integer targetIndex : pairs.get(sourceIndex)) {
				targetNode = network.getNodes().get(targetIndex);
				addDepth(calculate(sourceNode, targetNode));
			}
		}
	}

	protected ListMultimap<Integer, Integer> determineRandomPairs(final Network<?> network,
			final int numberOfRandomPairs) {
		int numberOfNodes = network.getNumberOfNodes();
		checkNumberOfRandomPairs(numberOfNodes, numberOfRandomPairs);
		ListMultimap<Integer, Integer> pairs = ArrayListMultimap.create();
		Random random = new Random();
		int iteration = 0;
		int firstIndex = 0;
		int secondIndex = 0;
		while (iteration < numberOfRandomPairs) {
			firstIndex = random.nextInt(numberOfNodes);
			secondIndex = random.nextInt(numberOfNodes);
			if (firstIndex == secondIndex) {
				continue;
			}
			if (!pairs.containsEntry(firstIndex, secondIndex)) {
				pairs.put(firstIndex, secondIndex);
				iteration++;
			}
		}
		return pairs;
	}

	protected void addDepth(final List<Path> paths) {
		if (paths.isEmpty()) {
			values.add(0);
		} else {
			values.add(paths.get(0).getDepth());
		}
	}

	protected void checkNumberOfRandomPairs(int numberOfNodes, int numberOfRandomPairs) {
		if (numberOfRandomPairs < 1) {
			throw new IllegalArgumentException("The numberOfRandomPairs parameter must be positive");
		}
		if (numberOfRandomPairs > (numberOfNodes * (numberOfNodes - 1))) {
			throw new IllegalArgumentException(
					"The numberOfRandomPairs parameter is invalid, cannot find as many number of shortest paths as "
							+ numberOfRandomPairs);
		}

	}

	public List<Path> calculate(final Node<?> sourceNode, final Node<?> targetNode) {
		if (sourceNode == null) {
			throw new IllegalArgumentException("The sourceNode is null!");
		}
		if (targetNode == null) {
			throw new IllegalArgumentException("The targetNode is null!");
		}
		init();

		Queue<Node<?>> queue = new LinkedList<Node<?>>();
		queue.add(sourceNode);
		visits.put(sourceNode, null);
		depths.put(sourceNode, 0);

		Node<?> currentNode;
		int depth = 0;
		while (!queue.isEmpty()) {
			currentNode = queue.remove();
			if (higherDepth(currentNode, depth)) {
				break;
			}
			for (Node<?> neighborNode : currentNode.getOutgoingNeighbors().keySet()) {
				if (neighborNode == targetNode) {
					results.put(currentNode, 0);
				}
				if (!visits.containsKey(neighborNode)) {
					visit(neighborNode, currentNode);
					depth = depths.get(neighborNode);
					queue.add(neighborNode);
				} else if (depths.get(currentNode) < depths.get(neighborNode)) {
					visits.put(neighborNode, currentNode);
				}
			}
		}
		List<Path> paths;
		if (!results.isEmpty()) {
			paths = resolveResults(targetNode);
		} else {
			paths = new ArrayList<Path>();
		}

		clearFields();
		return paths;
	}

	protected List<Path> resolveResults(final Node<?> targetNode) {
		int depth = depths.get(targetNode);
		List<Path> allPaths = new ArrayList<Path>();
		for (Node<?> node : results.keySet()) {
			Path path = new Path(depth);
			path.add(targetNode);
			path.add(node);
			allPaths.add(path);
			resolvePaths(allPaths, path, node);
		}
		return allPaths;
	}

	private void resolvePaths(List<Path> allPaths, Path path, Node<?> node) {
		if (reachedSourceNode(node)) {
			return;
		}
		List<Node<?>> neighbors = visits.get(node);
		for (int i = 1; i < neighbors.size(); i++) {
			Path newPath = new Path(path);
			newPath.add(neighbors.get(i));
			allPaths.add(newPath);
			resolvePaths(allPaths, newPath, neighbors.get(i));
		}
		path.add(neighbors.get(0));
		resolvePaths(allPaths, path, neighbors.get(0));
	}

	protected boolean reachedSourceNode(Node<?> node) {
		if (visits.get(node).get(0) == null) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param currentNode
	 * @param expectedDepth
	 * 
	 * @return true if the results Map is not empty and the currentNode's
	 *         depth is higher or equal than the expectedDepth, false
	 *         otherwise
	 */
	protected boolean higherDepth(final Node<?> currentNode, final int expectedDepth) {
		if (!results.isEmpty()) {
			return depths.get(currentNode) >= expectedDepth;
		}
		return false;

	}

	protected void init() {
		if (visits == null) {
			visits = ArrayListMultimap.create();
		}
		if (depths == null) {
			depths = new HashMap<Node<?>, Integer>();
		}
		if (results == null) {
			results = new HashMap<Node<?>, Integer>();
		}
		clearFields();
	}

	protected void clearFields() {
		visits.clear();
		depths.clear();
		results.clear();
	}

	protected void visit(final Node<?> child, final Node<?> parent) {
		visits.put(child, parent);
		int depth = depths.get(parent);
		depth++;
		depths.put(child, depth);
	}

	public static class Path<N>  {

		protected List<Node<N>> path;
		protected int depth;

		public Path() {
			path = new ArrayList<Node<N>>();
		}

		public Path(int depth) {
			path = new ArrayList<Node<N>>();
			this.depth = depth;
		}

		public Path(Path path2) {
			this.path = new ArrayList<Node<N>>();
			this.depth = path2.depth;
			this.path.addAll(path2.getPath());
		}

		public boolean isEmpty() {
			return path.isEmpty();
		}

		public List<Node<N>> getPath() {
			return path;
		}

		public int getDepth() {
			return depth;
		}

		public void setDepth(int depth) {
			this.depth = depth;
		}

		public int size() {
			return path.size();
		}

		public boolean add(Node<N> e) {
			return path.add(e);
		}

		public boolean addAll(Collection<? extends Node<N>> c) {
			return path.addAll(c);
		}

		@Override
		public String toString() {
			return "Path [path=" + path + ", depth=" + depth + "]";
		}

	}
}
