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
		train_set = new ArrayList<>(train_set.subList(0, 1000));
		ArrayList<float[]> test_set = loadDataSet("zip.test");
		Conventional_KNN nn_obj = new Conventional_KNN();
		
		long tic_toc = System.currentTimeMillis();
		
		double [] error =new double[13];
		int counter=0;
		for(float[] tst_sample: test_set) {
			TreeMap<Double, Integer> trMap = nn_obj.nn_search(train_set, tst_sample,25);
			for(int k = 0; k<13; k++){
				int predict =  nn_obj.classifier( 2*k+1, trMap);
				int label=(int) test_set.get(counter)[0];
				//System.out.println("pred="+predict+" value="+label+" k="+(2*k+1));
				if(predict!=label){
					error[k]++;
				}
			}
			counter++;
		}
		
		for(int k = 0; k<13; k++) {
			System.out.println("For k="+(2*k+1)+" the error rate is: "+ error[k]/test_set.size());
		}	
		
		System.out.println("Ellapsed time: "+(float)(System.currentTimeMillis()-tic_toc)/1000f);
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
