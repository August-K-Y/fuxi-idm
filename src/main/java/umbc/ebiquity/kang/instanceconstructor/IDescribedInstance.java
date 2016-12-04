package umbc.ebiquity.kang.instanceconstructor;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import umbc.ebiquity.kang.entityframework.object.Concept;

public interface IDescribedInstance extends IInstance {

	public Set<Concept> getConcepts();

	public Set<IRelation> getRelations();

	public IValuedRelation getRelationValueByName(String name);

	public Collection<String> getTaxonomicRelationValue();

	public Set<IValuedRelation> getValuedRelations();

	public Map<String, Set<String>> getTaxonomicRelation2ValueMap();

	public String getProcessedSubjectLabel();
	
	public boolean containsConcept();

}
