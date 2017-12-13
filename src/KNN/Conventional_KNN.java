package KNN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Conventional_KNN {
	
	public TreeMap<Double,Integer> nn_search (ArrayList<float[]> training_set, float[] tst_sample, int k) {
			
		TreeMap<Double, Integer> neighbors = new TreeMap<Double, Integer>();
			
		for(float[] tr_sample: training_set){
			int digit =(int)tr_sample[0];
			double dist = Euclidean(tr_sample, tst_sample);
			neighbors.put(dist, digit);
			if(neighbors.size()>k){
					neighbors.remove(neighbors.lastKey());
			}
		}
		return neighbors;
	}
	

	public Integer classifier( int k, TreeMap<Double,Integer> tmp) {
		
		TreeMap<Integer, Integer> voting = new TreeMap<>();
		int max = -1;
		int digit = -1;
		for(int i: tmp.values()){
			Integer label_count = voting.get(i);
			if (label_count == null)
				label_count = 0;
			label_count += 1;
			voting.put(i, label_count);
			if (label_count > max) {
				digit = i;
				max = label_count;
			}
		}		
		return digit;
	}
	
	public double Euclidean(float[] vector1, float[] vector2){
		double cum=0;
		for (int i=1; i<vector1.length ; i++) {
			cum+=Math.pow((vector1[i]-vector2[i]), 2);
		}
		return Math.sqrt(cum);
	}
	
	
//public Integer classifier( int k, TreeMap<Double,Integer> tmp) {
//		
//		int [] labels = new int[k];
//		int counter=0;
//		for(int i: tmp.values()){
//			labels[counter]=i;
//			counter++;
//		}	
//		return getMode(labels);
//}
//	
//	public static int getMode(int[] values) {
//		
//		  HashMap<Integer,Integer> freqs = new HashMap<Integer,Integer>();
//
//		  for (int val : values) {
//		    Integer freq = freqs.get(val);
//		    freqs.put(val, (freq == null ? 1 : freq+1));
//		  }
//
//		  int mode = 0;
//		  int maxFreq = 0;
//
//		  for (Map.Entry<Integer,Integer> entry : freqs.entrySet()) {
//		    int freq = entry.getValue();
//		    if (freq > maxFreq) {
//		      maxFreq = freq;
//		      mode = entry.getKey();
//		    }
//		  }
//
//		  return mode;
//		}
}


