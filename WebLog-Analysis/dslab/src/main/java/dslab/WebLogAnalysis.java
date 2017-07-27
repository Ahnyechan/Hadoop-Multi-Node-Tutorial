package dslab;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

//
//import dslab.WebLogMapper;
//import dslab.WebLogReducer;

public class WebLogAnalysis {

	  public static void main(String[] args) throws Exception {
		  
			Configuration conf = new Configuration();
			
			if(args.length != 2) {
				System.err.println("Usage : WebLogAnalysis <input> <output>");
				System.exit(2);
			}
			
			Job job = Job.getInstance(conf, "WebLogAnalysis");
			//����� ������ ��� ����
			FileInputFormat.addInputPath(job, new Path(args[0]));
			FileOutputFormat.setOutputPath(job, new Path(args[1]));
			//��Ŭ���� ����
			job.setJarByClass(WebLogAnalysis.class);
			
			job.setMapperClass(WebLogMapper.class);		//Mapper Ŭ���� ����
			//job.setCombinerClass(WebLogReducer.class);	
			job.setReducerClass(WebLogReducer.class);	//Reducer Ŭ���� ����
			
			job.setInputFormatClass(TextInputFormat.class);
			job.setOutputFormatClass(TextOutputFormat.class);
			
			//���Ű �� ��°� ���� ����
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);

			job.waitForCompletion(true);
			//return (job.waitForCompletion(true) ? 0 : 1);
			//System.exit(job.waitForCompletion(true) ? 0 : 1);
	  }
}
