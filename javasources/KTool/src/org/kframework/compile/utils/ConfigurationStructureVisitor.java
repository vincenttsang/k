package org.kframework.compile.utils;

import org.kframework.kil.*;
import org.kframework.kil.loader.DefinitionHelper;
import org.kframework.kil.visitors.BasicVisitor;

import java.util.Stack;

public class ConfigurationStructureVisitor extends BasicVisitor {

    Stack<ConfigurationStructure> ancestors = new Stack<ConfigurationStructure>();

	private ConfigurationStructureMap config;
	private int maxLevel = 0;

	public ConfigurationStructureVisitor(ConfigurationStructureMap cfgStr, DefinitionHelper definitionHelper) {
		super(definitionHelper);
		this.config = cfgStr;
	}

	@Override
	public void visit(Configuration node) {
		Term t = node.getBody();
		Cell top = new Cell();
		top.setLabel(MetaK.Constants.generatedCfgAbsTopCellLabel);
		top.setId(MetaK.Constants.generatedCfgAbsTopCellLabel);
		top.setContents(t);
		top.accept(this);
	}
	
	@Override
	public void visit(Cell node) {
		ConfigurationStructure cfg = new ConfigurationStructure();
		cfg.multiplicity = node.getMultiplicity();
		cfg.id = node.getId();
		cfg.cell = node;
		if (!ancestors.empty()) {
			ConfigurationStructure parent = ancestors.peek();
			cfg.level = parent.level+1;
			cfg.parent = parent;
			if (cfg.level > maxLevel) maxLevel = cfg.level;
			parent.sons.put(cfg.id, cfg);
		}
		ancestors.push(cfg);
		super.visit(node);
		ancestors.pop();
		config.put(cfg.id, cfg);
	}
	
	@Override
	public void visit(Context node) {
	}

	@Override
	public void visit(Syntax node) {
	}

	@Override
	public void visit(Rule node) {
	}

	public int getMaxLevel() {
		return maxLevel;
	}
}