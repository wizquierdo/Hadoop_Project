package map.reduce;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.hadoop.io.ObjectWritable;

public class ListOfPairs extends ObjectWritable{
	public TreeMap<Double,Integer>  map;
	
	public ListOfPairs() {}
	
	public ListOfPairs(TreeMap<Double,Integer>  map) {
		this.map=map;
	}
	
	 public void write(DataOutput out) throws IOException {
         out.writeInt(map.size());
         for(Entry<Double, Integer> e : map.entrySet()) {
        	 out.writeDouble(e.getKey());
        	 out.writeInt(e.getValue());
         }
     }
       
     public void readFields(DataInput in) throws IOException {
        int size = in.readInt();
        map = new TreeMap<>();
        for(int i=0; i<size; i++) {
        	Double key = in.readDouble();
        	Integer value = in.readInt();
        	map.put(key, value);
        }
     }
       
     public static ListOfPairs read(DataInput in) throws IOException {
    	ListOfPairs w = new ListOfPairs();
        w.readFields(in);
        return w;
     }
}
