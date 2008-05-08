package de.fzi.ipe.trie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Node_Literal;
import com.hp.hpl.jena.graph.Node_URI;
import com.hp.hpl.jena.reasoner.TriplePattern;
import com.hp.hpl.jena.reasoner.rulesys.ClauseEntry;

import de.fzi.ipe.trie.inference.RuleImpl;


/**
 * Rule parser that utilizes the Jena rule parser. Could be made a lot faster by simply implementing 
 * a real parser - to lazy to do that now. Note that Try! currently supports only a subset of the 
 * rules in the Jena framework: those without any functors.
 */
public class RuleParser {
	
	// \[\s*([^:\s]+)\s*:
	private static final Pattern RULE_NAME_PATTERN = Pattern.compile("\\[\\s*([^:\\s]+)\\s*:");
	private static final Matcher RULE_NAME_MATCHER = RULE_NAME_PATTERN.matcher("");
	
	
	@SuppressWarnings("unchecked")
	public static Collection<Rule> readRules(File file) throws IOException{
		Map<String, String> ruleComments = parseRuleComments(file);
		Set<Rule> toReturn = new HashSet<Rule>();
		List<com.hp.hpl.jena.reasoner.rulesys.Rule> jenaRules = com.hp.hpl.jena.reasoner.rulesys.Rule.rulesFromURL(file.toURL().toString());
		Map<Node,Variable> variableMap = new HashMap<Node,Variable>();
		for (com.hp.hpl.jena.reasoner.rulesys.Rule r:jenaRules) {
			Atom[] body = getAtoms(r.getBody(),variableMap);
			Atom[] head = getAtoms(r.getHead(),variableMap);
			Rule currentRule = new RuleImpl(r.getName(),ruleComments.get(r.getName()),head,body);
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
	
	/** 
	 * Reads the rule comments from the file. What this method does: it takes all comment lines before a rule and stores 
	 * these in a hashMap. This parser is not very robust .. in particular it assumes that the rule declaration has the rule name and 
	 * the open square bracket on one line. 
	 * @param file That is read and expected to contain the rules and their comments. 
	 * @throws IOException If reading the file fails for any reason
	 */
	public static Map<String,String> parseRuleComments(File file) throws IOException {
		Map<String,String> ruleComments = new HashMap<String,String>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		try {
			String currentLine = reader.readLine();
			StringBuilder currentComment = new StringBuilder();
			while (currentLine != null) {
				if (isCommentLine(currentLine)) {
					currentComment.append(getComment(currentLine));
					currentComment.append(" ");
				}
				else if (isRuleDeclaration(currentLine)) {
					ruleComments.put(getRuleName(currentLine), currentComment.toString());
					currentComment = new StringBuilder();
				}
				else if (currentLine.trim().length() == 0) {
					if (currentComment.length()>0) currentComment = new StringBuilder();
				}
				currentLine = reader.readLine();
			}
		} finally {
			reader.close();
		}
		return ruleComments;
	}

	private static boolean isRuleDeclaration(String currentLine) {
		return RULE_NAME_MATCHER.reset(currentLine).find();
	}
	
	private static String getRuleName(String currentLine) {
		return RULE_NAME_MATCHER.group(1);
	}
	
	private static boolean isCommentLine(String currentLine) {
		return currentLine.trim().startsWith("#");
	}
	
	private static String getComment(String currentLine) {
		currentLine = currentLine.trim();
		return currentLine.substring(1,currentLine.length());
	}
	
	
}
