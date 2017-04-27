package com.mastek.maprdb.JavaClient;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class MapRJavaPut {
	public static void main(String[] args) throws IOException {
		//Create an instance of HBase Configuration
		Configuration config = HBaseConfiguration.create();

		//Create an instance to the MapRDB table.
        HTable table = new HTable(config, "/user/mapr/test_table");

        //Input values for the DML Operation. 
        String rowkey = args[0].toString();
        String columnfamily = args[1].toString();
        String columnqualifier = args[2].toString();
        String cellvalue = args[3].toString();
        
        //Create table
        HBaseAdmin admin = new HBaseAdmin(config);
        HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf("/user/mapr/test_table2"));
        tableDescriptor.addFamily(new HColumnDescriptor("name"));
        tableDescriptor.addFamily(new HColumnDescriptor("address"));        
        admin.createTable(tableDescriptor);        
        
        //Put Operation
        Put p = new Put(Bytes.toBytes(rowkey));
        p.add(Bytes.toBytes(columnfamily), Bytes.toBytes(columnqualifier),Bytes.toBytes(cellvalue));
        table.put(p);
        
        //Get Operation
        Get g = new Get(Bytes.toBytes(rowkey));
        Result r = table.get(g);
        byte[] value = r.getValue(Bytes.toBytes(columnfamily),Bytes.toBytes(columnqualifier));
        String valueStr = Bytes.toString(value);
        System.out.println("GET: " + valueStr);
        
        //Delete Operation (Entire row)
        Delete delete = new Delete(Bytes.toBytes(rowkey));
        table.delete(delete);   
        
        //Scan Operation
        Scan s = new Scan();
        s.addColumn(Bytes.toBytes(columnfamily), Bytes.toBytes(columnqualifier));
        ResultScanner scanner = table.getScanner(s);
        try {
            for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
                System.out.println("Found row: " + rr);
            }
        } finally {
        	table.close();
            scanner.close();
            config.clear();
        }
    }
}
