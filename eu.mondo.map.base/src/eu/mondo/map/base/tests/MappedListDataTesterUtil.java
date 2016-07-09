package eu.mondo.map.base.tests;

import org.junit.Assert;

import eu.mondo.map.base.data.MappedListData;

public abstract class MappedListDataTesterUtil {

	public static <T, V extends Number> void checkSize(final int expectedSize, final MappedListData<T, V> data) {
		Assert.assertEquals(expectedSize, data.getValues().size());
	}

	public static <T, V extends Number> void checkSize(final int expectedSize, final T type,
			final MappedListData<T, V> data) {
		Assert.assertEquals(expectedSize, data.getValues().get(type).size());
	}

	public static <T, V extends Number> void checkDimensionsNumber(final int expectedNumberOfDimensions,
			final MappedListData<T, V> data) {
		Assert.assertEquals(expectedNumberOfDimensions, data.getValues().keySet().size());
	}

	public static <T, V extends Number> void checkValue(final V expectedValue, final T type, final int index,
			final MappedListData<T, V> data) {
		Assert.assertEquals(expectedValue, data.getValues().get(type).get(index));
	}

}
