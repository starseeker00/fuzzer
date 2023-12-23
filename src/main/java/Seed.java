public class Seed {
    private final String data;
    private double energy;
    private int coverageHashCode;

    public Seed(String data) {
        this.data = data;

        this.energy = 0.0;
        this.coverageHashCode = 0;
    }

    public String getData() {
        return data;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public int getCoverageHashCode() {
        return coverageHashCode;
    }

    public void setCoverageHashCode(int coverageHashCode) {
        this.coverageHashCode = coverageHashCode;
    }

    @Override
    public String toString() {
        return data + "\t\t" + energy + "\t\t" + coverageHashCode;
    }
}
