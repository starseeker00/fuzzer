/**
 * The test target we want to see code coverage for.
 */
public class PUT implements Runnable {
    private String str;
    private String result;

    public PUT(String str) {
        this.str = str;
    }

    public void run() {
        result = test(str);
    }

    public String test(String str) {
        if (str.length() > 0 && str.charAt(0) == 'b') {
            if (str.length() > 1 && str.charAt(1) == 'a') {
                if (str.length() > 2 && str.charAt(2) == 'd') {
                    if (str.length() > 3 && str.charAt(3) == '!') {
                        throw new RuntimeException("crashed!!");
                    }
                }
            }
        }
        return "Wrong Answer";
    }

    @Override
    public String toString() {
        return result;
    }
}
