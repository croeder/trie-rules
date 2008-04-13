package de.fzi.ipe.trie;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Node_Literal;
import com.hp.hpl.jena.graph.Node_URI;
import com.hp.hpl.jena.reasoner.TriplePattern;
import com.hp.hpl.jena.reasoner.rulesys.ClauseEntry;


/**
 * Rule parser that utilizes the Jena rule parser. Could be made a lot faster by simply implementing 
 * a real parser - to lazy to do that now. Note that Try! currently supports only a subset of the 
 * rules in the Jena framework: those without any functors.
 */
public class RuleParser {
	
	
	@SuppressWarnings("unchecked")
	public static Collection<Rule> readRules(File file) throws IOException{
		Set<Rule> toReturn = new HashSet<Rule>();
		List<com.hp.hpl.jena.reasoner.rulesys.Rule> jenaRules = com.hp.hpl.jena.reasoner.rulesys.Rule.rulesFromURL(file.toURL().toString());
		Map<Node,Variable> variableMap = new HashMap<Node,Variable>();
		for (com.hp.hpl.jena.reasoner.rulesys.Rule r:jenaRules) {
			Atom[] body = getAtoms(r.getBody(),variableMap);
			Atom[] head = getAtoms(r.getHead(),variableMap);
			Rule currentRule = new Rule(r.getName(),head,body);
			toReturn.add(currentRule);
		}
		return toReturn;
	}
	
	public static Atom[] getAtoms(ClauseEntry[] entries,Map<Node,Variable> variableMap) {
		Atom[] toReturn = new Atom[entries.length];
		for (int i=entries.length-1;i>=0;i--) {
			TriplePattern currentPattern = (TriplePattern) entries[i];
			Term s = getTerm(currentPattern.getSubject(),variableMap);
			Term p = getTerm(currentPattern.getPredicate(),variableMap);
			Term o = getTerm(currentPattern.getObject(),variableMap);
			toReturn[i] = new Atom(s,p,o);
		}
		return toReturn;
	}
	

	public static Term getTerm(Node node,Map<Node,Variable> variableMap) {
		if (node.isVariable()) {
			Variable v = variableMap.get(node);
			if (v==null) {
				v = new Variable(node.toString());
				variableMap.put(node, v);
			}
			return v;
		}
		else {
			if (node instanceof Node_URI) return new URITerm(node);
			else if (node instanceof Node_Literal) return new LiteralTerm(node);
			else throw new ClassCastException("Expected URI or literal, got "+node.getClass());
		}
	}
	
	
}
