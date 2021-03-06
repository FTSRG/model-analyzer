package hu.bme.mit.ga.base.testutils;

import hu.bme.mit.ga.base.data.MappedListData;
import org.junit.Assert;

public class MappedListDataTesterUtil {

    public static <T, V extends Number> void checkSize(final int expectedSize, final MappedListData<T, V> data) {
        Assert.assertEquals(expectedSize, data.getValues().size());
    }

    public static <T, V extends Number> void checkSize(final int expectedSize, final T type,
                                                       final MappedListData<T, V> data) {
        Assert.assertEquals(expectedSize, data.getValues().get(type).size());
    }

    public static <T, V extends Number> void checkTypesNumber(final int expectedNumberOfTypes,
                                                                   final MappedListData<T, V> data) {
        Assert.assertEquals(expectedNumberOfTypes, data.getValues().keySet().size());
    }

    public static <T, V extends Number> void checkValue(final V expectedValue, final T type, final int index,
                                                        final MappedListData<T, V> data) {
        Assert.assertEquals(expectedValue, data.getValues().get(type).get(index));
    }

}
