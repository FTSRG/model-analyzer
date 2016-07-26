package eu.mondo.map.modelanalyzer;

import static com.google.common.base.Preconditions.checkState;

import org.apache.log4j.Logger;

import com.google.common.base.Preconditions;

import eu.mondo.map.base.Analyzer;
import eu.mondo.map.base.metrics.Metric;
import eu.mondo.map.base.metrics.SummaryMetric;
import eu.mondo.map.modeladapters.ModelAdapter;
import eu.mondo.map.modelmetrics.ModelMetric;
import eu.mondo.map.modelmetrics.impl.ModelMetrics;

public class ModelAnalyzer<MetricT extends ModelMetric> extends Analyzer<String, MetricT> {

    protected final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ModelAnalyzer<MetricT> clear() {
	super.clear();
	return this;
    }

    /**
     * <p>
     * Adds the parameter metric (see {@link ModelMetrics}) to the metrics
     * collection which will be used in the analysis. Throws
     * IllegalStateException if the metric was already added to this class.
     * </p>
     * 
     * @param metric
     *            represents the metric
     * @throws IllegalStateException
     *             if metric was already added
     * @return this
     */
    public ModelAnalyzer<MetricT> use(ModelMetrics metric) {
	useMetric(metric, null);
	return this;
    }

    /**
     * <p>
     * Adds the parameter metric (see {@link ModelMetrics}) to the
     * <em>metrics</em> collection which will be used in the analysis. The
     * metric is identified by the given name parameter. Throws
     * IllegalStateException if the metric was already added to this class.
     * </p>
     * 
     * @param metric
     *            {@link ModelMetrics}
     * 
     * @param name
     *            String, represents the name of the metric
     * @throws IllegalStateException
     *             if metric was already added
     * 
     * @return this
     */
    public ModelAnalyzer<MetricT> use(ModelMetrics metric, String name) {
	useMetric(metric, name);
	return this;
    }

    // /**
    // * <p>
    // * Adds the parameter metric (see {@link Metric}) to the <em>metrics</em>
    // * collection which will be used in the analysis.
    // * </p>
    // *
    // * @param metric
    // * {@link Metric} object
    // * @return this
    // */
    // public ModelAnalyzer use(Metric metric) {
    // metrics.put(metric.getName(), metric);
    // return this;
    // }

    // /**
    // * <p>
    // * Adds the parameter metric (see {@link Metric}) to the <em>metrics</em>
    // * collection which will be used in the analysis. The metric is identified
    // * by the given name parameter.
    // * </p>
    // *
    // * @param metric
    // * {@link Metric} object
    // * @param name
    // * String, represents the name of the metric
    // *
    // * @return this
    // */
    // public ModelAnalyzer use(Metric metric, String name) {
    // metric.setName(name);
    // metrics.put(metric.getName(), metric);
    // return this;
    // }

    protected void useMetric(ModelMetrics metric, String name) {
	checkNewMetric(metric);
	addMetric(metric, (MetricT) metric.instantiate(), name);
    }

    protected void checkNewMetric(ModelMetrics metric) {
	checkState(!metrics.containsKey(metric.toString()),
		"The metric " + metric.toString() + " was already added to the analyzer.");
    }

    protected void addMetric(ModelMetrics metric, MetricT metricObj, String name) {
	if (name != null) {
	    metricObj.setName(name);
	}
	metrics.put(metric.toString(), metricObj);
	logger.info("Use metric for the analysis: " + metric.toString());
    }

    /**
     * <p>
     * Adds all of the metrics that are in {@link ModelMetrics}. It must be
     * guaranteed that other metric was not added before and the
     * <em>metrics</em> collection is empty.
     * </p>
     * 
     * @throws IllegalStateException
     *             if at least one metric was added before
     * 
     * @return
     */
    public ModelAnalyzer<MetricT> useAll() {
	checkEmptyMetrics();
	for (ModelMetrics m : ModelMetrics.values()) {
	    useMetric(m, null);
	}
	return this;
    }

    protected void checkEmptyMetrics() {
	checkState(metrics.isEmpty(),
		"The ModelAnalyzer already contains metrics, so the useAll function cannot be used.");
    }

    public ModelAnalyzer<MetricT> useSummary(SummaryMetric<?, ?> summary) {
	// checkArgument(metrics.containsKey(metric.toString()),
	// "The " + metric.toString() + " should be contained by the
	// ModelAnalyzer");
	// TODO
	return this;
    }

    /**
     * <p>
     * Removes the metric parameter (see {@link ModelMetrics}) from the
     * <em>metrics</em> collection. As a result, the metric won't be calculated
     * during the analysis.
     * </p>
     * 
     * @param metric
     *            {@link ModelMetrics} value
     * 
     * @throws IllegalStateException
     *             if the <em>metrics</em> collection is empty
     * @return this
     */
    public ModelAnalyzer<MetricT> omit(ModelMetrics metric) {
	checkState(!metrics.isEmpty(), "The ModelAnalyzer should contain metrics to omit " + metric.toString()
		+ " from the analysis, but it is empty.");
	if (!metrics.containsKey(metric.toString())) {
	    logger.warn("The ModelAnalyzer does not contain " + metric.toString() + " which is planned to be omitted.");
	} else {
	    metrics.remove(metric.toString());
	    logger.info("Omit the metric from the analysis: " + metric.toString());
	}
	return this;
    }

    /**
     * <p>
     * Returns a {@link Metric} object which is mapped to the
     * {@link ModelMetrics} parameter and contained by the ModelAnalyzer. If
     * there is not any {@link Metric} object, returns <strong>null</strong>.
     * </p>
     * 
     * @param metric
     *            {@link ModelMetrics} value
     * 
     * @return {@link Metric} object or <strong>null</strong>
     */
    public MetricT getMetric(ModelMetrics metric) {
	return metrics.get(metric.toString());
    }

    /**
     * <p>
     * Evaluates every metric ({@link Metric}) which were added for the
     * analysis. The calculations are evaluated in the insertion order of
     * metrics. The adapter parameter ({@link ModelAdapter}) provides the
     * necessary interface for the calculations.
     * </p>
     * 
     * @param adapter
     *            instance of {@link ModelAdapter}
     * @param <N>
     *            the model
     * 
     * @return this
     */
    public <M, N, T> ModelAnalyzer<MetricT> evaluate(final ModelAdapter<M, N, T> adapter) {
	for (MetricT m : metrics.values()) {
	    m.evaluate(adapter);
	}
	return this;
    }

    /**
     * <p>
     * Evaluates the given {@link Metric} identified by the {@link ModelMetrics}
     * parameter. The adapter parameter ({@link ModelAdapter}) provides the
     * necessary interface for the calculations.
     * </p>
     * 
     * @param adapter
     *            instance of {@link ModelAdapter}
     * @param metric
     *            value in {@link ModelMetrics}, identifies the {@link Metric}
     *            to evaluate
     * @param <N>
     *            type of the model
     * 
     * @return this
     */
    public <M, N, T> ModelAnalyzer<MetricT> evaluate(final ModelAdapter<M, N, T> adapter, ModelMetrics metric) {
	MetricT metricObj = getMetric(metric);
	Preconditions.checkNotNull("The " + metric + " metric was not added to the analyzer.", metricObj);

	metricObj.evaluate(adapter);
	return this;
    }

    /**
     * <p>
     * Runs a preliminary validation to check whether every metric requirement
     * is provided by the adapter parameter or not (see {@link ModelAdapter}).
     * Throws an {@link IllegalStateException} if the adapter parameter does not
     * implement an interface that is required by the evaluation of at least one
     * metric.
     * </p>
     * 
     * @param <N>
     *            type of the model
     * 
     * @throws IllegalArgumentException
     *             if the adapter parameter does not implement an interface that
     *             is necessary for the calculations of metrics
     * 
     * @return this
     */
    public ModelAnalyzer<MetricT> validate(final ModelAdapter<?, ?, ?> adapter) {
	throw new UnsupportedOperationException("Not implemented yet");
	// return this;
    }

}
