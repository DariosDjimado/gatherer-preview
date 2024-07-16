import java.util.List;
import java.util.stream.Gatherer;

import static com.dariosdjimado.Utils.printExample;

public static <T> Gatherer<T, ?, T> repeat(int n) {
  Gatherer.Integrator<Void, T, T> integrator = (_, element, downstream) -> {
    for (int i = 0; i < n; i++) {
      downstream.push(element);
    }
    return true;
  };

  return Gatherer.of(integrator);
}

public void main() {
  List<Integer> input = List.of(1, 2, 3, 4, 5);
  List<Integer> output = input.stream()
      .gather(repeat(3))
      .toList();

  printExample(output);
}