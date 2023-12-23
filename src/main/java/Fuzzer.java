import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public interface Fuzzer {

    String fuzz();

    default Pair<String, String> run(Runner runner) {
        return runner.run(fuzz());
    }

    default List<Pair<String, String>> runs(Runner runner, int trails) {
        List<Pair<String, String>> results = new ArrayList<>();
        for (int i = 0; i < trails; i++) {
            // 计算进度百分比
            int progress = (int) (i * 100.0 / trails);

            // 更新进度条
            System.out.print("\rfuzzing: " + progress + "%");
            System.out.flush();

            results.add(run(runner));
        }
        return results;
    }
}
