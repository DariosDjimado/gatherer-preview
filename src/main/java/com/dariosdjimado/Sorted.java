import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

import static com.dariosdjimado.Utils.printExample;

public static <T> Gatherer<T, ?, T> sorted(Comparator<T> comparator) {
  Supplier<List<T>> initializer = ArrayList::new;
  Gatherer.Integrator<List<T>, T, T> integrator = (state, element, _) -> {
    state.add(element);
    return true;
  };

  BiConsumer<List<T>, Gatherer.Downstream<? super T>> finisher = (state, downstream) -> {
    state.sort(comparator);
    state.forEach(downstream::push);
  };

  return Gatherer.ofSequential(initializer, integrator, finisher);
}

public void main() {
  List<Integer> input = List.of(5, 3, 2, 1, 4);
  List<Integer> output = input.stream()
      .gather(sorted(Comparator.<Integer>naturalOrder()))
      .toList();

  printExample(output);
}