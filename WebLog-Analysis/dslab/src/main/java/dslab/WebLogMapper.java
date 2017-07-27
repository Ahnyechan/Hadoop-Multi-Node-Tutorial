package dslab;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class WebLogMapper extends Mapper<Object, Text, Text, IntWritable> {
	
	private final static IntWritable one = new IntWritable(1);
	
	private Text outputKey = new Text();
	

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		
		System.err.println("We are inside mapper"); 
		
		String[] at = value.toString().split(" ");
		
		String ip = at[0];
		
		outputKey.set(ip);
		/*
		
		String logEntryLine = value.toString();
		//String logPattern = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}).*\\[(.*?)\\] \\\"(.*?)";
				//"^([0-9]{1,3}) \\. ([0-9]{1,3}) \\. ([0-9]{1,3}) \\. ([0-9]{1,3})";
		//String logPattern = "^(\\S+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\]";
		String logPattern = "^(\\S+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\S+) \"(.+?)\" \"(.+?)\"";
		Pattern p = Pattern.compile(logPattern);
		Matcher m = p.matcher(logEntryLine);
		
		/*if(!m.matches() || m.groupCount() != 7)
		{
			System.err.println("Invalid log entry");
			System.err.println(logEntryLine);
			return;
		}*/
		//Extract http code
		
		//outputKey.set(m.group(1));	
		//Integer httpCode = Integer.parseInt(m.group(6));
		//String hostname = m.group(6).toString();

		context.write(outputKey, one);
	}

}
