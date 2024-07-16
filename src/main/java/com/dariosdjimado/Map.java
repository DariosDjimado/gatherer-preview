import java.util.List;
import java.util.function.Function;
import java.util.stream.Gatherer;

import static com.dariosdjimado.Utils.printExample;


public static <T, R> Gatherer<T, ?, R> map(Function<? super T, ? extends R> mapper) {
  Gatherer.Integrator<Void, T, R> integrator = (_, element, downstream) -> {
    downstream.push(mapper.apply(element));
    return true;
  };

  return Gatherer.of(integrator);
}

public void main() {
  List<Integer> input = List.of(1, 2, 3, 4, 5);
  List<Integer> output = input.stream()
      .gather(map(n -> n * n))
      .toList();

  printExample(output);
}