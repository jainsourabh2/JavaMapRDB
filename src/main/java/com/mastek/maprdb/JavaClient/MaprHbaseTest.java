package com.mastek.maprdb.JavaClient;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;

/**
 * Hello world!
 *
 */
public class MaprHbaseTest 
{
	private static Configuration conf = null;
    static {
        conf = HBaseConfiguration.create();
        //conf.set("hbase.table.namespace.mappings","t1:/,t15:/tables,t3:./,t20:/goose/tables");
        conf.set("mapr.htable.impl","com.mapr.fs.MapRHTable");
        //conf.set("fs.default.name", "maprfs://xxx:7222");
        conf.set("hbase.zookeeper.quorum","localhost");
        conf.set("hbase.zookeeper.property.clientPort","5181");
        conf.set("fs.maprfs.impl", "com.mapr.fs.MapRFileSystem");
        conf.set("hadoop.spoofed.user.uid","2000");
        conf.set("hadoop.spoofed.user.gid","2000");
        conf.set("hadoop.spoofed.user.username","mapr");
}	
	
	public static void getAllRecord(String tableName){
		try{
			HTable table = new HTable(conf, tableName.getBytes());
			Scan s = new Scan();
			ResultScanner ss = table.getScanner(s);
			for(Result r:ss){
				for(KeyValue kv : r.raw()){
                    System.out.print(new String(kv.getRow()) + " ");
                    System.out.print(new String(kv.getFamily()) + ":");
                    System.out.print(new String(kv.getQualifier()) + " ");
                    System.out.print(kv.getTimestamp() + " ");
                    System.out.println(new String(kv.getValue()));					
				}
			}
		}catch(IOException e){
			
		}catch(Exception e){
			
		}
	}
	
    public static void main( String[] args )
    {
        try{
        	String tablename = "/basetest";
        	if(args != null && args.length > 0){
        		tablename = args[0];
        	}
        	MaprHbaseTest.getAllRecord(tablename);
        }catch(Exception e){
        	
        }
    }
}
