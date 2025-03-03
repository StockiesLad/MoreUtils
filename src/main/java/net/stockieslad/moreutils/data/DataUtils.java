package net.stockieslad.moreutils.data;

import java.util.*;

public class DataUtils {
    public static <T> List<T> list(DataPriority dataPriority, boolean threadSafe) {
        List<T> list = switch (dataPriority) {
            case READ -> new ArrayList<>();
            case WRITE -> new LinkedList<>();
            default -> new CyclicList<>();
        };

        list = threadSafe ? Collections.synchronizedList(list) : list;
        return list;
    }
}
