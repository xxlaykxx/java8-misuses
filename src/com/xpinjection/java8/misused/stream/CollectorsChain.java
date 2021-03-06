package com.xpinjection.java8.misused.stream;

import com.xpinjection.java8.misused.Annotations.Good;
import com.xpinjection.java8.misused.Annotations.Ugly;
import com.xpinjection.java8.misused.User;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

public class CollectorsChain {
    @Ugly
    class GroupByAndTransformResultingMap {
        public Map<String, Integer> getMaxAgeByUserName(List<User> users) {
            return users.stream()
                    .collect(groupingBy(User::getName))
                    .entrySet().stream()
                    .collect(toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().stream()
                                    .mapToInt(User::getAge)
                                    .reduce(0, Integer::max)
                    ));
        }
    }

    @Good
    class CollectToMapWithMergeFunction {
        public Map<String, Integer> getMaxAgeByUserName(List<User> users) {
            return users.stream()
                    .collect(toMap(User::getName,
                            User::getAge,
                            Integer::max));
        }
    }

    @Good
    class ApplyReduceCollectorAsDownstream {
        public Map<String, Integer> getMaxAgeByUserName(List<User> users) {
            return users.stream()
                    .collect(groupingBy(User::getName, mapping(User::getAge,
                            reducing(0, Integer::max))));
        }
    }
}
