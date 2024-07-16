import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Gatherer;

import static com.dariosdjimado.Utils.printExample;

public static <T> Gatherer<T, ?, T> peek(Consumer<T> consumer) {
  Gatherer.Integrator<Void, T, T> integrator = (_, element, downstream) -> {
    consumer.accept(element);
    downstream.push(element);
    return true;
  };

  return Gatherer.of(integrator);
}

public void main() {
  List<Integer> input = List.of(1, 2, 3, 4, 5);
  List<Integer> output = input.stream()
      .gather(peek(i -> System.out.println(STR."Processing: \{i}")))
      .toList();

  printExample(output);
}