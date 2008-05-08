package de.fzi.ipe.trie;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Node_Literal;
import com.hp.hpl.jena.rdf.model.Literal;

public class LiteralTerm extends GroundTerm{

	private String literalValue;
	
	public LiteralTerm(Literal literal) {
		this.literalValue = literal.toString();
	}
	
	public LiteralTerm(Node node) {
		this.literalValue = ((Node_Literal) node).getLiteral().getLexicalForm();
	}

	public boolean equals(Object other) {
		if (other instanceof LiteralTerm) {
			return ((LiteralTerm)other).literalValue.equals(literalValue);
		}
		else return false;
	}

	public int hashCode() {
		return literalValue.hashCode();
	}
	
	public String toString() {
		return "\""+literalValue+"\"";
	}

	public String getLiteral() {
		return literalValue;
	}
}
