package com.factory.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "factory_url")
public class Url {
	public int id;
	public String link;
	public int statu = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getStatu() {
		return statu;
	}

	public void setStatu(int statu) {
		this.statu = statu;
	}

}
