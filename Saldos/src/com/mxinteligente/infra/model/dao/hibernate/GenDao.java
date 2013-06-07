package com.mxinteligente.infra.model.dao.hibernate;

import java.io.Serializable;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.mxinteligente.infra.model.dao.GenDaoI;


@Repository("genDao")
public class GenDao extends GenericoDao<Object, Serializable> implements GenDaoI{
	
}
