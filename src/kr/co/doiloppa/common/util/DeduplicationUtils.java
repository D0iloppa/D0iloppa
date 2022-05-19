/**
 * 
 */
package kr.co.doiloppa.common.util;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * bean 객체 리스트 특정 키 값을 중심으로 중복제거
 * @author doil
 *
 */
public class DeduplicationUtils {

	    public static <T> List<T> deduplication(final List<T> list, Function<? super T, ?> key) {
	        return list.stream()
	            .filter(deduplication(key))
	            .collect(Collectors.toList());
	    }

	    private static <T> Predicate<T> deduplication(Function<? super T, ?> key) {
	        final Set<Object> set = ConcurrentHashMap.newKeySet();
	        return predicate -> set.add(key.apply(predicate));
	    }
	}