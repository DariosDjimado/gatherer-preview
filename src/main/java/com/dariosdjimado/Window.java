import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

import static com.dariosdjimado.Utils.printExample;

public static <T> Gatherer<T, ?, List<T>> window(int size) {
  assert size > 0 : "Size must be greater than 0";

  Supplier<List<T>> initializer = () -> new ArrayList<>(size);
  Gatherer.Integrator<List<T>, T, List<T>> integrator = (state, element, downstream) -> {
    state.add(element);
    if (state.size() == size) {
      downstream.push(List.copyOf(state));
      state.clear();
    }
    return true;
  };

  BiConsumer<List<T>, Gatherer.Downstream<? super List<T>>> finisher = (state, downstream) -> {
    if (!state.isEmpty()) {
      downstream.push(List.copyOf(state));
    }
  };

  return Gatherer.ofSequential(initializer, integrator, finisher);
}

public void main() {
  List<Integer> input = List.of(1, 2, 3, 4, 5);
  List<List<Integer>> output = input.stream()
      .gather(window(2))
      .toList();

  printExample(output);
}