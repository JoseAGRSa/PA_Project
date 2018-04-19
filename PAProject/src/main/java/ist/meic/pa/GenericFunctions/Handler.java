package ist.meic.pa.GenericFunctions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.Class.*;

public class Handler {

    public void handleMethodCall(Object[] args,
                                 String methodName, String className) {

        try {
            Class[] parametersTypes = null;

            int i = 0;

            for (Object a : args) {
                parametersTypes[i] = a.getClass();
                i++;
            }

            Class<?> c = Class.forName(className);

            Method m = c.getDeclaredMethod(methodName, parametersTypes);

            m.invoke(args);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
