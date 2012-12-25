package com.TheJobCoach.userdata;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;

import com.TheJobCoach.util.CassandraAccessor;

public class TodoList 
{
	static ColumnFamilyDefinition cfDef = null;

	final static String COLUMN_FAMILY_NAME_TODOLIST = "todolist";

	public TodoList()
	{
		cfDef = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_TODOLIST, cfDef);
	}
	
}
