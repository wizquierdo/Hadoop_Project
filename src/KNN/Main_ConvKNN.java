package KNN;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class Main_ConvKNN {

	public static void main(String[] args) {
		ArrayList<float[]> train_set = loadDataSet("zip.train");
		//train_set = new ArrayList<>(train_set.subList(0, 1000));
		ArrayList<float[]> test_set = loadDataSet("zip.test");
		Conventional_KNN nn_obj = new Conventional_KNN();
		
		long tic_toc = System.currentTimeMillis();
		for(int k = 1; k<26; k++){
			long tic = System.currentTimeMillis();
			double error=0;
			int counter=0;
			for(float[] tst_sample: test_set){
				TreeMap<Double, Integer> trMap = nn_obj.nn_search(train_set, tst_sample,k);
				int predict =  nn_obj.classifier( k, trMap);
				int label=(int) test_set.get(counter)[0];
				if(predict!=label){
					error++;
				}
				counter++;
			}
			long toc=System.currentTimeMillis();
			System.out.println("For k="+(k)+" the error rate is: "+ String.format( "%.8f", error/test_set.size() ));
			System.out.println("Execution time: "+(float)(toc-tic)/1000f);
			k++;
		}	
		
		System.out.println("Execution time: "+(float)(System.currentTimeMillis()-tic_toc)/1000f);
	}
	
	public static ArrayList<float[]> loadDataSet(String path){
		ArrayList<float[]> data = new ArrayList<>();
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
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

}
