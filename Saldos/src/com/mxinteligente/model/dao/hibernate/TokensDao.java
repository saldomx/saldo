package com.mxinteligente.model.dao.hibernate;

import java.io.Serializable;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mxinteligente.infra.model.dao.hibernate.GenericoDao;
import com.mxinteligente.model.dao.TokensDaoI;
import com.mxinteligente.model.entidades.Tokens;


@Repository("tokensDao")
public class TokensDao extends GenericoDao<Tokens, Serializable> implements TokensDaoI {

	
	public boolean eliminarToken(String SessionID) {
		Session ses =null;
		Transaction tx=null;
		try{
		 ses= super.getSession();
		 tx = ses.beginTransaction();
		Query query = ses.createSQLQuery("DELETE FROM tokens WHERE sessionID = '"+SessionID+"'");
		query.executeUpdate();
		tx.commit();
		return true;
		}catch(Exception e){
			tx.rollback();
			e.printStackTrace();
			return false;
		}
		finally{
			ses.close();
		}
	}

}
