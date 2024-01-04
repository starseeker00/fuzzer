import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PowerSchedule {

    private HashMap<Integer, Integer> frequency = new HashMap<>();

    public void assignEnergy(List<Seed> population) {
        // Assign exponential energy inversely proportional to path frequency
        for (Seed seed : population) {
            seed.setEnergy(
                    1 / Math.pow(frequency.get(seed.getCoverageHashCode()), 5)
            );
        }
    }

    public Seed choose(List<Seed> population) {
        assignEnergy(population);

        // normalize energy
        // choose by energy
        double energy = population.stream().mapToDouble(Seed::getEnergy).sum();
        double random = new Random().nextDouble() * energy;

        double cumulativeWeight = 0.0;
        for (Seed seed : population) {
            cumulativeWeight += seed.getEnergy();
            if (cumulativeWeight > random) {
                return seed;
            }
        }

        return population.get(population.size() - 1);
    }

    public HashMap<Integer, Integer> getFrequency() {
        return frequency;
    }
}
