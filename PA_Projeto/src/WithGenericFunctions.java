import java.lang.reflect.InvocationTargetException;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;


public class WithGenericFunctions {

	/*public WithGenericFunctions() throws ClassNotFoundException{

		Class c = Class.forName("Explain");
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
		
		CtClass cc = pool.get("Exemplo");
		
		CtMethod ctMethod = cc.getDeclaredMethod("main");
		
		ctMethod.instrument(
			        new ExprEditor() {
			            public void edit(MethodCall m)
			                          throws CannotCompileException
			            {
			            	//if(m.getClass().isAnnotationPresent(GenericFunction.class)) {
			            		//m.replace("if($1 instanceof Integer){	System.out.println(\"IS A INTEGER\");");
			            						
			            	//}
			            	
			                System.out.println(m.getClassName() + "." + m.getMethodName() + " " + m.getSignature());
			            }
			        });
		
		Class example = Class.forName("Exemplo");
		
		Method meth = example.getMethod("main", String[].class);
		
		
		
		String[] params = null; 
		
	    meth.invoke(null, (Object) params);
		
		
	}
		
}
