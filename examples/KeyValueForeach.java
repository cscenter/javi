import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class KeyValueForeach
{
    public static void main(String[] args) {
        Map<String, Integer> map = new TreeMap<String, Integer>();
        map.put("A", 10);
        map.put("B", 20);
        map.put("C", 30);

        Set<String> keys = map.keySet();
        Collection<Integer> values = map.values();
        Set<Map.Entry<String, Integer>> entries = map.entrySet();

        for (String key : keys) {
            System.out.print(" " + key);
        }
        System.out.println();
        for (Integer value : values) {
            System.out.print(" " + value);
        }
        System.out.println();
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            System.out.print(" " + key + "->" + value);
        }
    }
}
