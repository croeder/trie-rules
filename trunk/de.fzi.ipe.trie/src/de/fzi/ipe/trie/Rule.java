package de.fzi.ipe.trie;

public class Rule {

	private Atom[] head;
	private Atom[] body;
	private String name;
	private String comment;
	
	public Rule(String name, String comment, Atom[] head, Atom[] body) {
		this.head = head;
		this.body = body;
		this.name = name;
		this.comment = comment;
	}
	
	public String getName() {
		return name;
	}
	
	public String getComment() {
		return comment;
	}
	
	public Atom[] getHead() {
		return cloneAtoms(head); //defensive copy, because I really change the atom objects a lot. 
	}
	
	public Atom[] getBody() {
		return cloneAtoms(body); //defensive copy, because I really change the atom objects a lot. 
	}
	
	private Atom[] cloneAtoms(Atom[] atoms) {
		Atom[] clone = new Atom[atoms.length];
		for (int i=0;i<clone.length;i++) {
			clone[i] = atoms[i].clone();
		}
		return clone;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("["+name+":\n");
		for (Atom a:head) builder.append("\t"+a+"\n");
		builder.append("<-\n");
		for (Atom a:body) builder.append("\t"+a+"\n");
		builder.append("]");
		return builder.toString();
	}
	
	
}
