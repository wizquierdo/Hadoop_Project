package map.reduce;

import java.io.IOException;
import java.util.TreeMap;

import org.apache.hadoop.io.IntWritable;

public class Reducer extends org.apache.hadoop.mapreduce.Reducer<IntWritable, ListOfPairs, IntWritable, IntWritable> {

	int k=1;
	
	@Override
	public void setup(Context cxt) {
		 k=cxt.getConfiguration().getInt("k",1);
	}
	
	@Override
	public void reduce(IntWritable index, Iterable<ListOfPairs> maps, Context cxt) throws IOException, InterruptedException {

		TreeMap<Double, Integer> reduced_map = new TreeMap<Double, Integer>();

		for (ListOfPairs i : maps) {
			reduced_map.putAll(i.map);
		}
		
		cxt.write(index, new IntWritable (classifier(k, reduced_map)));
	}
	
	public Integer classifier(int k, TreeMap<Double, Integer> tmp) {

		TreeMap<Integer, Integer> voting = new TreeMap<>();
		int max = -1;
		int digit = -1;
		int counter = 0;
		for (int i : tmp.values()) {
			if (counter < k) {
				Integer label_count = voting.get(i);
				if (label_count == null)
					label_count = 0;
				label_count += 1;
				voting.put(i, label_count);
				if (label_count > max) {
					digit = i;
					max = label_count;
				}
				counter++;
			}
		}
		return digit;
	}
}
