package org.kframework.backend.symbolic;

import org.kframework.kil.ASTNode;
import org.kframework.kil.Configuration;
import org.kframework.kil.Term;
import org.kframework.kil.loader.DefinitionHelper;
import org.kframework.kil.visitors.CopyOnWriteTransformer;
import org.kframework.kil.visitors.exceptions.TransformerException;
/**
 * Search for input stream cell in the configuration and plug
 * a variable into it. Its purpose is to send symbolic values
 * as input using -cIN option.
 * @author andreiarusoaie
 *
 */
public class ResolveSymbolicInputStream extends CopyOnWriteTransformer {

	public ResolveSymbolicInputStream(DefinitionHelper definitionHelper) {
		super("Resolve input stream for symbolic execution", definitionHelper);
	}

	@Override
	public ASTNode transform(Configuration node) throws TransformerException {
		
		ResolveInputStreamCell risc = new ResolveInputStreamCell(definitionHelper);
		Term content = (Term) node.getBody().accept(risc);
		
		node.shallowCopy();
		node.setBody(content);
		return node;
	}
}
