package com.test.wheel.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.test.wheel.shared.FieldVerifier;
import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WOY implements EntryPoint {
	  
	//private static final HorizontalAlignmentConstant CENTER = "ALIGN_CENTER";
	private VerticalPanel mainPanel = new VerticalPanel();
	  private FlexTable restaurantsFlexTable = new FlexTable();
	  private HorizontalPanel addPanel = new HorizontalPanel();
	  private TextBox newRestTextBox = new TextBox();
	  private Button addRestButton = new Button("Add");
	  private ArrayList<Place> restArray = new ArrayList<Place>();
	  
	  private AbsolutePanel imagePanel = new AbsolutePanel();
	  private HorizontalPanel spinPannel = new HorizontalPanel();
	  private Button spinButton = new Button("Spin");
	  private ArrayList<String> colorList = new ArrayList<String>();
	  private ArrayList<String> alreadyPicked = new ArrayList<String>();
	  
	  
	  
	  private SpinAnimation animation = null;
	  final Image chart = new Image();
	  final Image tick = new Image();
	  private double radian = 0.1;
	  private double totalDegrees;
	  
	  
	  private class SpinAnimation extends Animation{
		  
		  
		    private int centerX = 96;

		    /**
		     * The y-coordinate of the center of the circle.
		     */
		    private int centerY = 96;

		    /**
		     * The radius of the circle.
		     */
		    private int radius = 88;
			@Override
			protected void onUpdate(double progress) {
				// TODO Auto-generated method stub
				radian -= totalDegrees*(1-progress);
				updatePosition(tick, radian, 0);
			      GWT.log("Radian: " + Double.toString(radian));
			     // GWT.log("Progress: " + Double.toString(progress));
			}
			
		    @Override
		    protected void onComplete() {
		      super.onComplete();

		    }
		    
		    @Override
		    protected void onStart() {
		      super.onStart();

		    }
		    
		    private void updatePosition(Widget w, double radian, double offset) {
		        radian += offset;
		        double x = radius * Math.cos(radian) + centerX;
		        double y = radius * Math.sin(radian) + centerY;
		        imagePanel.setWidgetPosition(w, (int) x, (int) y);
		        GWT.log("Left: "+Integer.toString(imagePanel.getWidgetLeft(tick)));
		        GWT.log("Top: "+Integer.toString(imagePanel.getWidgetTop(tick)));
		      }
	  }
	  

	  /**
	   * Entry point method.
	   */
	  public void onModuleLoad() {

		  colorList.add("FF5959");
		  colorList.add("FFA659");
		  colorList.add("DDFF59");
		  colorList.add("59FF85");
		  colorList.add("59FFF1");
		  colorList.add("59A3FF");
		  colorList.add("9659FF");
		  colorList.add("FF59D0");
		  
		  chart.setUrl("http://chart.googleapis.com/chart?cht=p&chs=200x200&chd=s:Helo,Wrld");
		  tick.setUrl("http://upload.wikimedia.org/wikipedia/commons/c/c4/Locator_Dot.png");
		  imagePanel.setPixelSize(200, 200);
		  
		//Attach Styles
		  spinButton.setStyleName("spinButton");
		  addRestButton.setStyleName("addButton");
		  
		  mainPanel.setStyleName("centerWidth");
		  imagePanel.setStyleName("centerNoWidth");
		  mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		  newRestTextBox.setStyleName("placeTextBox");

		  
		
		// TODO Assemble Add Stock panel.
		addPanel.add(newRestTextBox);
		addPanel.add(addRestButton);
		
		// TODO Assemble Main panel.
		mainPanel.add(restaurantsFlexTable);
		mainPanel.add(addPanel);
		mainPanel.add(imagePanel);
		imagePanel.add(chart,0,0);
		//imagePanel.add(chartTop);
		imagePanel.add(tick);
		//imagePanel.add(tick,96,96);
		mainPanel.add(spinButton);

		
		// TODO Associate the Main panel with the HTML host page.
		RootPanel.get("restList").add(mainPanel);
		
		// TODO Move cursor focus to the input box.
		newRestTextBox.setFocus(true);
		animation = new SpinAnimation();
		
		
		spinButton.addClickHandler(new ClickHandler() {
			      public void onClick(ClickEvent event) {
			    	totalDegrees =  Random.nextDouble();
			        animation.run(5000);
			      }
			    });
		
		addRestButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				addRest();
				makeChart(makeChartURL());
			}

		});
		newRestTextBox.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				// TODO Auto-generated method stub
				if(event.getCharCode() == KeyCodes.KEY_ENTER){
					addRest();
					makeChart(makeChartURL());
				}
			}
		});
	  
	  }
	  

		private void addRest() {
			// TODO Auto-generated method stub
			final String restName = newRestTextBox
				.getText().toUpperCase().trim();
			newRestTextBox.setFocus(true);
			if(!restName.matches("^[a-zA-Z]{1,20}$")){
				Window.alert("'" + restName + " is not a valid restaurant name.");
				newRestTextBox.selectAll();
				return;
			}
			if(doesExist(restName)){
				return;
			}
			
			int row = restaurantsFlexTable.getRowCount();
			String color = randomColor();
			final Place newPlace = new Place(restName, color);
			restArray.add(newPlace);
			
			//Make this new place a widget, insert into flex table
		    Button voteButton = new Button(newPlace.getName());
		    voteButton.addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		    	  	newPlace.incrementVotes();
		    	  	makeChart(makeChartURL());
		      }
		    });    
		    DOM.setStyleAttribute(voteButton.getElement(),
                    "backgroundColor",
                    newPlace.getColor());

		    voteButton.setStylePrimaryName("placeButton");
		    

			
			restaurantsFlexTable.setWidget(row, 0, voteButton);
			
			//Add "Remove" button

		    Button removePlaceButton = new Button("x");
		    removePlaceButton.addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		        int removedIndex = restArray.indexOf((Object)newPlace);
		        restArray.remove(removedIndex);
		        restaurantsFlexTable.removeRow(removedIndex);
		        makeChart(makeChartURL());
		      }
		      
		    });
		    removePlaceButton.setStyleName("removeButton");
		    restaurantsFlexTable.setWidget(row, 2, removePlaceButton);
		    newRestTextBox.selectAll();
		    
		    
		}
		

		
		
		
		private String randomColor(){
			int index = (Random.nextInt(8));
			String colorPicked = colorList.get(index);
			if((restArray.size()%7)!=0){
				while(colorDoesExist(colorPicked)){
					index = (Random.nextInt(8));
					colorPicked = colorList.get(index);
				}
				alreadyPicked.add(colorPicked);
			}else{
				alreadyPicked.clear();
				index = (Random.nextInt(8));
				colorPicked = colorList.get(index);
				alreadyPicked.add(colorPicked);
			}
			
			return colorPicked;
			
		}
		
		
		private Boolean doesExist(String placeName){
			for(Place pl:restArray){
				if(pl.getName().equalsIgnoreCase(placeName)){
					return true;
				}
			}
			return false;
		}
		
		private Boolean colorDoesExist(String colorName){
			for(String cl:alreadyPicked){
				if(cl.equalsIgnoreCase(colorName)){
					return true;
				}
			}
			return false;
		}
		
		private String makeChartURL(){
			String url = "http://chart.googleapis.com/chart?cht=p&chs=200x200&chd=t:";
			String color = "&chco=";
			if(!restArray.isEmpty()){
				for(int i=0;i<restArray.size();i++){
					Place pl = restArray.get(i);
					url += pl.getNumVotes();
					color += pl.getColor();
					
					if(i<restArray.size()-1){
						url += ",";
						color += "|";
					}
				}
				url += color;
			}
			else{
				url += "1,2,3";
			}
			
			return url;
			
		}
		private void makeChart(String url){
			GWT.log("makeChart(): "+ url);
			chart.setUrl(url);
		}
		

		
		

		  
	}

