package org.semanticweb.ontop.ontology.impl;

/*
 * #%L
 * ontop-obdalib-core
 * %%
 * Copyright (C) 2009 - 2014 Free University of Bozen-Bolzano
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.ontop.model.Predicate;
import org.semanticweb.ontop.ontology.DisjointPropertyAxiom;

public class DisjointPropertyAxiomImpl implements DisjointPropertyAxiom {

	private static final long serialVersionUID = 4456694617300452114L;
	
	private Predicate pred1;
	private Predicate pred2;
	
	DisjointPropertyAxiomImpl(Predicate p1, Predicate p2){
		this.pred1 = p1;
		this.pred2 = p2;
	}
	
	public String toString() {
		return "disjoint(" + pred1.getName() + ", " + pred2.getName() + ")";
	}

	@Override
	public Set<Predicate> getReferencedEntities() {
		Set<Predicate> res = new HashSet<Predicate>();
		res.add(pred1);
		res.add(pred2);
		return res;
	}

	@Override
	public Predicate getFirst() {
		return this.pred1;
	}

	@Override
	public Predicate getSecond() {
		return this.pred2;
	}
	
	
}