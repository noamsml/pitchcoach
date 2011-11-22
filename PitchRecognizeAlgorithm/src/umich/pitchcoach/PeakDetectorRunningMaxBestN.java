package umich.pitchcoach;

public class PeakDetectorRunningMaxBestN extends PeakDetectorRunningMaxOutlier {

		//double top;
		private double threshold;
		
		@Override
		public void prepareData(float[] data, int offset, int endoffset) {
			super.prepareData(data, offset, endoffset);
			threshold = StatisticalAnalyzer.getTopNthSafe(data, offset, endoffset, 15);
		}
		@Override
		protected boolean isOutlier(float data) {
			return data > threshold;
		}

}
