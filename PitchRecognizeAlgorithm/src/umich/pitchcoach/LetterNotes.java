package umich.pitchcoach;

public class LetterNotes {
	public static double logC0 = Math.log(16.35);
	public static double C0 = 16.35;
	public static double stepVal = Math.pow(2.0, 1/12.0);
	public static double logStep = Math.log(Math.pow(2.0, 1/12.0));
	public static double logEvalBand = Math.log(Math.pow(2.0, 1/24.0));
	public static String[] steps = new String[]{"C3", "C#3", "D3", "D#3", "E3", "F3", "F#3", "G3", "G#3", "A3", "A#3", "B3"};
	public static int[] noteStepVals = new int[]{0, 2, 4, 5, 7, 9, 10};
	
	public static int stepNum(double freq) //Steps from C0
	{
		double flog = (Math.log(freq) - logC0)/logStep;
		return (int)Math.round(flog);
	}
	
	public static int scalenum(int step)
	{
		return step / 12;
	}
	 
	public static String letter(int step)
	{
		return steps[step % 12];
	}
	
	public static String freqToNoteSpec(double freq)
	{
		if (freq < 0) return "BadNote";
		int step = stepNum(freq);
		return letter(step) + scalenum(step);
	}
	
	public static double noteSpecToFreq(String spec)
	{
		return stepNumToFreq(noteSpecToStepNum(spec));
		
	}
	
	public static int noteSpecToStepNum(String spec)
	{
		int scalestep = letterNoteToStepNum(spec.charAt(0));
		int loc_of_scnum = 1;
		int scnum;
		if (spec.charAt(1) == '#')
		{
			scalestep += 1;
			loc_of_scnum = 2;
		}
		scnum = Integer.parseInt(spec.substring(loc_of_scnum));
		scalestep = scnum * 12 + scalestep;
		return scalestep;
	}
	
	public static int letterNoteToStepNum(char c)
	{
		int letNum = c - 'A';
		return noteStepVals[(letNum + 5) % 7];
	}
	
	public static double stepNumToFreq(int stepNum)
	{
		return C0 * Math.pow(stepVal, stepNum);
	}
	
	public static double evalNote(String noteSpec, double freq) //0 == correct
	{
		double freq_correct = noteSpecToFreq(noteSpec);
		double logError = Math.abs(Math.log(freq_correct/freq));
		if (logError < logEvalBand)
		{
			return 0;
		}
		return logError;
	}
	
	
	//Testing run
	public static void main(String[] arg)
	{
		System.out.println(evalNote("C0", 500));
	}
}
