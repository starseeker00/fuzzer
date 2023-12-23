import java.util.Random;

public class Mutator {

    private String last = "";
    private StringBuilder current;

    private Random random = new Random();

    public void CharFlip() {
        CharFlip(random.nextInt(127 - 32), random.nextInt(current.length()), 1);
    }

    public void CharFlip(int n, int L, int S) {
        // todo
    }

    public void CharIns() {
        CharIns(random.nextInt(current.length()), 1);
    }

    public void CharIns(int n, int K) {
        // todo
    }

    public void CharDel() {
        CharDel(random.nextInt(current.length()), 1);
    }

    public void CharDel(int n, int K) {
        // todo
    }

    public void Havoc() {
        for (int i = 0; i < random.nextInt(5); i++) {
            int way = random.nextInt(3);
            switch (way) {
                case 0:
                    CharFlip();
                    break;
                case 1:
                    CharIns();
                    break;
                case 2:
                    CharDel();
                    break;
            }
        }
    }

    public void Splice() {
        // todo
    }


    public String mutate(String inp) {
        current = new StringBuilder(inp);

        int way = random.nextInt(4);
        if (way == 0) {
            Splice();
        } else {
            Havoc();
        }

        last = current.toString();
        return last;
    }

}
