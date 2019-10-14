package net.punchtree.minigames.region;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.google.common.collect.Iterators;

public class MultiRegion implements Area{

	private final List<? extends Area> areas;
	
	public MultiRegion(List<? extends Area> areas){
		this.areas = areas;
	}
	
	public MultiRegion(Area... areas) {
		this.areas = Arrays.asList(areas);
	}

	@Override
	public boolean contains(Location l){
		for(Area area : areas) {
			if (area.contains(l)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Iterator<Block> iterator(){
		Iterator<Block> it = Collections.emptyIterator();
		for(Area area : areas) {
			it = Iterators.concat(it, area.iterator());
		}
		return it;
	}
	
}
