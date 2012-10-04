package edu.ufl.cise.cop5555.sp12;

import java.util.HashMap;
import java.util.Set;


public class Runtime {
	
    public static String className = "edu/ufl/cise/cop5555/sp12/Runtime";
    public static String binopDescriptor = "(Ljava/util/HashMap;Ljava/util/HashMap;)Ljava/util/HashMap;";
    public static String relopDescriptor = "(Ljava/util/HashMap;Ljava/util/HashMap;)Z";

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap plus(HashMap map0, HashMap map1) { 
		//this is (sort of) union.  if (c,v) in map0 and (c,v1) in map1, then (c,v) will be replaced with (c, v1)
		HashMap tmp = (HashMap)map0.clone();
		tmp.putAll(map1);
		return tmp;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap times(HashMap map0, HashMap map1) {
		//this computes the intersection
		HashMap tmp = (HashMap)map0.clone();
		tmp.entrySet().retainAll(map1.entrySet());
		return tmp;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap minus(HashMap map0, HashMap map1) {
		HashMap tmp = (HashMap)map0.clone();
		tmp.entrySet().removeAll(map1.entrySet());
		return tmp;
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean equals(HashMap map0, HashMap map1) {
		return map0.equals(map1);
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean  not_equals(HashMap map0, HashMap map1) {
		return ! equals(map0,map1);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean at_most(HashMap map0, HashMap map1) {
		Set entries0 = map0.entrySet();
		Set entries1 = map1.entrySet();
		return entries1.containsAll(entries0);
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static boolean less_than(HashMap map0, HashMap map1) {
		return at_most(map0,map1) && !equals(map0, map1);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean at_least(HashMap map0,  HashMap map1) {
		Set entries0 = map0.entrySet();
		Set entries1 = map1.entrySet();
		return entries0.containsAll(entries1);
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean greater_than(HashMap map0, HashMap map1) {
		return at_least(map0,map1) && !equals(map0, map1);
	}
		
}
