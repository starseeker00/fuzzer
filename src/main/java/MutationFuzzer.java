import javafx.util.Pair;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class MutationFuzzer implements Fuzzer {

    private final String[] seeds;
    private final Mutator mutator;
    private final PowerSchedule schedule;

    private ArrayList<String> inputs;
    private String inp;

    private List<Seed> population;
    private int seed_index;

    public MutationFuzzer(String[] seeds, Mutator mutator, PowerSchedule schedule) {
        this.seeds = seeds;
        this.mutator = mutator;
        this.schedule = schedule;

        inputs = new ArrayList<>();
        reset();
    }

    public void reset() {
        population = new ArrayList<>();
        seed_index = 0;
    }

    @Override
    public String fuzz() {
        // Returns first each seed once and then generates new inputs

        // todo mutate input

        return inp;
    }

    @Override
    public Pair<String, String> run(Runner runner) {
        // Inform scheduler about path frequency
        Pair<String, String> result = Fuzzer.super.run(runner);

       // todo feedback

        return result;
    }

    public static void main(String[] args) throws IOException {
        int n = 10 * 1000;

        MutationFuzzer fuzzer = new MutationFuzzer(new String[]{"abc"}, new Mutator(), new PowerSchedule());
        long st = System.currentTimeMillis();
        fuzzer.runs(new CoverageRunner(new FileOutputStream("log.txt")), n);
        long ed = System.currentTimeMillis();

        System.out.printf(" test %d inputs in %dms\n", n, ed - st);

        System.out.println("\npopulations:");
        for (Seed seed : fuzzer.population) {
            System.out.println(seed);
        }

        System.out.println("\nlatest 100 inputs:");
        int size = fuzzer.inputs.size();
        System.out.println(fuzzer.inputs.subList(size - 100, size));
    }
}
