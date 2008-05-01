package de.fzi.ipe.trie.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.fzi.ipe.trie.GroundTerm;
import de.fzi.ipe.trie.URITerm;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;
import de.fzi.ipe.trie.inference.executionTree.simple.ExecutionTreeElementImpl;

public class VariableBindingsTest {

	class TestExecutionTreeElement extends ExecutionTreeElementImpl {

		
	}
	
	
	@Test
	public void testRemoveBindings() {
		VariableBindings vb = new VariableBindings();
		
		ExecutionTreeElement elem1 = new TestExecutionTreeElement();
		GroundTerm term1 = new URITerm("1");
		ProofVariable var1 = new ProofVariable("1");
		
		vb.addVariableBinding(var1, term1, elem1);
		assertTrue (term1.equals(vb.getVariableBinding(var1)));

		
		ExecutionTreeElement elem2 = new TestExecutionTreeElement();
		GroundTerm term2 = new URITerm("2");
		ProofVariable var2 = new ProofVariable("1");

		assertNull(vb.getVariableBinding(var2));
		assertEquals(vb.getCurrentTerm(var2), var2);
		vb.addVariableBinding(var2, term2, elem2);
		assertNotNull(vb.getVariableBinding(var2));
		assertEquals(vb.getCurrentTerm(var2),term2);
		
		
		vb.removeBindings(elem2);
		assertNull(vb.getVariableBinding(var2));
		assertEquals(vb.getCurrentTerm(var2), var2);
		assertTrue (term1.equals(vb.getVariableBinding(var1)));
		
		vb.addVariableBinding(var2, term2, elem2);
		assertNotNull(vb.getVariableBinding(var2));
		assertEquals(vb.getCurrentTerm(var2),term2);
	}

}
