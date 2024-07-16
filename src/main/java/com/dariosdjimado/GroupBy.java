import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

import static com.dariosdjimado.Utils.printExample;

public static <T, K> Gatherer<T, ?, List<T>> groupBy(Function<? super T, ? extends K> classifier) {
  Supplier<Map<K, List<T>>> initializer = HashMap::new;
  Gatherer.Integrator<Map<K, List<T>>, T, List<T>> integrator = (state, element, _) -> {
    K key = classifier.apply(element);
    state.computeIfAbsent(key, _ -> new ArrayList<>()).add(element);
    return true;
  };

  BiConsumer<Map<K, List<T>>, Gatherer.Downstream<? super List<T>>> finisher = (state, downstream) -> {
    state.forEach((_, values) -> downstream.push(List.copyOf(values)));
  };

  return Gatherer.ofSequential(initializer, integrator, finisher);
}

public void main() {
  List<Integer> input = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
  List<List<Integer>> output = input.stream()
      .gather(groupBy(i -> i % 2 == 0))
      .toList();

  printExample(output);
}