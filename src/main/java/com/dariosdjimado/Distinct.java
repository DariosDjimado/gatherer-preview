import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

import static com.dariosdjimado.Utils.printExample;

public static <T> Gatherer<T, ?, T> distinct() {
  Supplier<Set<T>> initializer = HashSet::new;
  Gatherer.Integrator<Set<T>, T, T> integrator = (state, element, downstream) -> {
    if (state.add(element)) {
      downstream.push(element);
    }
    return true;
  };

  return Gatherer.ofSequential(initializer, integrator);
}

public void main() {
  List<Integer> input = List.of(1, 2, 3, 4, 5, 5, 4, 3, 2, 1);
  List<Integer> output = input.stream()
      .gather(distinct())
      .toList();

  printExample(output);
}