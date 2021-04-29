package Final;
import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;
/** Implements a visual marker for ocean earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 *
 */
public class OceanQuakeMarker extends EarthquakeMarker {
	public OceanQuakeMarker(PointFeature quake) {
		super(quake);
		isOnLand = false;
	}
	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		pg.ellipse(x, y, 2*radius, 2*radius);
	}
}
