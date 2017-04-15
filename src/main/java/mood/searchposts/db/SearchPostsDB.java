package mood.searchposts.db;

import java.util.List;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import db.mapreduce.MapReduce;
import db.mapreduce.ObjetRSV;
import db.tools.DbException;


/**
 * @author AJoan 
 * the letter Q means Query to indicate it's query driven search
 *  (Qyery Matching) */
public class SearchPostsDB {

	public static List<ObjetRSV> QMatchingAllPosts(String query)throws DBException{
		List<ObjetRSV> results=MapReduce.pertinence(query,PostsDB.posts());
		if(results.isEmpty()){ 
			System.out.println("no pertinent results switching to SQLMODO");
			DBCursor cursor=PostsDB.postsSQLMODO(query);
			while(cursor.hasNext()){
				DBObject doc = cursor.next();
				results.add(new ObjetRSV(doc,1));}}
		return results; }

	public static List<ObjetRSV> QMatchingFriendsPosts(String query,String uid)throws DBException{
		List<ObjetRSV> results= MapReduce.pertinence(query,PostsDB.friendsPosts(uid));
		if(results.isEmpty()){
			System.out.println("no pertinent results switching to SQLMODO");
			DBCursor cursor=PostsDB.friendsPostsSQLMODO(query,uid);
			while(cursor.hasNext()){
				DBObject doc = cursor.next();
				results.add(new ObjetRSV(doc,1));}}
		return results; }

	public static List<ObjetRSV> QMatchingMyPosts(String query,String uid)throws DBException{
		List<ObjetRSV> results= MapReduce.pertinence(query,PostsDB.userPosts(uid));
		if(results.isEmpty()){
			System.out.println("no pertinent results switching to SQLMODO");
			DBCursor cursor=PostsDB.userPostsSQLMODO(query,uid);
			while(cursor.hasNext()){
				DBObject doc = cursor.next();
				results.add(new ObjetRSV(doc,1));}}
		return results; }
	
	public static List<ObjetRSV> QMatchingPublicPosts(String query,String uid)throws DBException{
		List<ObjetRSV> results= MapReduce.pertinence(query,PostsDB.publicPosts(uid));
		if(results.isEmpty()){
			System.out.println("no pertinent results switching to SQLMODO");
			DBCursor cursor=PostsDB.publicPostsSQLMODO(query,uid);
			while(cursor.hasNext()){
				DBObject doc = cursor.next();
				results.add(new ObjetRSV(doc,1));}}
		return results; }
	
	
	public static void main(String[] args) throws DBException {
		System.out.println("all->"+QMatchingAllPosts("HOHO")+"\n");
		System.out.println("friends->"+QMatchingFriendsPosts(
				"HOHO","d910952c404b4b6cca5d6f61a5ab9df0")+"\n");
		System.out.println("me->"+QMatchingMyPosts(
				"503","d910952c404b4b6cca5d6f61a5ab9df0")+"\n");
		System.out.println("public->"+QMatchingPublicPosts(
				"paris ","d910952c404b4b6cca5d6f61a5ab9df0")+"\n");}
}