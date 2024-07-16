import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

import static com.dariosdjimado.Utils.printExample;

@SafeVarargs
public static <T> Gatherer<T, ?, T> concat(Stream<T>... other) {
  Gatherer.Integrator<Void, T, T> integrator = (_, element, downstream) -> {
    downstream.push(element);
    return true;
  };

  BiConsumer<Void, Gatherer.Downstream<? super T>> finisher = (_, downstream) -> {
    for (Stream<T> stream : other) {
      stream.forEach(downstream::push);
    }
  };

  return Gatherer.of(integrator, finisher);
}

public void main() {
  List<Integer> input = List.of(1, 2, 3, 4, 5);
  List<Integer> output = input.stream()
      .gather(
          concat(Stream.of(6, 7, 8, 9, 10), Stream.of(11, 12, 13, 14, 15)))
      .toList();

  printExample(output);
}