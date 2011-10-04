package umich.pitchcoach;

public interface TileDraw {
	public void drawPitch(double pitch, double sampleNo);
		//sampleNo uses the sample received from the microphone
		//to graph the data correctly
		//fn will decide if data should be graphed or not
		//sampleNo also used to determine time of how a user is doing
		
	//Question: How do we know which tile will be drawn to?
	//Is that in scope, or is that for Lu to determine?
	//Will there just be an array of tiles?
	
	public void setPitch(String pitch);
		//for display at bottom of tile. e.g. A4 
	
	public String getPitch();
		//returns the pitch of a given tile
	
	public boolean drawFinished();
		//returns true if user has sung correct note for 3 seconds, or 10 seconds have elapsed
	
	public int getUserResult();
		//returns int corresponding to how particular tile did
		//e.g. 1 = red, 2 = yellow, 3 = green. More colors for future?
}
