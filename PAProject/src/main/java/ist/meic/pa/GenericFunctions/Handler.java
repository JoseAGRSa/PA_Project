package ist.meic.pa.GenericFunctions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Class.*;

public class Handler {

    public void handleMethodCall(Object[] args,
                                 String methodName, String className) {

        try {
            Class[] parametersTypes = new Class[args.length];
            int i = 0;

            for (Object a : args) {
                parametersTypes[i] = a.getClass();
                i++;
            }
            Class<?> c = Class.forName(className);

            Method m = c.getDeclaredMethod(methodName, parametersTypes);
            //WithGenericFunctions.print("" + c.isAnnotationPresent(GenericFunction.class));
            handleBeforeMethods(c,args);
            m.invoke(m,args);
            handleAfterMethods(c,args);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleBeforeMethods(Class<?> c, Object[] args){
        try {
            for(Method m: c.getDeclaredMethods()){
                if(m.isAnnotationPresent(BeforeMethod.class)){
                    Class<?> aux = args[0].getClass();
                    WithGenericFunctions.print(aux.getName());
                    List<Class<?>> supers = getAllSuperClass(aux);
                    WithGenericFunctions.print("" + supers);

                    Class<?>[] pr  = m.getParameterTypes();
                    List<Class<?>> auxToList = new ArrayList<>();

                    for(Class<?> a: pr){
                        auxToList.add(a);
                    }

                    if(manage(auxToList,args)){
                        m.invoke(m,args);
                    }


                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void handleAfterMethods(Class<?> c, Object[] args){
        try {
            for(Method m: c.getDeclaredMethods()){
                if(m.isAnnotationPresent(AfterMethod.class)){
                    Class<?> aux = args[0].getClass();
                    List<Class<?>> supers = getAllSuperClass(aux);
                    if(manage(supers, args)){
                        m.invoke(m,args);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    //TODO: VERIFICAR SE O TAMANHO DO ARGS E IGUAL AO TAMANHO DOS PARAMETROS
    public Boolean manage(List<Class<?>> params, Object[] args){
        Object[] aux = args;
        if(getAllSuperClass(args[0].getClass()).contains(params.get(0))){
            if(params.size()>1){
                params.remove(0);
            }
            if(aux.length>1) {
                aux = Arrays.copyOfRange(args,1,args.length);
            }
            if(params.size()!=1 && aux.length!=1){
                manage(params,aux);
            }
            return true;
        }
        return false;

    }

    public List<Class<?>> getAllSuperClass(Class<?> c){
        List<Class<?>> list = new ArrayList<>();

        while(c!=Class.class){
            list.add(c);
            if(c.getSuperclass()==null){
                break;
            }
            c=c.getSuperclass();
        }

        return list;
    }
}
