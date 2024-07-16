import java.util.List;
import java.util.function.Function;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

import static com.dariosdjimado.Utils.printExample;


public static <T, R> Gatherer<T, ?, R> flatMap(Function<? super T,
    Stream<? extends R>> mapper) {
  Gatherer.Integrator<Void, T, R> integrator = (_, element, downstream) -> {
    mapper.apply(element).forEach(downstream::push);
    return true;
  };

  return Gatherer.of(integrator);
}

public void main() {
  List<List<Integer>> input = List.of(
      List.of(1, 2), List.of(3, 4), List.of(5, 6));
  List<Integer> output = input.stream()
      .gather(flatMap(List::stream))
      .toList();

  printExample(output);
}