package school.mjc.parser;

import java.util.Collection;
import java.util.function.Function;

public class CollectionUtil {

    public static <T> T getSingle(Collection<T> collection, Function<Integer, String> errorMessageSupplier) {
        if (collection.size() != 1) {
            throw new IllegalArgumentException(errorMessageSupplier.apply(collection.size()));
        }
        return collection.iterator().next();
    }
}
