import javafx.util.Pair;

public interface Runner {
    String PASS = "PASS";
    String FAIL = "FAIL";
    String UNRESOLVED = "UNRESOLVED";

    default Pair<String, String> run(String inp) {
        return new Pair<>(inp, UNRESOLVED);
    }
}
