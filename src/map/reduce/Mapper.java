package map.reduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

public class Mapper extends org.apache.hadoop.mapreduce.Mapper<LongWritable, Text, IntWritable, ListOfPairs> {

	ArrayList<float[]> test_set;
	ArrayList<TreeMap<Double,Integer>> maps;
	int k=1;
	
	@Override
	public void setup(Context cxt) {
		 test_set=MainMapReduce.loadDataSet(cxt.getConfiguration().get("testing_set"), cxt.getConfiguration());
		 maps = new ArrayList<TreeMap<Double,Integer>>();
		 for (int i = 0; i < test_set.size(); i++) {
			maps.add(new TreeMap<Double,Integer>());
		}
		 k=cxt.getConfiguration().getInt("k",1);
	}
	
	@Override
	public void cleanup(Context cxt) throws IOException, InterruptedException {
		for (int i = 0; i < test_set.size(); i++) {
			cxt.write(new IntWritable(i), new ListOfPairs(maps.get(i)));
		}
	}
	
	@Override
	public void map (LongWritable lineNo, Text training_sample, Context cxt) {
		
		String[] splits = training_sample.toString().split(" ");
		float[] samples = new float[splits.length];
		for(int i=0; i<samples.length; i++){
			samples[i] = Float.parseFloat(splits[i]);
		}
		
		int index=0;
		for (float[] tst_sample:test_set) {
			TreeMap<Double, Integer> neighbors = maps.get(index);
			int digit =(int)samples[0];
			double dist = Euclidean(samples, tst_sample);
			neighbors.put(dist, digit);
			if(neighbors.size()>k){
				neighbors.remove(neighbors.lastKey());
			}
			index++;
		}	
	}
	
	public double Euclidean(float[] vector1, float[] vector2){
		double cum=0;
		for (int i=1; i<vector1.length ; i++) {
			cum+=Math.pow((vector1[i]-vector2[i]), 2);
		}
		return Math.sqrt(cum);
	}
}
