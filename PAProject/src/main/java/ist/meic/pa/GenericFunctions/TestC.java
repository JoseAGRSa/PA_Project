package ist.meic.pa.GenericFunctions;

public class TestC {
    public static void main(String[] args) {
        Object colors = new Object[] { new Red(), 2.9, new Black(), "Holla!"};
        System.out.println(Color.mix(colors));
    }
}

