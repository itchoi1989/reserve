package com.narratage.reserve.inform.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

/**
 * 기존의 HashMap은 하나의 key값을 통해서 하나의 value를 가져옵니다. 항공운항정보에서 출발공항의 값을 통해서, 도착공항의 값을
 * 통해서, 또는 시작공항과 도착공항 두개의 값을 통해서 운항정보를 가져올 수 있어야 하기 떄문에 기존의 자료구조형 Class를 사용할 수
 * 없기때문에 직접 정의하였습니다.
 * 
 * 1개의 ArrayList에서는 핵심 데이터를 저장합니다. 2개의 HashMap에서는 각각의 Row와 Column을 정의하며 내부에
 * Set<Integer> 을 활용하여 핵심데이터 ArrayList의 index값을 저장합니다.
 * 
 * 예외처리, 복잡한 메서드는 구현하지 않았습니다. 항공정보에서만 사용할 것으로 예상됩니다.
 * 
 * @author StevePak
 * 
 * @param <Row>
 *            Row값을 정합니다.
 * @param <Column>
 *            Column값을 정합니다.
 * @param <Value>
 *            Value값을 정합니다.
 */
public class Table<Row, Column, Value> {
	private int CANNOT_FIND_INDEX = -1;

	private ArrayList<Value> coreDataList;
	private HashMap<Row, TreeSet<Integer>> rowIndexMap;
	private HashMap<Column, TreeSet<Integer>> columnIndexMap;

	public Table() {
		coreDataList = new ArrayList<Value>();
		rowIndexMap = new HashMap<Row, TreeSet<Integer>>();
		columnIndexMap = new HashMap<Column, TreeSet<Integer>>();
	}

	public int rowSize() {
		return rowIndexMap.size();
	}

	public int columnSize() {
		return columnIndexMap.size();
	}

	public int size() {
		return coreDataList.size();
	}

	public Value get(Row row, Column column) {
		return coreDataList.get(this.getIndexOfRowColumn(row, column));
	}

	public boolean isExist(Row row, Column column) {
		int idx = this.getIndexOfRowColumn(row, column);

		if (idx == CANNOT_FIND_INDEX) {
			return false;
		} else {
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	private int getIndexOfRowColumn(Row row, Column column) {
		TreeSet<Integer> tmpRowSet = null;
		
		try {
			tmpRowSet = (TreeSet<Integer>) rowIndexMap.get(row).clone();
			tmpRowSet.retainAll(columnIndexMap.get(column));
			
			if (tmpRowSet.size() == 1)
				return tmpRowSet.pollFirst();
			else if (tmpRowSet.size() == 0)
				return CANNOT_FIND_INDEX;
			else
				throw new RuntimeException("getIndexOfRowColumn Exception");
			
		} catch (NullPointerException e) {
			return CANNOT_FIND_INDEX;
		}
		
	}

	public void put(Row row, Column column, Value value) {
		int idx = getIndexOfRowColumn(row, column);
		if (idx != CANNOT_FIND_INDEX) {
			coreDataList.set(this.getIndexOfRowColumn(row, column), value);
		} else {
			coreDataList.add(value);

			if (!rowIndexMap.containsKey(row))
				rowIndexMap.put(row, new TreeSet<Integer>());
			if (!columnIndexMap.containsKey(column))
				columnIndexMap.put(column, new TreeSet<Integer>());

			rowIndexMap.get(row).add(coreDataList.size() - 1);
			columnIndexMap.get(column).add(coreDataList.size() - 1);
		}
	}

	public Map<Column, Value> getByRow(Row row) {
		Map<Column, Value> returnMap = new HashMap<Column, Value>();
		Iterator<Column> iter = columnIndexMap.keySet().iterator();
		while (iter.hasNext()) {
			Column col = iter.next();
			returnMap.put(col, get(row, col));
		}
		return returnMap;
	}

	public Map<Row, Value> getByColumn(Column col) {
		Map<Row, Value> returnMap = new HashMap<Row, Value>();
		Iterator<Row> iter = rowIndexMap.keySet().iterator();
		while (iter.hasNext()) {
			Row row = iter.next();
			returnMap.put(row, get(row, col));
		}
		return returnMap;
	}
}
