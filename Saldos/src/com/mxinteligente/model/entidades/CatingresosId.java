package com.mxinteligente.model.entidades;
// Generated 15/05/2011 12:59:36 PM by Hibernate Tools 3.2.1.GA



/**
 * CatingresosId generated by hbm2java
 */
public class CatingresosId  implements java.io.Serializable {


     private int id;
     private int usuariosId;

    public CatingresosId() {
    }

    public CatingresosId(int id, int usuariosId) {
       this.id = id;
       this.usuariosId = usuariosId;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public int getUsuariosId() {
        return this.usuariosId;
    }
    
    public void setUsuariosId(int usuariosId) {
        this.usuariosId = usuariosId;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof CatingresosId) ) return false;
		 CatingresosId castOther = ( CatingresosId ) other; 
         
		 return (this.getId()==castOther.getId())
 && (this.getUsuariosId()==castOther.getUsuariosId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getId();
         result = 37 * result + this.getUsuariosId();
         return result;
   }   


}


