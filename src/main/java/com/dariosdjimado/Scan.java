import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

import static com.dariosdjimado.Utils.printExample;


public static <T, R> Gatherer<T, ?, R> scan(R initialValue, BiFunction<R, T, R> accumulator) {
  Supplier<AtomicReference<R>> initializer = () -> new AtomicReference<>(initialValue);
  Gatherer.Integrator<AtomicReference<R>, T, R> integrator = (state, element, downstream) -> {
    state.set(accumulator.apply(state.get(), element));
    downstream.push(state.get());
    return true;
  };

  return Gatherer.ofSequential(initializer, integrator);
}


public void main() {
  List<Integer> input = List.of(1, 2, 3, 4, 5);
  List<Integer> output = input.stream()
      .gather(scan(0, Integer::sum))
      .toList();

  printExample(output);
}
