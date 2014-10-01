package utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DiffDates {
	
	Calendar m_a = GregorianCalendar.getInstance();
	Calendar m_b = GregorianCalendar.getInstance();

	public DiffDates (Date a, Date b) 
	{
		this.m_a.setTime(a);
		this.m_b.setTime(b);
	}
	
	public Integer Years () 
	{		
		m_b.add(Calendar.DAY_OF_YEAR, -m_a.get(Calendar.DAY_OF_YEAR));
		
		return m_b.get(Calendar.YEAR) - m_a.get(Calendar.YEAR);
	}
	
}
