package de.westnordost.streetcomplete.quests.max_height;

//if the minimum required API would be 24, we could maybe use https://developer.android.com/reference/android/icu/util/Measure
public class Height
{
	private double meters;

	private int feet;
	private int inches;

	private Unit unit;

	public enum Unit {
		METRIC,
		IMPERIAL
	}

	Height()
	{
		this.meters = -1;
		this.unit = Unit.METRIC;
	}

	Height(double meters)
	{
		this.meters = meters;
		this.unit = Unit.METRIC;
	}

	public Height(int feet, int inches)
	{
		this.feet = feet;
		this.inches = inches;
		this.unit = Unit.IMPERIAL;
	}

	public Unit getUnit()
	{
		return unit;
	}

	public boolean isEmpty()
	{
		if (unit.equals(Unit.METRIC))
		{
			return meters == -1;
		}
		else
		{
			return feet == -1 && inches == -1;
		}
	}

	public String toString()
	{
		if (unit.equals(Unit.METRIC))
		{
			return String.valueOf(meters);
		}
		else
		{
			//this adds an apostrophe and a double-quote to be in a format like e.g. 6'7"
			return String.valueOf(feet) + "'" + String.valueOf(inches) + "\"";
		}
	}

	public double getInMeters()
	{
		if (unit.equals(Unit.METRIC))
		{
			return meters;
		}
		else
		{
			return ((feet * 12 + inches) * 0.0254);
		}
	}
}
