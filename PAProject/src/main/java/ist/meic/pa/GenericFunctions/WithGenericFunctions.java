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

		CtClass exemplo = pool.get(args[0]);

		CtMethod ctMethod = exemplo.getDeclaredMethod("main");

		ctMethod.instrument(new ExprEditor() {
			            public void edit(MethodCall m) throws CannotCompileException {
                            try {
                               // m.getMethod().insertBefore("{ist.meic.pa.GenericFunctions.Handler dealer = new ist.meic.pa.GenericFunctions.Handler();}");
                                CtClass explain = m.getMethod().getDeclaringClass();
                                String className = explain.getName();
                                if(explain.hasAnnotation(GenericFunction.class)){

                                    /*
                                    TIRA ESTE COMENTARIO PARA VERES O ERRO
                                    handleMethodCalls(explain, className);
                                    explain.toClass();
                                     */

                                    String it = m.getMethod().getName();
                                    String replace = "{new ist.meic.pa.GenericFunctions.Handler().handleMethodCall($args,\"" + it + "\",\"" + className + "\");}";
                                    m.replace(replace);

                                }
                            } catch (NotFoundException e) {
                                e.printStackTrace();
                            }
                        }
			        });



		exemplo.toClass();
		
		Class example = Class.forName(args[0]);
		
		Method meth = example.getMethod("main", String[].class);

		String[] params = null; 
		
	    meth.invoke(null, (Object) params);

	}

	public static void handleMethodCalls(CtClass explain, String className){

		CtMethod[] methods = explain.getMethods();

		for(CtMethod ctm : methods){
        //cada metodo da classe Explain
                try {
                    ctm.instrument(new ExprEditor() {
                        public void edit(MethodCall m) {

                            //cada method call dentro de cada metodo da classe Explain
                            try {
                                if(m.getMethod().getDeclaringClass().hasAnnotation(GenericFunction.class)) {
                                    /*
                                    se o metodo da method call pertencer a uma classe anotada com
                                    GenericFunction, enviar a method call para o Handler
                                    */
                                        String it = ctm.getName();
                                        System.out.println("metodo = " + it);
                                        String replace = "{new ist.meic.pa.GenericFunctions.Handler().handleMethodCall($args,\"" + it + "\",\"" + className + "\");}";
                                        System.out.println(replace);
                                        m.replace(replace);

                                }
                            } catch (NotFoundException e) {
                                e.printStackTrace();
                            } catch (CannotCompileException e) {
                                e.printStackTrace();
                            }
                        }

                    });
                } catch (CannotCompileException e) {
                    e.printStackTrace();
                }
            }

		}





	public static void print(String s){
	    System.out.println(s);
    }
}
