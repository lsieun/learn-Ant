package filter.hmimpl;

import java.util.ArrayList;
import java.util.List;

import filter.IFilter;

public class HMFilter implements IFilter {

    public String[] filter(String[] values, String prefix) {
        if (values == null) {
            return null;
        }
        if (prefix == null) {
            return values;
        }
        List<String> result = new ArrayList<>();
        for (String value : values) {
            if (value != null && value.startsWith(prefix)) {
                result.add(value);
            }
        }
        return result.toArray(new String[result.size()]);
    }
}
