import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

import static com.dariosdjimado.Utils.printExample;

public static <T> Gatherer<T, ?, T> dropWhile(Predicate<T> predicate) {
  Supplier<AtomicBoolean> startKeeping = AtomicBoolean::new;
  Gatherer.Integrator<AtomicBoolean, T, T> integrator = (state, element, downstream) -> {
    boolean keep = state.get();
    if (keep) {
      downstream.push(element);
    } else if (!predicate.test(element)) {
      state.set(true);
      downstream.push(element);
    }

    return true;
  };

  return Gatherer.ofSequential(startKeeping, integrator);
}

public void main() {
  List<Integer> input = List.of(1, 2, 3, 4, 5);
  List<Integer> output = input.stream()
      .gather(dropWhile(n -> n < 3))
      .toList();

  printExample(output);
}