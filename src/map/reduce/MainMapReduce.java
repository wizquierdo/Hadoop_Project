package map.reduce;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MainMapReduce {

	public static void main(String[] args) throws IllegalArgumentException, IOException, ClassNotFoundException, InterruptedException {
		// TODO Auto-generated method stub

		Configuration conf = new Configuration();
		conf.set("testing_set", args[2]);
		conf.setInt("k", Integer.parseInt(args[4]));
		conf.setLong(FileInputFormat.SPLIT_MAXSIZE, 14512253/Integer.parseInt(args[5]));
		Job HadoopJob = Job.getInstance(conf, "MapReduceDS");
				
		HadoopJob.setJarByClass(MainMapReduce.class);	
		HadoopJob.setMapperClass(map.reduce.Mapper.class);
		HadoopJob.setReducerClass(map.reduce.Reducer.class);
		
		HadoopJob.setMapOutputKeyClass(IntWritable.class);
		HadoopJob.setMapOutputValueClass(ListOfPairs.class);
		HadoopJob.setOutputKeyClass(IntWritable.class);
		HadoopJob.setOutputValueClass(IntWritable.class);
		
		HadoopJob.setNumReduceTasks(Integer.parseInt(args[6]));
				
		FileInputFormat.addInputPath(HadoopJob, new Path(args[1]));
		FileOutputFormat.setOutputPath(HadoopJob, new Path(args[3]));
		
		long tic_toc = System.currentTimeMillis();
		boolean completed = HadoopJob.waitForCompletion(true);
		System.out.println("Ellapsed time: "+(float)(System.currentTimeMillis()-tic_toc)/1000f);
		
		System.out.println("Error Rate: "+getErrorRate(loadDataSet(args[2], conf), args[3], conf));
		
		System.exit(completed? 0 : 1);
	}
	
	public static ArrayList<float[]> loadDataSet(String path, Configuration cfg){
		ArrayList<float[]> data = new ArrayList<>();
		try {
			Path p = new Path(path);
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getFileSystem(cfg).open(p)));
			while(input.ready()){
				String[] seg = input.readLine().split(" ");
				float[] s = new float[seg.length];
				for(int i=0; i<s.length; i++){
					s[i] = Float.parseFloat(seg[i]);
				}
				data.add(s);
			}
			input.close();
			return data;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static float getErrorRate(ArrayList<float[]> testSamples, String path, Configuration cfg){
		float eRate = 1f/(float)testSamples.size();
		int errorCount = 0;
		try {
			Path p = new Path(path+"/part-r-00000");
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getFileSystem(cfg).open(p)));
			while(input.ready()){
				String[] seg = input.readLine().split("\t");
				int i = Integer.parseInt(seg[0]);
				int c = Integer.parseInt(seg[1]);
				if(c != (int)testSamples.get(i)[0])
					errorCount++;
			}
			input.close();
			return eRate*errorCount;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

}
