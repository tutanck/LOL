package mapreduce;

import com.mongodb.DBObject;

/**
 * @author AJoan
 *Principe : 
 *RSV : Relevance Score Value 
 *Le RSV ou score de pertinence est calcule par :
RSV (d,q)= Sum(tf-idf(w,d) foreach w in d and w in q)
On n'appelle cela un Modele vectoriel car RSV (d,q) correspond
a un produit scalaire entre un vecteur de document et un vecteur de requete 
Ce score va definir l'ordonnancement des resultats de recherche :
Pour chaque requete q et chaque document d, le moteur de
recherche calcule un score de pertinence (RSV : Relevance Score
Value) note RSV (q,d). Ce score permet de classer les resultats
pour une requete q donnee par ordre decroissant de pertinence.*/
public class ObjetRSV implements Comparable<ObjetRSV>{
	
	private DBObject dbo; 
	private Double rsv;		//score Rsv

	public ObjetRSV(DBObject d, double r){
		this.dbo = d;
		rsv = r;}

	public DBObject getDbo() {return dbo;}

	public void setDbo(DBObject dbo) {this.dbo = dbo;}

	public Double getRsv() {return rsv;}

	public void setRsv(Double rsv) {this.rsv = rsv;}

	public int compareTo(ObjetRSV o) {
		return rsv.compareTo(o.getRsv());}
	
	@Override
	public String toString() {
		return "(dbo="+dbo+")& (score rsv="+rsv+")";}
}
