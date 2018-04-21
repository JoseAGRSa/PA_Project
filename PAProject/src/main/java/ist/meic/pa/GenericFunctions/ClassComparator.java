package ist.meic.pa.GenericFunctions;

import java.util.Arrays;
import java.util.Comparator;

public class ClassComparator implements Comparator<Class<?>[]> {

    public int compare(Class<?>[] c1, Class<?>[] c2) {

        int ret = -1;

        if(c1 == null) {
            if(c2 == null) {
                return 0;
            } else {
                // Sort nulls first
                return -1;
            }
        } else if(c2 == null) {
            // Sort nulls first
            return 1;
        }

        if(c1.equals(c2)) {
            return 0;
        }

        for(int i=0; i<c1.length; i++){
            if(!c2[i].equals(c1[i])){
                boolean c2Lower = c2[i].isAssignableFrom(c1[i]);
                boolean c1Lower = c1[i].isAssignableFrom(c2[i]);

                if(c2Lower && !c1Lower) {
                    return ret;
                } else if(c1Lower && !c2Lower) {
                    ret=1;
                    return ret;
                }
                else{
                    return 9999;
                }
            }
        }
        return ret;
    }
}