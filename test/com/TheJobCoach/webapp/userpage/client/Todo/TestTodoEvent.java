package com.TheJobCoach.webapp.userpage.client.Todo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Vector;

import org.junit.Test;

import com.TheJobCoach.webapp.userpage.shared.TodoEvent;

public class TestTodoEvent {
	
	TodoEvent orgList0 = new TodoEvent(0,0,      100, 100);
	TodoEvent orgList1 = new TodoEvent(110,0,      100, 100);
	TodoEvent orgList2 = new TodoEvent(200,300,      100, 100);
	Vector<TodoEvent> orgList_0 = new Vector<TodoEvent>(Arrays.asList(orgList0, orgList1, orgList2));

	TodoEvent orgList1_0 = new TodoEvent(0,0,        1000, 100);
	TodoEvent orgList1_1 = new TodoEvent(5,100,      10, 100);
	TodoEvent orgList1_2 = new TodoEvent(500,100,    100, 100);
	Vector<TodoEvent> orgList_1 = new Vector<TodoEvent>(Arrays.asList(orgList1_0, orgList1_1, orgList1_2));
	
	TodoEvent orgList2_0 = new TodoEvent("0", 0,0,                                          100, 100);
	TodoEvent orgList2_1 = new TodoEvent("1", TodoEvent.NO_PLACE,TodoEvent.NO_PLACE,        100, 100);
	TodoEvent orgList2_2 = new TodoEvent("2", 500,100,                                      100, 100);
	TodoEvent orgList2_3 = new TodoEvent("3", TodoEvent.NO_PLACE,TodoEvent.NO_PLACE,        100, 100);
	Vector<TodoEvent> orgList_2 = new Vector<TodoEvent>(Arrays.asList(orgList2_0, orgList2_1, orgList2_2, orgList2_3));
	
	//@Test
	public void testOrderOneTodoEvent()
	{
		int num;		
		TodoEvent newOne_0 = new TodoEvent(0,0,      100, 100);
		TodoEvent.orderOneTodoEvent(orgList_0, newOne_0, 300);
		num = TodoEvent.MINIMUM_PLACE + ((TodoEvent.MINIMUM_PLACE + orgList1.x + orgList1.w + TodoEvent.MARGIN_X) / TodoEvent.SHIFT_QUANTUM + 1) * TodoEvent.SHIFT_QUANTUM;
		System.out.println(newOne_0.x + " " + newOne_0.y + " " + num);
		assertTrue(num == newOne_0.x);
		assertEquals(TodoEvent.MINIMUM_PLACE,   newOne_0.y);
		
		
		TodoEvent newOne_1 = new TodoEvent(0,0,      100, 100);
		TodoEvent.orderOneTodoEvent(orgList_1, newOne_1, 300);
		int num_x = TodoEvent.MINIMUM_PLACE + ((TodoEvent.MINIMUM_PLACE + orgList1_1.x + orgList1_1.w + TodoEvent.MARGIN_X) / TodoEvent.SHIFT_QUANTUM ) * TodoEvent.SHIFT_QUANTUM;
		int num_y = TodoEvent.MINIMUM_PLACE + ((TodoEvent.MINIMUM_PLACE + orgList1_0.y + orgList1_0.h + TodoEvent.MARGIN_X) / TodoEvent.SHIFT_QUANTUM ) * TodoEvent.SHIFT_QUANTUM;
		System.out.println(newOne_1.x + " " + newOne_1.y + " " + num_x + " " + num_y);
		assertTrue(num_y == newOne_1.y);
		assertTrue(num_x == newOne_1.x);		
	}
	
	private void printTodoEvent(TodoEvent event)
	{
		System.out.println(event.ID + " " + event.x + " " + event.y + " " + event.w + " " + event.h);
	}
	
	@Test
	public void testOrderTodoEvent()
	{		
		TodoEvent.orderTodoEvent(orgList_2, 300);
		printTodoEvent(orgList_2.get(0));
		printTodoEvent(orgList_2.get(1));
		printTodoEvent(orgList_2.get(2));
		printTodoEvent(orgList_2.get(3));
		
		assertEquals(orgList_2.get(0).ID, "0");
		assertEquals(orgList_2.get(1).ID, "2");
		assertEquals(orgList_2.get(2).ID, "1");
		assertEquals(orgList_2.get(3).ID, "3");
		
		assertEquals(orgList_2.get(2).x, 155);
		assertEquals(orgList_2.get(2).y, 5);
		
		assertEquals(orgList_2.get(3).x, 5);
		assertEquals(orgList_2.get(3).y, 155);
		
	}
}
