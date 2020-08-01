package net.punchtree.minigames.utility.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterators;

public class CirculatingList<E> implements Iterable<E> {

	private List<E> e;
	private final boolean shuffle;
	private int i = -1;
	
	public CirculatingList(){
		this(false);
	}
	
	public CirculatingList(boolean shuffle){
		this(new ArrayList<E>());
	}
	
	public CirculatingList(E[] e){
		this(e, false);
	}
	
	public CirculatingList(E[] e, boolean shuffle){
		this(Arrays.asList(e), shuffle);
	}
	
	public CirculatingList(Collection<E> e){
		this(e, false);
	}
	
	public CirculatingList(Collection<E> e, boolean shuffle){
		this.e = new ArrayList<E>(e);
		this.shuffle = shuffle;
		if(shuffle) shuffle();
	}
	
	public E next() throws NullPointerException{
		if(e.size() == 0) throw new NullPointerException();
		if (i + 1 == e.size()){
			i = 0;
			if(shuffle) shuffle();
		}else{
			i++;
		}
		return e.get(i);
	}
	
	public void resetIterator() {
		i = -1;
	}
	
	public void shuffle(){
		Collections.shuffle(e);
	}
	
	public List<E> getList(){
		return e;
	}

	@Override
	public Iterator<E> iterator() {
		return Iterators.concat(e.subList(i, e.size()).iterator(), e.subList(0, i).iterator());
	}
	
}
