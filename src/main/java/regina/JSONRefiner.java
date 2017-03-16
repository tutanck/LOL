package regina;

import org.json.JSONObject;

import java.util.*;

/**
 * @author ANAGBLA Joan */
//TODO relire attentivement tt repose su ca
public class JSONRefiner {	

	/**
	 * @description 
	 * Return an JSONObject equivalent of the {map}
	 * @param map
	 * @return */
	public static JSONObject jsonify(
			Map<?,?> map
			){
		return new JSONObject(map);
	}


	/**
	 * @description
	 * Return a sliced json according to the subset of {subKeys}
	 * The sliced json is a copy of the {whole} json and does not undergo any changes.
	 * 
	 * @param whole
	 * @param subKeys
	 * @return */
	public static JSONObject slice(
			JSONObject whole,
			String[]subKeys
			){
		JSONObject sliced= new JSONObject();
		for(String key : new HashSet<String>(Arrays.asList(subKeys)))
			if(whole.has(key))
				sliced.put(key, whole.get(key));
		return sliced;
	}



	/**
	 * @Description 
	 * Subdivide a json's {trunc} in two json's branches following {subKeys} keys
	 * One branch will contains all entries whose key is in {subKeys}
	 * The other branch will contains all entries in initial {trunc} except whose key is in {subKeys}
	 * @param trunc
	 * @param subKeys
	 * @return  */
	public static List<JSONObject> branch(
			JSONObject trunc,
			String[]subKeys
			){
		JSONObject branch1 = new JSONObject();
		JSONObject branch2 = new JSONObject(trunc);

		Set<String>subKeysSet = new HashSet<String>(Arrays.asList(subKeys));
		for(String key : trunc.keySet())
			if(subKeysSet.contains(key))
				branch1.put(key, trunc.get(key));
			else	
				branch2.put(key,trunc.get(key));

		List<JSONObject>node = new ArrayList<>();
		node.add(0,branch1);
		node.add(1,branch2);
		return node;
	}



	/**
	 * @Description
	 * Rename {json}'s keys by replacing them 
	 * by the associated value in the {keyMap} without
	 * changing the associated values in the {json}.
	 * No change is performed on the keys that are not in {keyMap}.
	 * 
	 * @param json
	 * @param keyMap
	 * @return  */
	public static JSONObject renameJSONKeys(
			JSONObject json,
			Map<String,String> keyMap
			){
		JSONObject aliasJSON = new JSONObject();
		for(String key : json.keySet())
			if(keyMap.containsKey(key))
				aliasJSON.put(keyMap.get(key), json.get(key));
			else
				aliasJSON.put(key, json.get(key));
		return aliasJSON;
	}



	public static void main(String[] args) {
		JSONObject jo = new JSONObject()
				.put("lol0","oui")
				.put("lol1",12.7)
				.put("lol2",true)
				.put("lol3",12);

		System.out.println("sliced : "+slice(jo, new String[]{"lol1","lol3"}));
		System.out.println("jo : "+jo+"\n");

		System.out.println("node : "+branch(jo,new String[]{"lol1","lol2"}));
		System.out.println("jo : "+jo+"\n");

		Map<String, String> kmap=new HashMap<>();
		kmap.put("lol", "newlol"); //lol don't exist in jo
		kmap.put("lol1", "newlol1");
		kmap.put("lol3", "newlol3");

		System.out.println("aliasMap : "+renameJSONKeys(jo,kmap));
		System.out.println("jo : "+jo+"\n");
	}

}