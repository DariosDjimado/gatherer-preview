import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Gatherer;

import static com.dariosdjimado.Utils.printExample;


public static <T> Gatherer<T, ?, T> filter(Predicate<T> predicate) {
  Gatherer.Integrator<Void, T, T> integrator = (_, element, downstream) -> {
    if (predicate.test(element)) {
      downstream.push(element);
    }
    return true;
  };

  return Gatherer.of(integrator);
}

public void main() {
  List<Integer> input = List.of(1, 2, 3, 4, 5);
  List<Integer> output = input.stream()
      .gather(filter(n -> n % 2 == 0))
      .toList();

  printExample(output);
}


