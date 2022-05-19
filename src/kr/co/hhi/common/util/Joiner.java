package kr.co.hhi.common.util;

import java.lang.Iterable;
import java.util.Iterator;

public class Joiner {
	private String glue;
	public Joiner(String str){
		glue=str;
	}

	public static Joiner on (String str){
		return new Joiner(str);
	}

	public String join(Iterable<String> list){
		String ret="";
		for(Iterator<String> i=list.iterator(); i.hasNext();){
			ret += i.next();
			if(i.hasNext())
				ret += glue;
		}
		return ret;
	}

	public String join(String array[]){
        String ret="";
        for(int i=0; i<array.length; i++){
            ret += array[i];
            if(i<array.length-1)
                ret += glue;
        }
        return ret;
    }
}