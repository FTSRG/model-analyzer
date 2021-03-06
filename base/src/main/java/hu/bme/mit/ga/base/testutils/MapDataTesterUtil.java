package hu.bme.mit.ga.base.testutils;

import hu.bme.mit.ga.base.data.MapData;
import org.junit.Assert;

public class MapDataTesterUtil {

    public static <K, V extends Number> void checkKeysSize(int expected, final MapData<K, V> data) {
        Assert.assertEquals(expected, data.getValues().keySet().size());
    }

    public static <K, V extends Number> void checkValue(final MapData<K, V> data, K key, V expected) {
        Assert.assertEquals(expected, data.getValues().get(key));
    }

}
