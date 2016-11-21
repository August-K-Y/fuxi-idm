package umbc.ebiquity.kang.instanceconstructor.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import umbc.ebiquity.kang.entityframework.object.Concept;
import umbc.ebiquity.kang.instanceconstructor.IInstanceDescriptionModel;
import umbc.ebiquity.kang.instanceconstructor.impl.Triple.PredicateType;

public class ModelSaveSupport {
	
	public RecordsHolder record(IInstanceDescriptionModel model) {
		System.out.println("Record Model ...");
		int numberOfRecords = 0;
		int numberOfRelations = 0;
//		StringBuilder triplesStringBuilder = new StringBuilder();
		RecordsHolder recordsHolder = new RecordsHolder(); 
		for (InstanceTripleSet instanceTripleSet : model.getInstanceTripleSets()) {
			String subjectLabel = instanceTripleSet.getSubjectLabel();
			Map<String, Set<String>> relation2ValueMap = instanceTripleSet.getRelation2ValueMap();
			for (String relationLabel : relation2ValueMap.keySet()) {
				for (String valueLabel : relation2ValueMap.get(relationLabel)) {
					Map<String, String> record = new LinkedHashMap<String, String>();
					record.put(RepositorySchemaConfiguration.TRIPLE_RECORD_TYPE,
							RepositorySchemaConfiguration.TRIPLE_RECORD_TYPE_RELATION_VALUE);
					record.put(RepositorySchemaConfiguration.TRIPLE_SUBJECT, subjectLabel);
					record.put(RepositorySchemaConfiguration.TRIPLE_PREDICATE, relationLabel);
					record.put(RepositorySchemaConfiguration.TRIPLE_OBJECT, valueLabel);

					String predicateTypeStr;
					if (Triple.BuiltinPredicateSet.contains(relationLabel)) {
						predicateTypeStr = PredicateType.Builtin.toString();
					} else {
						predicateTypeStr = PredicateType.Custom.toString();
					}

					record.put(RepositorySchemaConfiguration.NORMALIZED_TRIPLE_OBJECT, "");
					record.put(RepositorySchemaConfiguration.NORMALIZED_TRIPLE_SUBJECT, "");
					record.put(RepositorySchemaConfiguration.TRIPLE_PREDICATE_TYPE, predicateTypeStr);
					addRecord(recordsHolder, record);
					numberOfRecords++;
				}
			}
			
			Map<String, Set<Concept>> instance2ConceptSetMap = instanceTripleSet.getInstance2ConceptualSetMap();
			for (String instanceLabel : instance2ConceptSetMap.keySet()) {
				for (Concept concept : instance2ConceptSetMap.get(instanceLabel)) {
					Map<String, String> record = new LinkedHashMap<String, String>();
					record.put(RepositorySchemaConfiguration.TRIPLE_RECORD_TYPE,
							RepositorySchemaConfiguration.TRIPLE_RECORD_TYPE_CONCEPT_OF_INSTANCE);
					record.put(RepositorySchemaConfiguration.TRIPLE_SUBJECT, subjectLabel);
					record.put(RepositorySchemaConfiguration.TRIPLE_PREDICATE, instanceLabel);
					record.put(RepositorySchemaConfiguration.TRIPLE_OBJECT, concept.getConceptName());
					record.put(RepositorySchemaConfiguration.TRIPLE_OBJECT_AS_CONCEPT_SCORE, String.valueOf(concept.getScore()));

					String predicateTypeStr = "";
					if (Triple.BuiltinPredicateSet.contains(instanceLabel)) {
						predicateTypeStr = PredicateType.Builtin.toString();
					} else {
						predicateTypeStr = PredicateType.Custom.toString();
					}

					record.put(RepositorySchemaConfiguration.NORMALIZED_TRIPLE_OBJECT, "");
					record.put(RepositorySchemaConfiguration.NORMALIZED_TRIPLE_SUBJECT, "");
					record.put(RepositorySchemaConfiguration.TRIPLE_PREDICATE_TYPE, predicateTypeStr);
					/*
					 * 
					 */
					String isFromInstance = String.valueOf(concept.isFromInstance());
					record.put(RepositorySchemaConfiguration.IS_FROM_INSTANCE, isFromInstance);
					addRecord(recordsHolder, record);
					numberOfRecords++;
				}
			}
		}

		for (Triple triple : model.getRelationTypeTriple()) {
			// create data records for relation-to-property mappings
			Map<String, String> record = new LinkedHashMap<String, String>();
			record.put(RepositorySchemaConfiguration.TRIPLE_RECORD_TYPE,
					RepositorySchemaConfiguration.TRIPLE_RECORD_TYPE_RELATION_DEFINITION);
			record.put(RepositorySchemaConfiguration.TRIPLE_SUBJECT, triple.getSubject());
			record.put(RepositorySchemaConfiguration.TRIPLE_PREDICATE, triple.getPredicate());
			record.put(RepositorySchemaConfiguration.TRIPLE_OBJECT, triple.getObject());

			String predicateTypeStr = "";
			if (Triple.BuiltinPredicateSet.contains(triple.getPredicate())) {
				predicateTypeStr = PredicateType.Builtin.toString();
			} else {
				predicateTypeStr = PredicateType.Custom.toString();
			}

			record.put(RepositorySchemaConfiguration.NORMALIZED_TRIPLE_OBJECT, "");
			record.put(RepositorySchemaConfiguration.NORMALIZED_TRIPLE_SUBJECT, "");
			record.put(RepositorySchemaConfiguration.TRIPLE_PREDICATE_TYPE, predicateTypeStr);
			addRecord(recordsHolder, record);
			numberOfRecords++;
			numberOfRelations++;
		}
		
		// create data record for meta-data
		Map<String, String> metaDataRecord = new LinkedHashMap<String, String>();
		metaDataRecord.put(RepositorySchemaConfiguration.TRIPLE_RECORD_TYPE, RepositorySchemaConfiguration.TRIPLE_RECORD_TYPE_META_DATA);
		metaDataRecord.put(RepositorySchemaConfiguration.TRIPLE_STORE_URI, model.getSourceURL().toString());
		metaDataRecord.put(RepositorySchemaConfiguration.TRIPLE_STORE_NUMBER_OF_TRIPLES, String.valueOf(numberOfRecords));
		metaDataRecord.put(RepositorySchemaConfiguration.TRIPLE_STORE_NUMBER_OF_RELATIONS, String.valueOf(numberOfRelations));
		addRecord(recordsHolder, metaDataRecord);

		recordsHolder.setNumOfRelations(numberOfRelations);
		
		List<String> records2 = recordsHolder.getRecordsAsString();
		for (String record : records2) {
			System.out.println("@@@: " + record);
		}
		
		List<Map<String, String>> records = recordsHolder.getRecords();
		for (Map<String, String> record : records) {
			System.out.println("###: " + record);
		}
		
		
		return recordsHolder;
	}
	
	private void addRecord(RecordsHolder recordsHolder, Map<String, String> metaDataRecord) {
		recordsHolder.addRecord(metaDataRecord);
	}

}
