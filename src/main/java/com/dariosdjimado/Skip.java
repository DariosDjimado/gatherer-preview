import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

import static com.dariosdjimado.Utils.printExample;

public static <T> Gatherer<T, ?, T> skip(long n) {
  Supplier<AtomicLong> initializer = AtomicLong::new;
  Gatherer.Integrator<AtomicLong, T, T> integrator = (state, element, downstream) -> {
    long index = state.getAndIncrement();
    if (index >= n) {
      downstream.push(element);
    }
    return true;
  };

  return Gatherer.ofSequential(initializer, integrator);
}

public void main() {
  List<Integer> input = List.of(1, 2, 3, 4, 5);
  List<Integer> output = input.stream()
      .gather(skip(2))
      .toList();

  printExample(output);
}