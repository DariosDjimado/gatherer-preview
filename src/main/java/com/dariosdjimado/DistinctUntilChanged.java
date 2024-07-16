import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

import static com.dariosdjimado.Utils.printExample;

public static <T> Gatherer<T, ?, T> distinctUntilChanged() {
  Supplier<AtomicReference<T>> initializer = AtomicReference::new;
  Gatherer.Integrator<AtomicReference<T>, T, T> integrator = (state, element, downstream) -> {
    T lastValue = state.getAndSet(element);
    if (!element.equals(lastValue)) {
      downstream.push(element);
    }
    return true;
  };

  return Gatherer.ofSequential(initializer, integrator);
}

public void main() {
  List<Integer> input = List.of(1, 1, 2, 2, 3, 3, 1, 1, 4, 4, 5, 5, 2, 2);
  List<Integer> output = input.stream()
      .gather(distinctUntilChanged())
      .toList();

  printExample(output);
}