import java.util.Random;

public class Mutator {

    private String last = "";
    private StringBuilder current;

    private Random random = new Random();

    public void CharFlip() {
        CharFlip(random.nextInt(127 - 32), random.nextInt(current.length()), 1);
    }

    public void CharFlip(int n, int L, int S) {
        int i = 0;
        while (i < L) {
            for (int j = 0; j < S; j++) {
                int index = random.nextInt(current.length());
                int newChar = current.charAt(index) + n;
                newChar = newChar >= 127 ? newChar % 127 + 32 : newChar;
                current.setCharAt(index, (char) newChar);
                i++;
                if (i >= L) return;
            }
        }
    }

    public void CharIns() {
        CharIns(random.nextInt(current.length()), 1);
    }

    public void CharIns(int n, int K) {
        for (int i = 0; i < n; i++) {
            int index = random.nextInt(current.length());
            for (int j = 0; j < K; j++) {
                current.insert(index, random.nextInt(127 - 32) + 32);
            }
        }
    }

    public void CharDel() {
        CharDel(random.nextInt(current.length()), 1);
    }

    public void CharDel(int n, int K) {
        for (int i = 0; i < n; i++) {
            int index = random.nextInt(current.length());
            current.delete(index, index + K);
        }
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
        current.delete(0, current.length() / 2).append(last.substring(last.length() / 2));
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
