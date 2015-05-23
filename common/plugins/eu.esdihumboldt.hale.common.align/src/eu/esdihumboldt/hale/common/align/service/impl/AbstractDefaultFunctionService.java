/*
 * Copyright (c) 2015 Data Harmonisation Panel
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Data Harmonisation Panel <http://www.dhpanel.eu>
 */

package eu.esdihumboldt.hale.common.align.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eu.esdihumboldt.hale.common.align.extension.function.FunctionDefinition;
import eu.esdihumboldt.hale.common.align.extension.function.PropertyFunctionDefinition;
import eu.esdihumboldt.hale.common.align.extension.function.TypeFunctionDefinition;
import eu.esdihumboldt.hale.common.align.extension.function.custom.CustomPropertyFunction;
import eu.esdihumboldt.hale.common.align.model.Alignment;

/**
 * Function service that includes dynamic content in addition to the statically
 * defined functions.
 * 
 * @author Simon Templer
 */
public abstract class AbstractDefaultFunctionService extends StaticFunctionService {

	/**
	 * @return the current alignment
	 */
	protected abstract Alignment getCurrentAlignment();

	@Override
	public FunctionDefinition<?> getFunction(String id) {
		FunctionDefinition<?> function = super.getFunction(id);

		if (function == null) {
			return getCustomPropertyFunction(id);
		}
		return function;
	}

	private PropertyFunctionDefinition getCustomPropertyFunction(String id) {
		Alignment al = getCurrentAlignment();
		if (al != null) {
			CustomPropertyFunction cf = al.getCustomPropertyFunctions().get(id);
			if (cf != null) {
				return cf.getDescriptor();
			}
		}

		return null;
	}

	@Override
	public PropertyFunctionDefinition getPropertyFunction(String id) {
		PropertyFunctionDefinition function = super.getPropertyFunction(id);

		if (function == null) {
			return getCustomPropertyFunction(id);
		}
		return function;
	}

	@Override
	public TypeFunctionDefinition getTypeFunction(String id) {
		return super.getTypeFunction(id);
	}

	@Override
	public Collection<? extends TypeFunctionDefinition> getTypeFunctions() {
		return super.getTypeFunctions();
	}

	@Override
	public Collection<? extends PropertyFunctionDefinition> getPropertyFunctions() {
		Collection<? extends PropertyFunctionDefinition> functions = super.getPropertyFunctions();

		Alignment al = getCurrentAlignment();
		if (al != null) {
			List<PropertyFunctionDefinition> cfs = new ArrayList<>();
			for (CustomPropertyFunction cf : al.getCustomPropertyFunctions().values()) {
				cfs.add(cf.getDescriptor());
			}
			cfs.addAll(functions);

			functions = cfs;
		}

		return functions;
	}

	@Override
	public Collection<? extends TypeFunctionDefinition> getTypeFunctions(String categoryId) {
		return super.getTypeFunctions(categoryId);
	}

	@Override
	public Collection<? extends PropertyFunctionDefinition> getPropertyFunctions(String categoryId) {
		Collection<? extends PropertyFunctionDefinition> functions = super.getPropertyFunctions();

		Alignment al = getCurrentAlignment();
		if (al != null && categoryId == null) { // XXX for now custom functions
												// in category OTHER
			List<PropertyFunctionDefinition> cfs = new ArrayList<>();
			for (CustomPropertyFunction cf : al.getCustomPropertyFunctions().values()) {
				cfs.add(cf.getDescriptor());
			}
			cfs.addAll(functions);

			functions = cfs;
		}

		return functions;
	}

}