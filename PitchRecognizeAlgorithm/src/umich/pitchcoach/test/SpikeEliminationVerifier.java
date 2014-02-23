package umich.pitchcoach.test;

import umich.pitchcoach.SpikeEliminator;

public class SpikeEliminationVerifier {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpikeEliminator elim = new SpikeEliminator(2);
		System.out.println(elim.getSmoothedSample(2));
		System.out.println(elim.getSmoothedSample(2.1f));
		System.out.println(elim.getSmoothedSample(5f));
		System.out.println(elim.getSmoothedSample(2.3f));
		System.out.println(elim.getSmoothedSample(2.6f));
		System.out.println(elim.getSmoothedSample(0.5f));
		System.out.println(elim.getSmoothedSample(0.7f));
		System.out.println(elim.getSmoothedSample(0.4f));
	}

}
