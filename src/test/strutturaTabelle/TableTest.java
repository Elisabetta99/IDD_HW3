package test.strutturaTabelle;


import java.util.ArrayList;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import main.strutturaTabelle.Celle;
import main.strutturaTabelle.Coordinate;
import main.strutturaTabelle.Tabelle;

public class TableTest {

	private Coordinate coord1 = new Coordinate(1,1);
	private Coordinate coord2 = new Coordinate(2,1);
	private Coordinate coord3 = new Coordinate(1,2);
	private Coordinate coord4 = new Coordinate(2,3);
	private Celle c1 = new Celle(false, coord1, "prova_1");
	private Celle c2 = new Celle(true, coord2, "prova_2");
	private Celle c3 = new Celle(false, coord3, "prova_3");
	private Celle c4 = new Celle(false, coord4, "prova_4");

	private Collection<Celle> collections = new ArrayList<Celle>();
	private Map<Integer, List<Celle>> mappaColonne = new HashMap<>();

	@Test
	public void test() throws Exception {

		collections.add(c3);
		collections.add(c2);
		collections.add(c4);
		collections.add(c1);

		Tabelle table = new Tabelle();
		table.setCollectionCells(collections);
		table.setMappaColonne(mappaColonne);
		table.createCells();
		System.out.println(collections);
		System.out.println(mappaColonne);
	}
}
