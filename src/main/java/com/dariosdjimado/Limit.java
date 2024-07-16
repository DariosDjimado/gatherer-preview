import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

import static com.dariosdjimado.Utils.printExample;

public static <T> Gatherer<T, ?, T> limit(long maxSize) {
  Supplier<AtomicLong> initializer = AtomicLong::new;
  Gatherer.Integrator<AtomicLong, T, T> integrator = (state, element, downstream) -> {
    long size = state.getAndIncrement();
    if (size < maxSize) {
      downstream.push(element);
    }
    return size + 1 < maxSize;
  };

  return Gatherer.ofSequential(initializer, integrator);
}

public void main() {
  List<Integer> input = List.of(1, 2, 3, 4, 5);
  List<Integer> output = input.stream()
      .gather(limit(3))
      .toList();

  printExample(output);
}