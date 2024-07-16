import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

import static com.dariosdjimado.Utils.printExample;

public static <T> Gatherer<T, ?, T> interleave(Stream<T> other) {
  Supplier<Iterator<T>> initializer = other::iterator;
  Gatherer.Integrator<Iterator<T>, T, T> integrator = (state, element, downstream) -> {
    downstream.push(element);
    if (state.hasNext()) {
      downstream.push(state.next());
    }
    return true;
  };

  BiConsumer<Iterator<T>, Gatherer.Downstream<? super T>> finisher = (state, downstream) -> {
    while (state.hasNext()) {
      downstream.push(state.next());
    }
  };

  return Gatherer.ofSequential(initializer, integrator, finisher);
}

public void main() {
  List<Integer> input = List.of(1, 3, 5, 7, 9);
  List<Integer> output = input.stream()
      .gather(interleave(Stream.of(2, 4, 6, 8, 10)))
      .toList();

  printExample(output);
}