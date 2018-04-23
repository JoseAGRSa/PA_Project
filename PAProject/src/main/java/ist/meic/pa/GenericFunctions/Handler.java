package ist.meic.pa.GenericFunctions;


import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Handler {

    ClassComparator cc = new ClassComparator();

    public Object handleMethodCall(Object[] args,
                                 String methodName, String className) {

        try {
            Class[] parametersTypes = new Class[args.length];
            int i = 0;

            for (Object a : args) {
                parametersTypes[i] = a.getClass();
                i++;
            }
            Class<?> c = Class.forName(className);

            handleBeforeMethods(c, methodName,args);
            Object o = handleMethods(c, methodName,args);
            handleAfterMethods(c, methodName,args);

            return o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object handleMethods(Class<?> c, String methodName, Object[] args){
        try {
            Method[] ms = sort(c.getDeclaredMethods(),args);
            for(Method m: ms){
                WithGenericFunctions.print(args.getClass().getName());
                if(!m.isAnnotationPresent(BeforeMethod.class) && !m.isAnnotationPresent(AfterMethod.class)){
                    Class<?>[] paramTypes = m.getParameterTypes();
                    List<Class<?>> paramTypesToList = new ArrayList<>(Arrays.asList(paramTypes));
                    if(m.getParameterTypes().length==args.length){
                        if(manage(paramTypesToList, args) && m.getName().equals(methodName)){
                            WithGenericFunctions.print("Dsfddsf");
                            return m.invoke(m,args);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void handleBeforeMethods(Class<?> c, String methodName, Object[] args){
        try {
            Method[] ms = sort(c.getDeclaredMethods(),args);
            for(Method m: ms){
                if(m.isAnnotationPresent(BeforeMethod.class)){
                    Class<?>[] paramTypes = m.getParameterTypes();
                    List<Class<?>> paramTypesToList = new ArrayList<>(Arrays.asList(paramTypes));
                    if(m.getParameterTypes().length==args.length){
                        if(manage(paramTypesToList, args) && m.getName().equals(methodName)){
                            m.invoke(m,args);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void handleAfterMethods(Class<?> c, String methodName, Object[] args){
        try {
            Method[] ms = sort(c.getDeclaredMethods(),args);
            List<Method> msToList = new ArrayList<>(Arrays.asList(ms));
            Collections.reverse(msToList);
            for(Method m: msToList){
                if(m.isAnnotationPresent(AfterMethod.class)){
                    Class<?>[] paramTypes = m.getParameterTypes();
                    List<Class<?>> paramTypesToList = new ArrayList<>(Arrays.asList(paramTypes));
                    if(m.getParameterTypes().length==args.length){
                        if(manage(paramTypesToList, args) && m.getName().equals(methodName)){
                            m.invoke(m,args);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Boolean manage(List<Class<?>> params, Object[] args){
        Object[] aux = args;
        if(params.get(0).isAssignableFrom(args[0].getClass())){
            if(params.size()>1){
                params.remove(0);
            }
            if(aux.length>1) {
                aux = Arrays.copyOfRange(args,1,args.length);
            }
            if(params.size()!=1 && aux.length!=1){
                return manage(params,aux);
            }
            return true;
        }
        return false;
    }

    public Method[] sort(Method[] mts, Object[] args){
        List<Method> mtsToList = new ArrayList<>();
        for(Method m: mts){
            if(m.getParameterTypes().length==args.length){
                mtsToList.add(m);
            }
        }
        Method[] sortedMethods = new Method[mtsToList.size()];
        int tamanho = mtsToList.size();

        for(int i=0; i<tamanho; i++){
            Method aux = getMostSpecific(mtsToList,args);
            if(aux!=null){
                sortedMethods[i] = aux;
            }
            mtsToList.remove(sortedMethods[i]);
        }
        return sortedMethods;
    }

    public Method getMostSpecific(List<Method> mts, Object[] args){

        List<Method> mtsAux = new ArrayList<>(mts);
        if(mts.size()==1){
            return mts.get(0);
        }

        for(Method m1: mts){
            Class<?>[] m1Params = m1.getParameterTypes();
            for(Method m2: mts){
                if(mts.indexOf(m1)!=mts.indexOf(m2)){
                    Class<?>[] m2Params = m2.getParameterTypes();
                    int ret = cc.compare(m1Params,m2Params);
                    if(ret==1){
                        mtsAux.remove(m1);
                        return getMostSpecific(mtsAux, args);
                    }
                    else if(ret==0 || ret==-1){
                        mtsAux.remove(m2);
                        return getMostSpecific(mtsAux,args);
                    }
                    else{
                        Class<?>[] t = args[0].getClass().getInterfaces();
                        if(t.length>0) {
                            if(m1.getParameterTypes()[0]==t[0]){
                                mtsAux.remove(m1);
                                return getMostSpecific(mtsAux,args);
                            }
                            else{
                                mtsAux.remove(m2);
                                return getMostSpecific(mtsAux,args);
                            }
                        }
                            mtsAux.remove(m1);
                            return getMostSpecific(mtsAux,args);
                    }
                }
            }
        }
        return null;
    }
}