package umich.pitchcoach;

public class StatisticalAnalyzer {
	public static StatisticalData statAnal(float[] data, int start, int end)
	{
		StatisticalData retval = new StatisticalData();
		float[] data_copy = new float[end-start];
		for (int i = start; i < end; i++)
		{
			data_copy[i-start] = data[i];
		}
		
		//For now, approximate location rather than perfect quartiles
		retval.firstQuartile = getNth(data_copy, data_copy.length/4);
		retval.thirdQuartile = getNth(data_copy, data_copy.length - data_copy.length/4);
		retval.quartileDiff = retval.thirdQuartile - retval.firstQuartile;
		return retval;
	}
	
	public static float getNth(float[] data, int n)
	{
		return getNth(data, n, 0, data.length);
	}
	
	public static float getNth(float[] data, int n, int begin, int end)
	{
		int loc = partition(data, begin, end);
		if (loc == n)
		{
			return data[loc];
		}
		else if (loc > n)
		{
			return getNth(data, n, begin, loc);
		}
		else
		{
			return getNth(data, n, loc+1, end);
		}
	}
	
	public static int partition(float[] data, int begin, int end)
	{
		int pivot = getPivot(data, begin, end);
		swap(data, pivot, end-1);
		int currentIndex = begin;
		
		for (int i = begin; i < end-1; i++)
		{
			if (data[i] < data[end-1])
			{
				swap(data, i, currentIndex);
				currentIndex++;
			}
		}
		
		swap(data, currentIndex, end-1);
		return currentIndex;
	}
	
	public static int getPivot(float[] data, int begin, int end)
	{
		int a = begin;
		int b = end - 1;
		int c = (end + begin) / 2;
		
		if (data[a] > data[b])
		{
			if (data[c] > data[a]) return a;
			else if (data[c] > data[b]) return c;
			else return b;
		}
		else
		{
			if (data[c] > data[b]) return b;
			else if (data[c] > data[a]) return c;
			else return a;
		}
	}
	
	
	public static void swap(float[] data, int a, int b)
	{
		float temp = data[a];
		data[a] = data[b];
		data[b] = temp;
	}
	
}