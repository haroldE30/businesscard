package com.businesscard.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.businesscard.model.BusinessCard;

public final class JSONModel extends HashMap<String, Object>{
	private static final long serialVersionUID = 1L;

	private JSONModel() {
	}
	
	public static class Builder<T> {
		private final Map<String, Object> item = new HashMap<>();
		
		public Builder(final Object entity) {
			loadItem(entity);
		}
		
		public Builder(final Collection<?> entities) {
			loadItem("businesscards", mapEntityValues(entities));
		}
		
		@SuppressWarnings("unchecked")
		private List<Map<String,String>> mapEntityValues(Collection<?> entities) {
			List<BusinessCard> list = (List<BusinessCard>) entities;
			return list.stream().map(card -> {
				Map<String, String> map = new HashMap<>();
				map.put("name", card.getName());
				map.put("enterpriseNumber", card.getEnterpriseNumber());
				map.put("language", card.getLanguage());
				return map;
			}).collect(Collectors.toList());
		}
		
		private Builder<T> loadItem(final Object entity) {
			if (entity != null) {
				item.put(entity.getClass().getSimpleName().toLowerCase(), entity);
			}
			return this;
		}
		
		private Builder<T> loadItem(final String rootName, final Object entity) {
			if (entity != null) {
				item.put(rootName, entity);
			}
			return this;
		}
		
		public JSONModel build() {
			JSONModel model = new JSONModel();
			model.putAll(item);
			return model;
		}
	}
}
