package de.fzi.ipe.trie;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Resource;

public class URITerm extends GroundTerm {

	private String uri;
	
	
	public URITerm(Node node) {
		assert node.isURI();
		
		this.uri= node.getURI();
	}
	
	public URITerm(String uri) {
		this.uri = uri;
	}
	
	public URITerm(Resource node) {
		this.uri = node.getURI();
	}
	
	public String getUri() {
		return uri;
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof URITerm)) return false;
		return ((URITerm)other).uri.equals(uri);
	}

	public int hashCode() {
		return uri.hashCode();
	}
	
	public String toString() {
		return uri.toString();
	}
	
	
}
