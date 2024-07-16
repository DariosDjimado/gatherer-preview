import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

import static com.dariosdjimado.Utils.printExample;

public static <T> Gatherer<T, ?, List<T>> pairwise() {
  Supplier<AtomicReference<T>> initializer = AtomicReference::new;
  Gatherer.Integrator<AtomicReference<T>, T, List<T>> integrator = (state, element, downstream) -> {
    T previous = state.getAndSet(element);
    if (previous != null) {
      downstream.push(List.of(previous, element));
    }
    return true;
  };

  return Gatherer.ofSequential(initializer, integrator);
}

public void main() {
  List<Integer> input = List.of(1, 2, 3, 4, 5);
  List<List<Integer>> output = input.stream()
      .gather(pairwise())
      .toList();

  printExample(output);
}