*Tags: Java, Object Oriented Programing*<br>
**Description:**
- Worked with an existing library to build an earthquake GUI map in Java OOP using a large dataset tagged by geospatial information.
- Read earthquake data from a live RSS feed and plotted recent earthquakes on a map to highlight potential disaster zones.
- Applied abstract class, inheritance, polymorphism, and UML (class hierarchy) diagram to build a class hierarchy to reflect the different kinds of markers for different categories of earthquakes.
- Built event handlers to respond to graphical user input (hover to display a city/earthquake information, click on map to show only earthquakes/cities in thread circle, click in key section to show only that category in the map)
- Wrote and applied searching and sorting algorithms to manage large data sets (print largest earthquakes in term of magnitude).
- Used test cases to ensure correctness of a program.
- Tools used :  *Java, PApplet, PGraphics*

**Java files:** 

*Check out “Chau Nguyen – UML.xlsx” to see the relationship among classes*

- CityMarker.java
- CommonMarker.java
- EarthquakeCityMap.java
- EarthquakeMarker.java
- LandQuakeMarker.java
- OceanQuakeMarker.java
- - -



Starter codes offered by UC San Diego through Coursera Object Oriented Programming in Java course.

INSTALLATION

Import this folder in Eclipse ('File' -> 'Import' -> 'Existing Projects into Workspace', Select this folder, 'Finish')


MANUAL INSTALLATION

If the import does not work follow the steps below.

- Create new Java project
- Copy+Paste all files into project
- Add all lib/*.jars to build path
- Set native library location for jogl.jar. Choose appropriate folder for your OS.
- Add data/ as src


TROUBLE SHOOTING

Switch Java Compiler to 1.6 if you get VM problems. (Processing should work with Java 1.6, and 1.7)

---
**Goals:**
- Use sorting to answer questions about data sets
- Implement the Comparable interface 
- Organize the earthquake data and compute statistics on it

**Implementation:**

1. **Add functionality to the classes developed in the previous parts of the project**: EarthquakeCityMap.java , EarthquakeMarker.java , CityMarker.java , CommonMarker.java, LandQuakeMarker.java , OceanQuakeMarker.java. IF YOU ARE WORKING OFFLINE, you should once again make sure the variable offline is set to true.

2. **Implement the Comparable interface in EarthquakeMarker:**
- add the keywords “implements Comparable<EarthquakeMarker>” to the class definition.
- implement the compareTo(EarthquakeMarker marker) method in the EarthquakeMarker class so that it sorts earthquakes in reverse order of magnitude.  

3. **Add and Implement the private method void sortAndPrint(int numToPrint) in EarthquakeCityMap.**  This method will create a new array from the list of earthquake markers (there is a method in the List interface named toArray() which returns the elements in the List as an array of Objects).  Then it will sort the array of earthquake markers in reverse order of their magnitude (highest to lowest) and then print out the top numToPrint earthquakes. If numToPrint is larger than the number of markers in quakeMarkers, it should print out all of the earthquakes and stop, but it should not crash.Call this method from setUp() to test it.  Use test2.atom as the input file, and sortandPrint.test2.out.txt is the expected output for a couple different calls to sortAndPrint.

---

	public void setup() {		
		size(900, 700, OPENGL);
		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom";  
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 650, 600, new Google.GoogleMapProvider());
		}
		MapUtils.createDefaultEventDispatcher(this, map);
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    quakeMarkers = new ArrayList<Marker>();
	    for(PointFeature feature : earthquakes) {
		  if(isLand(feature)) {
		    quakeMarkers.add(new LandQuakeMarker(feature));
		  }
		  else {
		    quakeMarkers.add(new OceanQuakeMarker(feature));
		  }
	    }
	    printQuakes();
	    sortAndPrint(20);
	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers);
	}  


	private void sortAndPrint(int numToPrint) {
		System.out.println("Largest "+numToPrint+" Earthquakes are:");
		List<EarthquakeMarker> eqms = new ArrayList();
		Object[] obs =quakeMarkers.toArray();
		for (int i=0;i<obs.length;i++) eqms.add((EarthquakeMarker) obs[i]);
		Collections.sort(eqms);
		Object[] sortedeqs=eqms.toArray();
		for (int y=0;y<numToPrint;y++) {
			if (y<sortedeqs.length) System.out.println(((EarthquakeMarker)sortedeqs[y]).toString());	
		}
	}

4. **Add other extension!**
* ***All Earthquakes are presented by a circle***
To make every Earthquake appear as a circle: In OceanQuakeMarker.java, change the code in the drawEarthquake method so its draw a circle instead of a square. Replace the code "pg.rect(x-radius, y-radius, 2*radius, 2*radius);" with "pg.ellipse(x, y, 2*radius, 2*radius);" 

* ***When we click on an Earthquake type in Key section (Shallow, Intermediate, Deep), only Earthquakes of that type will appear on the Map, all other types and cities will disappear. Can make everything appear again by click on an Earthquake twice.***
In EarthquakeCityMap.java, add a method checkKeyForClick(). This method will check if an Earthquake type ((Shallow, Intermediate, Deep) in Key section is clicked on. We check this by testing the range of mouseX and mouseY coordinates. If an Earthquake type in Key section is clicked, we loop over the earthquake markers, for the earthquake markers belong to that Earthquake type, we setHidden to False, everything else setHidden to True. (Test whether an earthquake marker belong to an Earthquake type by testing the depth of that earthquake, reference to the colorDetermine and getDepth method in EarthquakeMarker.java).
In EarthquakeCityMap.java, also modify the method mouseClicked() so that after it uses checkCitiesForClick(), it uses checkKeyForClick() too.

---
	public void mouseClicked(){
		if (lastClicked != null) {
			unhideMarkers();
			lastClicked = null;
		}
		else if (lastClicked == null) 
		{
			checkEarthquakesForClick();
			if (lastClicked == null) {
				checkCitiesForClick();
				if (lastClicked==null) {checkKeyForClick();}
			}
		}
	}


	private void checkKeyForClick()	{
		
		if (lastClicked != null) return;
		for (Marker mhide : cityMarkers) 
				mhide.setHidden(true);				

		float INTERMEDIATE = 70;
		float DEEP = 300;
		
		if (mouseX>=55 && mouseX<=65) {
			if(mouseY>=185 && mouseY<=195) {
				for (Marker m:quakeMarkers) {
					EarthquakeMarker marker = (EarthquakeMarker)m;
					if (marker.getDepth()<INTERMEDIATE) marker.setHidden(false); else marker.setHidden(true);
				} return;
			}
			
			if(mouseY>=205 && mouseY<=215) {
				for (Marker m:quakeMarkers) {
					EarthquakeMarker marker = (EarthquakeMarker)m;
					if (marker.getDepth()>=INTERMEDIATE && marker.getDepth()<DEEP) marker.setHidden(false); else marker.setHidden(true);
				} return;
			}
			if(mouseY>=225 && mouseY<=235) {
				for (Marker m:quakeMarkers) {
					EarthquakeMarker marker = (EarthquakeMarker)m;
					if (marker.getDepth()>=DEEP) marker.setHidden(false); else marker.setHidden(true);
				} return;				
			}
		}				
	}
---

![](https://github.com/cmn0705/Earthquake_Map/blob/master/Chau%20Nguyen%20-%20Submit%20Final.png)