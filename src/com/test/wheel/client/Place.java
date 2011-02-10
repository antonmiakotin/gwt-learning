package com.test.wheel.client;

import java.util.ArrayList;
import java.util.Comparator;





public class Place {

	private String name;
	private String color;
	private int numVotes;
	
	public Place(){
		
	}
	
	public Place(String _name, String _color){
		name = _name;
		color = _color;
		numVotes = 1;
	}
	
	public String getName(){
		return name;
	}
	public String getColor(){
		return color;
	}
	public int getNumVotes(){
		return numVotes	;
	}
	
	public void setName(String name){
		this.name = name;
	}
	public void setColor(String color){
		this.color = color;
	}
	public void incrementVotes(){
		numVotes++;
	}
	

	
}

class PlaceComparator implements Comparator{

	@Override
	public int compare(Object place1, Object place2) {
		// TODO Auto-generated method stub
		String place1Name = ((Place)place1).getName();
		String place2Name = ((Place)place2).getName();
		
		
		return place1Name.compareTo(place2Name);
	}
	
	
}
