package ist.meic.pa.GenericFunctions;

import javassist.*;
import javassist.expr.*;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;

public class WithGenericFunctions {

	/*public ist.meic.pa.GenericFunctions.WithGenericFunctions() throws ClassNotFoundException{

		Class c = Class.forName("ist.meic.pa.GenericFunctions.Explain");
		Method[]  ms = c.getDeclaredMethods();

		for(Method m : ms) {
			System.out.println(m.getName());
			
			for(Parameter p : m.getParameters()) {
				System.out.println(p.getType() + " " + p.getName());
			}
		}
	}
	*/
	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException, CannotCompileException{
		
		ClassPool pool = ClassPool.getDefault();
		pool.importPackage("java.lang.reflect");
		pool.importPackage("java.lang.Class");

		CtClass exemplo = pool.get("ist.meic.pa.GenericFunctions.Exemplo");

		CtMethod ctMethod = exemplo.getDeclaredMethod("main");

		ctMethod.instrument(new ExprEditor() {
			            public void edit(MethodCall m) throws CannotCompileException {
                            try {
                                m.getMethod().insertBefore("{Handler dealer = new Handler();}");
                                CtClass explain = m.getMethod().getDeclaringClass();
                                String className = explain.getSimpleName();
                                if(explain.hasAnnotation(GenericFunction.class)){
                                    String it = m.getMethod().getName();
                                    String replace = "{dealer.handleMethodCall($$,\n" + it + "\n)}";
                                    //m.replace(replace);
                                    System.out.println(replace);
                                }
                            } catch (NotFoundException e) {
                                e.printStackTrace();
                            }
                        }
			        });
		
		Class example = Class.forName("ist.meic.pa.GenericFunctions.Exemplo");
		
		Method meth = example.getMethod("main", String[].class);

		String[] params = null; 
		
	    meth.invoke(null, (Object) params);

	}
}
