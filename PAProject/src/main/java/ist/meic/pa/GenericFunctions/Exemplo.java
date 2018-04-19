package ist.meic.pa.GenericFunctions;

public class Exemplo {
	
	public static void main(String[] args){
		Object[] objs = new Object[] {"Hello",1,2.0};
		for(Object o : objs) {
			Explain.it(o);
		}


	}
}
	
	// java -cp genericFunctions.jar ist.meic.pa.GenericFunctions.WithGenericFunctions ist.meic.pa.GenericFunctions.Exemplo
	// ist.meic.pa.GenericFunctions.Exemplo = args[0]
	// https://jboss-javassist.github.io/javassist/tutorial/tutorial2.html