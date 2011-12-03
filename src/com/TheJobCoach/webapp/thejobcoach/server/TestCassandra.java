package com.TheJobCoach.webapp.thejobcoach.server;

import java.util.Arrays;

import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.*;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.cassandra.serializers.*;

public class TestCassandra {

	public static void main (String[] args)
	{
		Cluster myCluster = HFactory.getOrCreateCluster("test-cluster","localhost:9160");
		ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition("MyKeyspace",                              
				"ColumnFamilyName", 
				ComparatorType.BYTESTYPE);

		System.out.println("Cluster");

		KeyspaceDefinition newKeyspace = myCluster.describeKeyspace("MyKeyspace");
		if (newKeyspace == null)
		{
			newKeyspace = HFactory.createKeyspaceDefinition("MyKeyspace",                 
					ThriftKsDef.DEF_STRATEGY_CLASS,  
					1, 
					Arrays.asList(cfDef));
			System.out.println("Keyspace");	
			myCluster.addKeyspace(newKeyspace, true);
		}

		System.out.println("Keyspace create");
		Keyspace ksp = HFactory.createKeyspace("MyKeyspace", myCluster);

		ColumnFamilyTemplate<String, String> template = 
				new ThriftColumnFamilyTemplate<String, String>(ksp,
						"CF", 
						StringSerializer.get(),        
						StringSerializer.get());

		// <String, String> correspond to key and Column name.
		ColumnFamilyUpdater<String, String> updater = template.createUpdater("a key");
		updater.setString("domain", "www.datastax.com");
		updater.setLong("time", System.currentTimeMillis());

		System.out.println("Updater");

		try {
			template.update(updater);
		} catch (HectorException e) {
			// do something ...
			System.out.println("Updater error");
		}

		System.out.println("Finish");

	}
}
