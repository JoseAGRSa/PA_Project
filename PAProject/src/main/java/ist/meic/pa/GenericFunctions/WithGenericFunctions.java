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
                                CtClass explain = m.getMethod().getDeclaringClass();
                                String className = explain.getSimpleName();
                                if(explain.hasAnnotation(GenericFunction.class)){
                                    CtMethod[] mts = explain.getDeclaredMethods();
                                    for(CtMethod ctm : mts){
                                        String name = ctm.getName();
                                        CtClass[] ctc = ctm.getParameterTypes();
                                        for(CtClass ctcl : ctc){
                                            if(!ctcl.getSimpleName().equals("Object")){
                                                name += "_" + ctcl.getSimpleName();
                                            }
                                        }
                                        ctm.setName(name);
                                    }
                                    String ib = "{String name = $1.getClass().getSimpleName(); Method m = " + className +
                                            ".class.getMethod(\"" + m.getMethod().getName() +
                                            "_\" + name, $1.getClass().getName().class);}";
                                    //System.out.println("IB: " + ib);
                                    m.getMethod().insertBefore(ib);
                                    explain.toClass();
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
