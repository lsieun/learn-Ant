package filter.ccimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import filter.IFilter;

public class CCFilter implements IFilter {

    public String[] filter(String[] values, final String prefix) {
        if (values == null) {
            return null;
        }
        if (prefix == null) {
            return values;
        }

        List<String> result = new ArrayList<>(Arrays.asList(values));
        CollectionUtils.filter(result, new Predicate<String>() {
            public boolean evaluate(String string) {
                return string != null && string.startsWith(prefix);
            }
        });
        return result.toArray(new String[result.size()]);
    }
}
