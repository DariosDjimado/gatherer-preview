import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

import static com.dariosdjimado.Utils.printExample;

public static <T> Gatherer<T, ?, T> reverse() {
  Supplier<List<T>> initializer = ArrayList::new;
  Gatherer.Integrator<List<T>, T, T> integrator = (state, element, _) -> {
    state.add(element);
    return true;
  };

  BiConsumer<List<T>, Gatherer.Downstream<? super T>> finisher = (state, downstream) -> {
    for (int i = state.size() - 1; i >= 0; i--) {
      downstream.push(state.get(i));
    }
  };

  return Gatherer.ofSequential(initializer, integrator, finisher);
}

public void main() {
  List<Integer> input = List.of(1, 2, 3, 4, 5);
  List<Integer> output = input.stream()
      .gather(reverse())
      .toList();

  printExample(output);
}